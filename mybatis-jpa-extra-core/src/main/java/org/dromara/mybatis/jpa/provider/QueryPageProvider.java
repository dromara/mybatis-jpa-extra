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

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class QueryPageProvider <T extends JpaEntity>{
	
	private static final Logger logger 	= 	LoggerFactory.getLogger(QueryPageProvider.class);

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String queryPage(Map<String, Object>  parametersMap) {
		@SuppressWarnings("unchecked")
		T entity = (T)parametersMap.get(MapperMetadata.ENTITY);
		MapperMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName());
		String[] column = new String[listFields.size()] ;
		for(int i = 0 ; i< listFields.size() ; i++) {
			column[i] = listFields.get(i).getColumnName();
		}
		SQL sql = new SQL()
			.SELECT(column).FROM(MapperMetadata.getTableName(entity.getClass()));
		StringBuffer conditions = new StringBuffer();
		for(FieldColumnMapper fieldColumnMapper : listFields) {
			logger.trace("Field {} , Type {} , Value {}",
							fieldColumnMapper.getFieldName(), 
							fieldColumnMapper.getFieldType(),
							BeanUtil.get(entity, fieldColumnMapper.getFieldName()));
			if(
				(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
						||fieldColumnMapper.getFieldType().startsWith("byte")
						||fieldColumnMapper.getFieldType().startsWith("int")
						||BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == null
				)
				&& BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())== null) {
				//skip null field value
				logger.trace("skip {}({}) is null ",fieldColumnMapper.getFieldName(),fieldColumnMapper.getColumnName());
			}else {
				if(!conditions.isEmpty()) {
					conditions.append(" and ");
				}
				conditions.append(
						" %s = #{%s.%s} ".formatted(
								fieldColumnMapper.getColumnName(),
								MapperMetadata.ENTITY,
								fieldColumnMapper.getFieldName()));
			}
		}
		
		sql.WHERE(conditions.toString());
			
		logger.trace("Query Page SQL : \n{}" , sql);
		return sql.toString();
	}
	

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String queryPageByCondition(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		Query condition = (Query)parametersMap.get(MapperMetadata.CONDITION);
		MapperMetadata.buildColumnList(entityClass);
		List<FieldColumnMapper> listFields = MapperMetadata.fieldsMap.get(entityClass.getSimpleName());
		String[] column = new String[listFields.size()] ;
		for(int i = 0 ; i< listFields.size() ; i++) {
			column[i] = listFields.get(i).getColumnName();
		}
		SQL sql = new SQL()
			.SELECT(column).FROM(MapperMetadata.getTableName(entityClass))
			.WHERE(QueryBuilder.build(condition));
		
		logger.trace("query Page By Condition SQL : \n{}" , sql);
		return sql.toString();
	}

}
