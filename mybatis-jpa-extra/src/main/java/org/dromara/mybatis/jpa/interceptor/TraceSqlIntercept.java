/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.mybatis.jpa.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.dromara.mybatis.jpa.constants.ConstMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class TraceSqlIntercept  implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(TraceSqlIntercept.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 计算这一次SQL执行钱后的时间，统计一下执行耗时
		//执行开始时间
		long executeStartTime 	= System.currentTimeMillis();
		Object proceed 	= invocation.proceed();
		//执行结束时间
		long executeFinishTime 	= System.currentTimeMillis();
		//替换完成，打印的SQL语句
		String printSql = null;
		try {
			// Trace级别 ， 通过buildPrintSql方法拿到最终生成的SQL
			if(logger.isTraceEnabled()) {
				printSql = buildPrintSql(invocation);
			}
		} catch (Exception exception) {
			logger.error("Get SQL Exception", exception);
		} finally {
			// 拼接日志打印过程
			if(StringUtils.isNotBlank(printSql)) {
				logger.debug("Execute SQL Cost Time {}ms , SQL :\n {}", (executeFinishTime - executeStartTime), printSql);
			}else {
				logger.debug("Execute SQL Cost Time {}ms ", (executeFinishTime - executeStartTime));
			}
		}
		return proceed;
	}

	private static String buildPrintSql(Invocation invocation) {
		// 获取到BoundSql以及Configuration对象
		// BoundSql 对象存储了一条具体的 SQL 语句及其相关参数信息。
		// Configuration 对象保存了 MyBatis 框架运行时所有的配置信息
		MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
		if(statement.getId().endsWith("fetchCount")) {
			return "";
		}
		Object args = null;
		if (invocation.getArgs().length > 1) {
			args = invocation.getArgs()[1];
		}
		Configuration configuration = statement.getConfiguration();
		BoundSql boundSql = statement.getBoundSql(args);

		// 获取参数对象
		Object parameterObject = boundSql.getParameterObject();
		// 获取参数映射
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		// 获取到执行的SQL
		String originalSql = boundSql.getSql();
		String sql = "";
		if(StringUtils.isNotBlank(originalSql) && originalSql.indexOf(ConstMetaObject.SQL_PLACEHOLDER) > -1) {
			sql = originalSql;
			// SQL中多个空格使用一个空格代替
			sql = sql.replaceAll("[\\s]+", " ");
			logger.trace("Args {}",args);
			logger.trace("Original SQL \n {}",sql);
			if (!ObjectUtils.isEmpty(parameterMappings) && !ObjectUtils.isEmpty(parameterObject)) {
				// TypeHandlerRegistry 是 MyBatis 用来管理 TypeHandler 的注册器。TypeHandler 用于在 Java 类型和
				// JDBC 类型之间进行转换
				TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
				// 如果参数对象的类型有对应的 TypeHandler，则使用 TypeHandler 进行处理
				if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
					sql = sql.replaceFirst(ConstMetaObject.SQL_PLACEHOLDER_REGEX, Matcher.quoteReplacement(getParameterValue(parameterObject)));
				} else {
					// 否则，逐个处理参数映射
					for (ParameterMapping parameterMapping : parameterMappings) {
						// 获取参数的属性名
						String propertyName = parameterMapping.getProperty();
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						// 检查对象中是否存在该属性的 getter 方法，如果存在就取出来进行替换
						if (metaObject.hasGetter(propertyName)) {
							Object obj = metaObject.getValue(propertyName);
							sql = sql.replaceFirst(ConstMetaObject.SQL_PLACEHOLDER_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
							// 检查 BoundSql 对象中是否存在附加参数。附加参数可能是在动态 SQL 处理中生成的，有的话就进行替换
						} else if (boundSql.hasAdditionalParameter(propertyName)) {
							Object obj = boundSql.getAdditionalParameter(propertyName);
							sql = sql.replaceFirst(ConstMetaObject.SQL_PLACEHOLDER_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
						} else {
							// 如果都没有，说明SQL匹配不上，带上“缺失”方便找问题
							sql = sql.replaceFirst(ConstMetaObject.SQL_PLACEHOLDER_REGEX, "Missing Parameter");
						}
					}
				}
			}
		}
		return sql;
	}

	private static String getParameterValue(Object object) {
		String value = "";
		if (object instanceof String) {
			value = "'" + object.toString() + "'";
		} else if (object instanceof Date dateValue) {
			DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + format.format(dateValue) + "'";
		} else if (!ObjectUtils.isEmpty(object)) {
			value = object.toString();
		}
		return value;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
}
