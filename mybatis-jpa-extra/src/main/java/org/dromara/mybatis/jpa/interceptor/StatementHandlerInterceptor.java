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
import org.dromara.mybatis.jpa.meta.FindByMapper;
import org.dromara.mybatis.jpa.meta.FindByMetadata;
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
			
			BoundSql boundSql = statement.getBoundSql();
			String sql = boundSql.getSql();
			
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
				return invocation.proceed();
			}
			
			MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
			FindByMetadata.build(mappedStatement.getId());
			FindByMapper findByMapper = FindByMetadata.getFindByMapperMap().get(mappedStatement.getId());
			if(findByMapper != null && findByMapper.isFindBy()) {
				findByMapper.parseEntityClass();
				FieldMetadata.buildColumnList(findByMapper.getEntityClass());
				List<FieldColumnMapper> entityFields = FieldMetadata.getFieldsMap().get(findByMapper.getEntityClass().getSimpleName());
				Query q = Query.builder();
				String removedFindByName = findByMapper.getRemovedFindByName();
				int argIndex = 0;
				for(FieldColumnMapper fcm: entityFields) {
					if(removedFindByName.startsWith(StringUtils.capitalize(fcm.getFieldName()))) {
						logger.trace("FieldName : {} , capitalize {}" , fcm.getFieldName(),StringUtils.capitalize(fcm.getFieldName()));
						if(removedFindByName.length() >= fcm.getFieldName().length()) {
							removedFindByName = removedFindByName.substring(fcm.getFieldName().length());
							String jpaKeyword = JpaFindByKeywords.startKeyword(removedFindByName);
							if(StringUtils.isNotBlank(jpaKeyword) ) {
								logger.trace("JPAKeyword : {} " , jpaKeyword);
								removedFindByName = removedFindByName.substring(jpaKeyword.length());
							}
						}

						if(parameterObject instanceof ParamMap) {
							Object parameterValue = ((ParamMap<?>)parameterObject).get("arg"+(argIndex++ ));
							q.eq(fcm.getColumnName(), parameterValue);
						}else {
							q.eq(fcm.getColumnName(), parameterObject);
						}
						
						if(removedFindByName.length() <= fcm.getFieldName().length() || StringUtils.isBlank(removedFindByName)) {
							break;
						}
					}
				}
				
				SQL selectSql = TableMetadata.buildSelect(findByMapper.getEntityClass(),findByMapper.isDistinct()).WHERE(QueryBuilder.build(q));
				logger.trace("selectSql : {}" , selectSql);
				metaObject.setValue("boundSql.sql", selectSql.toString());
			}
			return invocation.proceed();
		}
		
		return invocation.proceed();
	}	
}
