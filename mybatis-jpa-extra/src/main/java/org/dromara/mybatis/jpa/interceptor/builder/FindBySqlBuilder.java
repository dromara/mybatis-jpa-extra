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
import org.dromara.mybatis.jpa.meta.findby.FindByKeywords;
import org.dromara.mybatis.jpa.meta.findby.FindByKeywords.KEY;
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
				String findByKeyword = "";
				if(removedFindByName.length() >= fcm.getFieldName().length()) {
					removedFindByName = removedFindByName.substring(fcm.getFieldName().length());
					findByKeyword = FindByKeywords.startKeyword(removedFindByName);
					if(StringUtils.isNotBlank(findByKeyword) ) {
						logger.trace("FindBy Keyword : {} " , findByKeyword);
						removedFindByName = removedFindByName.substring(findByKeyword.length());
					}
				}

				if(KEY.Between.equals(findByKeyword)) {
					appendParameter(q,findByKeyword,fcm.getColumnName(),((ParamMap<?>)parameterObject).get("arg0"),((ParamMap<?>)parameterObject).get("arg1"));
					break;
				}else if(parameterObject instanceof ParamMap<?> paramMap) {
					Object parameterValue = paramMap.get("arg"+(argIndex++ ));
					logger.trace("FindBy getCanonicalName : {} " , parameterValue.getClass().getCanonicalName());
					appendParameter(q,findByKeyword,fcm.getColumnName(),parameterValue,null);
				}else {
					appendParameter(q,findByKeyword,fcm.getColumnName(),parameterObject,null);
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
	
	protected static void  appendParameter(Query q,String operator,String columnName, Object value, Object value1) {
		if(KEY.And.equals(operator)) {
			q.and();
		}else if(KEY.Or.equals(operator)) {
			q.or();
		}else if(KEY.Is.equals(operator) || KEY.Equals.equals(operator)) {
			q.eq(columnName, value);
		}else if(KEY.Between.equals(operator)) {
			q.between(columnName, value, value1);
		}else if(KEY.LessThan.equals(operator) || KEY.Before.equals(operator)) {
			q.lt(columnName, value);
		}else if(KEY.LessThanEqual.equals(operator)) {
			q.le(columnName, value);
		}else if(KEY.GreaterThan.equals(operator) || KEY.After.equals(operator)) {
			q.gt(columnName, value);
		}else if(KEY.GreaterThanEqual.equals(operator)) {
			q.ge(columnName, value);
		}else if(KEY.IsNull.equals(operator) || KEY.Null.equals(operator)) {
			q.isNull(columnName);
		}else if(KEY.IsNotNull.equals(operator) || KEY.NotNull.equals(operator)) {
			q.isNotNull(columnName);
		}else if(KEY.Like.equals(operator)) {
			q.like(columnName, value);
		}else if(KEY.NotLike.equals(operator)) {
			q.notLike(columnName, value);
		}else if(KEY.StartingWith.equals(operator)) {
			q.likeLeft(columnName, value);
		}else if(KEY.EndingWith.equals(operator)) {
			q.likeRight(columnName, value);
		}else if(KEY.Containing.equals(operator)) {
			q.eq(columnName, value);
		}else if(KEY.OrderBy.equals(operator)) {
			q.orderBy(columnName,"desc");
		}else if(KEY.Not.equals(operator)) {
			q.notEq(columnName, value);
		}else if(KEY.In.equals(operator)) {
			q.in(columnName, value);
		}else if(KEY.NotIn.equals(operator)) {
			q.notIn(columnName, value);
		}else if(KEY.True.equals(operator)) {
			q.eq(columnName, true);
		}else if(KEY.False.equals(operator)) {
			q.eq(columnName, false);
		}else if(KEY.IgnoreCase.equals(operator)) {
			q.ignoreCase(columnName, value);
		}
	}
}
