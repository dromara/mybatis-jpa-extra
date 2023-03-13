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
 

package org.apache.mybatis.jpa;

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
import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.apache.mybatis.jpa.persistence.JpaPagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts( {
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class ,Integer.class })})
public class StatementHandlerInterceptor extends AbstractStatementHandlerInterceptor implements Interceptor {
	protected Logger _logger = LoggerFactory.getLogger(StatementHandlerInterceptor.class);
	
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
		if (statement instanceof SimpleStatementHandler 
				|| statement instanceof PreparedStatementHandler) {
			MetaObject metaObject=SystemMetaObject.forObject(statement);
			Object parameterObject=metaObject.getValue("parameterHandler.parameterObject");
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			_logger.trace("parameter object  ==> {}" , parameterObject);
			//判断是否select语句及需要分页支持
			if ((parameterObject instanceof JpaPagination)
					&& (sql.toLowerCase().trim().startsWith("select")) ) {
				JpaPagination pagination=(JpaPagination)parameterObject;
				//分页标识
				if(pagination.isPageable()){
					_logger.trace("prepare  boundSql  ==> {}" , removeBreakingWhitespace(sql));
					if(statement instanceof SimpleStatementHandler){
						sql = dialect.getLimitString(sql, pagination);
					}else if(statement instanceof PreparedStatementHandler){
						JpaBaseService.pageResultsBoundSqlCache.put(
								pagination.getPageResultSelectUUID(), 
								new PageResultsSqlCache(sql,boundSql)
								);
						sql = dialect.getLimitString(sql, pagination);
					}
					_logger.trace("prepare dialect boundSql : {}" , removeBreakingWhitespace(sql));
					metaObject.setValue("boundSql.sql", sql);
				}
				
			}
			
		}
		return invocation.proceed();
	}	
}
