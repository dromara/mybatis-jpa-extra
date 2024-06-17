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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.provider;

import java.util.List;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.meta.FieldColumnMapper;
import org.dromara.mybatis.jpa.meta.MapperMetadata;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class UpdateProvider <T extends JpaEntity>{
	static final Logger logger 	= 	LoggerFactory.getLogger(UpdateProvider.class);

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = MapperMetadata.getFieldsMap().get(entity.getClass().getSimpleName());
		
		SQL sql = new SQL()
			.UPDATE(MapperMetadata.getTableName(entity.getClass()));
		
		FieldColumnMapper partitionKey = null;
		FieldColumnMapper idFieldColumnMapper = null;
		for(FieldColumnMapper fieldColumnMapper : listFields) {
			logger.trace("Field {} , Type {}",
							fieldColumnMapper.getFieldName(), fieldColumnMapper.getFieldType());
			if (fieldColumnMapper.isIdColumn() ) {
				idFieldColumnMapper = fieldColumnMapper;
				continue;
			}
			if(fieldColumnMapper.getPartitionKey() != null) {
				partitionKey = fieldColumnMapper;
				continue;
			}
			if(fieldColumnMapper.isLogicDelete()) {
				continue;
			}
			if(
				(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
						||fieldColumnMapper.getFieldType().startsWith("byte")
						||BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == null
				)
				&& BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())== null
				&& !fieldColumnMapper.isGenerated()) {
				//skip null field value
				logger.trace("skip {}({}) is null ",fieldColumnMapper.getFieldName(),fieldColumnMapper.getColumnName());
			}else {
				if(fieldColumnMapper.getColumnAnnotation().updatable()) {
					if(fieldColumnMapper.isGenerated() && fieldColumnMapper.getTemporalAnnotation() != null) {
						sql.SET(" %s =  '%s' ".formatted(fieldColumnMapper.getColumnName(),DateConverter.convert(entity, fieldColumnMapper,true)));
					}else {
						sql.SET(" %s = #{%s} ".formatted(fieldColumnMapper.getColumnName(),fieldColumnMapper.getFieldName()));
					}
				}
			}
		}
		if(idFieldColumnMapper != null) {
			if(partitionKey != null) {
				sql.WHERE("""
						%s = #{%s}
						and %s = #{%s}
						""".formatted(
								partitionKey.getColumnName(),
								partitionKey.getFieldName(),
								idFieldColumnMapper.getColumnName(),
								idFieldColumnMapper.getFieldName())
						);
			}else {
				sql.WHERE("%s = #{%s}" .formatted(idFieldColumnMapper.getColumnName(),idFieldColumnMapper.getFieldName()));
			}
			logger.trace("Update SQL : \n{}" , sql);
			return sql.toString();
		}else {
			return "";
		}
	}
	
	public String updateByCondition(Class<?> entityClass,String setSql, Query query) {
		logger.trace("update By Query \n{}" , query);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(tableName)
				.SET(setSql).WHERE(QueryBuilder.build(query));
		
		logger.trace("update By Query  SQL \n{}" , sql);
		return sql.toString();
	}

}
