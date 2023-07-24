/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResultsSqlCache;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.provider.PageResultsCountProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts( {
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class ,Integer.class })})
public class StatementHandlerInterceptor extends AbstractStatementHandlerInterceptor implements Interceptor {
	protected static Logger logger = LoggerFactory.getLogger(StatementHandlerInterceptor.class);
	
	public Object intercept(Invocation invocation) throws Throwable {
		Method m = invocation.getMethod();

		if ("prepare".equals(m.getName())) {
			return prepare(invocation);
		}
		
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}
	

	private Object prepare(Invocation invocation) throws Throwable {
		StatementHandler statement = getStatementHandler(invocation);
		if (statement instanceof SimpleStatementHandler || statement instanceof PreparedStatementHandler) {
			MetaObject metaObject = SystemMetaObject.forObject(statement);
			Object parameterObject = metaObject.getValue("parameterHandler.parameterObject");
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			logger.trace("parameter class {} object  ==> {}" , parameterObject.getClass().getCanonicalName(),parameterObject);
			//判断是否select语句及需要分页支持
			if (sql.toLowerCase().trim().startsWith("select")) {
				JpaPage page = null;
				if((parameterObject instanceof JpaPage)) {
					page = (JpaPage)parameterObject;
				}else if((parameterObject instanceof ParamMap)
						&& ((ParamMap<?>)parameterObject).containsKey(MapperMetadata.PAGE)) {
					page = (JpaPage)((ParamMap<?>)parameterObject).get(MapperMetadata.PAGE);
				}else {
					try {
						for (Object key : ((ParamMap<?>)parameterObject).entrySet()){
							if(((ParamMap<?>)parameterObject).get(key) instanceof JpaPage) {
								page = (JpaPage) ((ParamMap<?>)parameterObject).get(key);
								break;
							}
						}
					}catch(Exception e) {}
				}
				//分页标识
				if(page != null && page.isPageable()){
					logger.trace("prepare  boundSql  ==> {}" , removeBreakingWhitespace(sql));
					if(statement instanceof SimpleStatementHandler){
						sql = dialect.getLimitString(sql, page);
					}else if(statement instanceof PreparedStatementHandler){
						PageResultsCountProvider.pageResultsBoundSqlCache.put(
								page.getPageResultSelectUUID(), 
								new JpaPageResultsSqlCache(sql,boundSql)
								);
						sql = dialect.getLimitString(sql, page);
					}
					logger.trace("prepare dialect boundSql : {}" , removeBreakingWhitespace(sql));
					metaObject.setValue("boundSql.sql", sql);
				}
			}
		}
		return invocation.proceed();
	}	
}
