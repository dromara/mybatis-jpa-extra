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
import org.dromara.mybatis.jpa.meta.FieldColumnMapper;
import org.dromara.mybatis.jpa.meta.FieldMetadata;
import org.dromara.mybatis.jpa.meta.MapperMetadata;
import org.dromara.mybatis.jpa.meta.TableMetadata;
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
		for(int i = 0 ; i< listFields.size() ; i++) {
			column[i] = listFields.get(i).getColumnName();
		}
		SQL sql = new SQL()
			.SELECT(column).FROM(TableMetadata.getTableName(entity.getClass()));
		StringBuffer conditions = new StringBuffer();
		
		for(FieldColumnMapper fieldColumnMapper : listFields) {
			Object fieldValue = BeanUtil.getValue(entity, fieldColumnMapper.getFieldName());
			String fieldType = fieldColumnMapper.getFieldType();
			logger.trace("Field {} , Type {} , Value {}",
							fieldColumnMapper.getFieldName(), 
							fieldColumnMapper.getFieldType(),
							fieldValue);
			if(fieldValue == null ||fieldValue.toString().equalsIgnoreCase("null")|| fieldType.startsWith("byte")) {
				//skip null field value
				logger.trace("skip {}({}) is null ",fieldColumnMapper.getFieldName(),fieldType);
			}else if(fieldType.equalsIgnoreCase("String")&&StringUtils.isBlank((String)fieldValue)){
				logger.trace("skip {}({}) is Blank ",fieldColumnMapper.getFieldName(),fieldType);
			}else {
				if(!conditions.isEmpty()) {
					conditions.append(" and ");
				}
				if(fieldColumnMapper.isLogicDelete()) {
					conditions.append(
							" %s = '%s' ".formatted(
									fieldColumnMapper.getColumnName(),
									fieldColumnMapper.getSoftDelete().value()));
				}else {
					conditions.append(
							" %s = #{%s.%s} ".formatted(
									fieldColumnMapper.getColumnName(),
									MapperMetadata.ENTITY,
									fieldColumnMapper.getFieldName()));
				}
			}
		}
		
		sql.WHERE(conditions.toString());
			
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
		SQL sql = new SQL()
			.SELECT(column).FROM(TableMetadata.getTableName(entityClass))
			.WHERE("( " + QueryBuilder.build(condition) +" ) ");
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
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
		SQL sql = new SQL()
			.SELECT(column).FROM(TableMetadata.getTableName(entityClass))
			.WHERE("( " + LambdaQueryBuilder.build(condition) +" ) ");
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
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
