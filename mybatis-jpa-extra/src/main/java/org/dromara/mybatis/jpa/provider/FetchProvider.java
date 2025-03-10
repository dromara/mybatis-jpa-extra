/*
 * Copyright [2023] [MaxKey of copyright http://www.maxkey.top]
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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.provider;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.LambdaQueryBuilder;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */

@SuppressWarnings("unchecked")
public class FetchProvider <T extends JpaEntity>{	
	static final Logger logger 	= 	LoggerFactory.getLogger(FetchProvider.class);
	
	/**
	 * @param entity
	 * @return fetch sql String
	 */
	public String fetch(Map<String, Object>  parametersMap) {
		T entity = (T)parametersMap.get(MapperMetadata.ENTITY);
		FieldMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = FieldMetadata.getFieldsMap(entity.getClass());
		String[] column = new String[listFields.size()] ;
		StringBuffer conditions = new StringBuffer();
		int columnCount = 0;
		for(FieldColumnMapper fieldColumnMapper : listFields) {
			String columnName = fieldColumnMapper.getColumnName();
			String fieldName = fieldColumnMapper.getFieldName();
			String fieldType = fieldColumnMapper.getFieldType();
			Object fieldValue = BeanUtil.getValue(entity, fieldName);
			column[columnCount++] = columnName;
			boolean isFieldValueNull = BeanUtil.isFieldBlank(fieldValue);
			
			if(isFieldValueNull || fieldType.startsWith("byte")) {
				//skip null field value
				if(logger.isTraceEnabled()) {
					logger.trace("Field {} , Type {} , Value is null , Skiped ",
						String.format(MapperMetadata.LOG_FORMAT, fieldName),String.format(MapperMetadata.LOG_FORMAT, fieldType));
				}
			}else {
				if(logger.isTraceEnabled()) {
					logger.trace("Field {} , Type {} , Value {}",
						String.format(MapperMetadata.LOG_FORMAT, fieldName), String.format(MapperMetadata.LOG_FORMAT, fieldType),fieldValue);
				}
				if(!conditions.isEmpty()) {
					conditions.append(" and ");
				}
				if(fieldColumnMapper.isLogicDelete()) {
					conditions.append(
							" %s = '%s' ".formatted(
									columnName,
									fieldColumnMapper.getSoftDelete().value()));
				}else {
					conditions.append(
							" %s = #{%s.%s} ".formatted(
									columnName,
									MapperMetadata.ENTITY,
									fieldName));
				}
			}
		}
		
		SQL sql = new SQL()
				.SELECT(column)
				.FROM(TableMetadata.getTableName(entity.getClass()))
				.WHERE(conditions.toString());
		
		logger.trace("Query Page SQL : \n{}" , sql);
		return sql.toString();
	}
	

	/**
	 * @param entity
	 * @return fetch sql String
	 */
	public String fetchByQuery(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		Query condition = (Query)parametersMap.get(MapperMetadata.CONDITION);
		FieldMetadata.buildColumnList(entityClass);
		List<FieldColumnMapper> listFields = FieldMetadata.getFieldsMap(entityClass);
		String[] column = new String[listFields.size()] ;
		for(int i = 0 ; i< listFields.size() ; i++) {
			column[i] = listFields.get(i).getColumnName();
		}
		SQL sql = new SQL().SELECT(column).FROM(TableMetadata.getTableName(entityClass));
		
		String conditionString = QueryBuilder.build(condition);
		if(StringUtils.isNotBlank(conditionString)) {
			sql.WHERE("( " + conditionString +" ) ");
		}
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && condition.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		logger.trace("query Page By Query SQL : \n{}" , sql);
		return sql.toString();
	}
	
	/**
	 * @param entity
	 * @return fetch sql String
	 */
	public String fetchByLambdaQuery(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		LambdaQuery<T> condition = (LambdaQuery<T>)parametersMap.get(MapperMetadata.CONDITION);
		FieldMetadata.buildColumnList(entityClass);
		List<FieldColumnMapper> listFields = FieldMetadata.getFieldsMap(entityClass);
		String[] column = new String[listFields.size()] ;
		for(int i = 0 ; i< listFields.size() ; i++) {
			column[i] = listFields.get(i).getColumnName();
		}
		SQL sql = new SQL().SELECT(column).FROM(TableMetadata.getTableName(entityClass));
		
		String conditionString = LambdaQueryBuilder.build(condition);
		if(StringUtils.isNotBlank(conditionString)) {
			sql.WHERE("( " + conditionString +" ) ");
		}
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && condition.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		logger.trace("query Page By LambdaQuery SQL : \n{}" , sql);
		return sql.toString();
	}
	
}
