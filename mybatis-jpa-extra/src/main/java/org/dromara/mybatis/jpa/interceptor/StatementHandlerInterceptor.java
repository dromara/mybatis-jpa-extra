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
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageSqlCache;
import org.dromara.mybatis.jpa.meta.FieldColumnMapper;
import org.dromara.mybatis.jpa.meta.FieldMetadata;
import org.dromara.mybatis.jpa.meta.MapperMetadata;
import org.dromara.mybatis.jpa.meta.TableMetadata;
import org.dromara.mybatis.jpa.provider.FetchCountProvider;
import org.dromara.mybatis.jpa.query.JpaFindByKeywords;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts( {
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class ,Integer.class })})
public class StatementHandlerInterceptor extends AbstractStatementHandlerInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(StatementHandlerInterceptor.class);
	
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
			MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			String mappedStatementId = mappedStatement.getId();
			String mappedStatementClassName = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
			String mappedStatementMethodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);
			logger.trace("mappedStatementClass {}" ,mappedStatementClassName);
			logger.trace("methodName {}" , mappedStatementMethodName);
			logger.trace("parameter {}({})" , parameterObject,parameterObject.getClass().getCanonicalName());
			//判断是否select语句及需要分页支持
			if (sql.toLowerCase().trim().startsWith("select")) {
				JpaPage page = null;
				if((parameterObject instanceof JpaPage parameterObjectPage)) {
					page = parameterObjectPage;
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
					String boundSqlRemoveBreakingWhitespace = removeBreakingWhitespace(sql);
					logger.trace("prepare  boundSql  ==> {}" , boundSqlRemoveBreakingWhitespace);
					if(statement instanceof SimpleStatementHandler){
						sql = dialect.getLimitString(sql, page);
					}else if(statement instanceof PreparedStatementHandler){
						FetchCountProvider.PAGE_BOUNDSQL_CACHE.put(
								page.getPageSelectId(), 
								new JpaPageSqlCache(sql,boundSql)
								);
						sql = dialect.getLimitString(sql, page);
					}
					logger.trace("prepare dialect boundSql : {}" , boundSqlRemoveBreakingWhitespace);
					metaObject.setValue("boundSql.sql", sql);
				}
			}else if(mappedStatementMethodName.startsWith(JpaFindByKeywords.FINDBY) 
						|| mappedStatementMethodName.startsWith(JpaFindByKeywords.FINDDISTINCTBY)) {
				boolean isDistinct = false;
				String removedFindByName = "";
				if(mappedStatementMethodName.startsWith(JpaFindByKeywords.FINDBY)) {
					removedFindByName = mappedStatementMethodName.substring(JpaFindByKeywords.FINDBY.length());
				}else {
					isDistinct = true;
					removedFindByName = mappedStatementMethodName.substring(JpaFindByKeywords.FINDDISTINCTBY.length());
				}
				
				logger.trace("removed FindBy name : {}" , removedFindByName);
				
				Class<?> mappedStatementClass  = Class.forName(mappedStatementClassName);
				Type[] pType = mappedStatementClass.getGenericInterfaces();
				
				if (pType != null && pType.length >= 1) {
					ParameterizedType parameterizedType = (ParameterizedType)pType[0];
					if(parameterizedType != null && parameterizedType.getActualTypeArguments().length > 0) {
						Class<?> entityClass = (Class<?>)parameterizedType.getActualTypeArguments()[0];
						logger.trace("Entity Class : {}" , entityClass.getCanonicalName());
						FieldMetadata.buildColumnList(entityClass);
						List<FieldColumnMapper> entityFields = FieldMetadata.getFieldsMap().get(entityClass.getSimpleName());
						Query q = Query.builder();
						int argIndex = 0;
						for(FieldColumnMapper fcm: entityFields) {
							if(removedFindByName.startsWith(StringUtils.capitalize(fcm.getFieldName()))) {
								logger.trace("FieldName : {} , capitalize {}" , fcm.getFieldName(),StringUtils.capitalize(fcm.getFieldName()));
								removedFindByName = removedFindByName.substring(fcm.getFieldName().length());
								String jpaKeyword = JpaFindByKeywords.startKeyword(removedFindByName);
								if(StringUtils.isNotBlank(jpaKeyword) ) {
									logger.trace("jpaKeyword : {} " , jpaKeyword);
									removedFindByName = removedFindByName.substring(jpaKeyword.length());
								}
								q.eq(fcm.getColumnName(), ((ParamMap<?>)parameterObject).get("arg"+(argIndex++ )));
								
								if(StringUtils.isBlank(removedFindByName)) {
									break;
								}
							}
						}
						
						
						SQL selectSql = TableMetadata.buildSelect(entityClass,isDistinct).WHERE(QueryBuilder.build(q));
						logger.trace("selectSql : {}" , selectSql);
						
						Method[] mappedStatementMethods = mappedStatementClass.getDeclaredMethods();
						for(Method mappedStatementMethod : mappedStatementMethods) {
							if(mappedStatementMethodName.endsWith(mappedStatementMethod.getName())) {
								logger.trace("DeclaredMethod : {}" , mappedStatementMethod.getName());
								Parameter[]  parameters = mappedStatementMethod.getParameters();
								for(Parameter parameter : parameters) {
									logger.trace("Parameter name : {} , type {} , value {}" , parameter.getName(),parameter.getType(),((ParamMap<?>)parameterObject).get(parameter.getName()));
								}
							}
						}
						
						logger.trace("selectSql : {}" , selectSql);
						metaObject.setValue("boundSql.sql", selectSql.toString());
					}
				}
				logger.trace("methodName {}" , mappedStatementMethodName);
			}
		}
		return invocation.proceed();
	}	
}
