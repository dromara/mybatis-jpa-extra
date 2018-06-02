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
import org.apache.mybatis.jpa.domain.Pagination;
import org.apache.mybatis.jpa.service.JpaBaseService;


@Intercepts( {
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class ,Integer.class })})
public class StatementHandlerInterceptor extends AbstractStatementHandlerInterceptor implements Interceptor {

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
			MetaObject metaObject=SystemMetaObject.forObject(statement);
			Object parameterObject=metaObject.getValue("parameterHandler.parameterObject");
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			//log.debug("prepare  boundSql  ==> "+removeBreakingWhitespace(sql));
			//log.debug("prepare  boundSql  ==> "+parameterObject);
			if ((parameterObject instanceof Pagination)
					&& (sql.toUpperCase().trim().startsWith("SELECT")) ) {
				Pagination pagination=(Pagination)parameterObject;
				if(pagination.isPageable()){
					//log.debug("startsWith SELECT : "+sql.toUpperCase().trim().startsWith("SELECT"));
					_logger.debug("prepare  boundSql  ==> "+removeBreakingWhitespace(sql));
					if(statement instanceof SimpleStatementHandler){
						sql = dialect.getLimitString(sql, pagination);
					}else if(statement instanceof PreparedStatementHandler){
						JpaBaseService.pageResultsBoundSqlCache.put(pagination.getPageResultSelectUUID(), new PageResultsSqlCache(sql,boundSql));
						sql = dialect.getLimitString(sql, pagination);
					}
					_logger.debug("prepare dialect boundSql : "+removeBreakingWhitespace(sql));
					metaObject.setValue("boundSql.sql", sql);
				}
				
			}
			
		}
		return invocation.proceed();
	}	
}