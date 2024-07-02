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
 

package org.dromara.mybatis.jpa.interceptor.builder;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.meta.FieldColumnMapper;
import org.dromara.mybatis.jpa.meta.FieldMetadata;
import org.dromara.mybatis.jpa.meta.TableMetadata;
import org.dromara.mybatis.jpa.meta.findby.FindByMapper;
import org.dromara.mybatis.jpa.meta.findby.FindByMetadata;
import org.dromara.mybatis.jpa.query.JpaFindByKeywords;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindBySqlBuilder {
	private static final Logger logger 	= 	LoggerFactory.getLogger(FindBySqlBuilder.class);

	public static void parse(String mappedStatementId){
		if(!FindByMetadata.getFindByMapperMap().containsKey(mappedStatementId)){
			FindByMapper findByMapper = new FindByMapper(mappedStatementId);
			findByMapper.parseFindBy();
			if(findByMapper.isFindBy()) {
				logger.trace("mappedStatementId {}  ==> findByMapper {}" , mappedStatementId,findByMapper);
				FindByMetadata.getFindByMapperMap().put(mappedStatementId, findByMapper);
			}
		}
	}
	
	public static String translate(FindByMapper findByMapper,Object parameterObject) {
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
		return selectSql.toString();
	}
}
