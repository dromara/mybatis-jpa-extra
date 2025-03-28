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
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.LambdaQueryBuilder;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.dromara.mybatis.jpa.update.LambdaUpdateBuilder;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateBuilder;
import org.dromara.mybatis.jpa.update.UpdateWrapper;
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
		FieldMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = FieldMetadata.getFieldsMap(entity.getClass());
		
		SQL sql = new SQL()
			.UPDATE(TableMetadata.getTableName(entity.getClass()));
		
		FieldColumnMapper partitionKey = null;
		FieldColumnMapper idFieldColumnMapper = null;
		for(FieldColumnMapper fieldColumnMapper : listFields) {
			String columnName = fieldColumnMapper.getColumnName();
			String fieldName = fieldColumnMapper.getFieldName();
			String fieldType = fieldColumnMapper.getFieldType();
			Object fieldValue = BeanUtil.getValue(entity, fieldName);
			boolean isFieldValueNull = BeanUtil.isFieldBlank(fieldValue);
			
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
			if(isFieldValueNull && !fieldColumnMapper.isGenerated()) {
				//skip null field value
				if(logger.isTraceEnabled()) {
					logger.trace("Field {} , Type {} , Value is null , Skiped ",
						String.format(MapperMetadata.LOG_FORMAT, fieldName), String.format(MapperMetadata.LOG_FORMAT, fieldType));
				}
			}else {
				if(logger.isTraceEnabled()) {
					logger.trace("Field {} , Type {} , Value {}",
						String.format(MapperMetadata.LOG_FORMAT, fieldName), String.format(MapperMetadata.LOG_FORMAT, fieldType),fieldValue);
				}
				if(fieldColumnMapper.getColumnAnnotation().updatable()) {
					if(fieldColumnMapper.isGenerated() && fieldColumnMapper.getTemporalAnnotation() != null) {
						sql.SET(" %s =  '%s' ".formatted(columnName,DateConverter.convert(entity, fieldColumnMapper,true)));
					}else {
						sql.SET(" %s = #{%s} ".formatted(columnName,fieldName));
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
	
	public String updateByQuery(Class<?> entityClass,String setSql, Query query) {
		logger.trace("update By Query \n{}" , query);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(tableName)
				.SET(setSql).WHERE(QueryBuilder.build(query));
		
		logger.trace("update By Query  SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String updateByLambdaQuery(Class<?> entityClass,String setSql, LambdaQuery<T> lambdaQuery) {
		logger.trace("update By LambdaQuery \n{}" , lambdaQuery);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(tableName)
				.SET(setSql).WHERE(LambdaQueryBuilder.build(lambdaQuery));
		
		logger.trace("update By LambdaQuery  SQL \n{}" , sql);
		return sql.toString();
	}
	
	
	public String updateByUpdateWrapper(Class<?> entityClass, UpdateWrapper updateWrapper) {
		logger.trace("update By UpdateWrapper \n{}" , updateWrapper);
		FieldMetadata.buildColumnList(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(TableMetadata.getTableName(entityClass))
				.SET(UpdateBuilder.buildSetSql(updateWrapper))
				.WHERE(QueryBuilder.build(updateWrapper));
		
		logger.trace("update By UpdateWrapper  SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String updateByLambdaUpdateWrapper(Class<?> entityClass, LambdaUpdateWrapper<T> lambdaUpdateWrapper) {
		logger.trace("update By LambdaUpdateWrapper \n{}" , lambdaUpdateWrapper);
		FieldMetadata.buildColumnList(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(TableMetadata.getTableName(entityClass))
				.SET(LambdaUpdateBuilder.buildSetSql(lambdaUpdateWrapper))
				.WHERE(LambdaQueryBuilder.build(lambdaUpdateWrapper));
		
		logger.trace("update By LambdaUpdateWrapper  SQL \n{}" , sql);
		return sql.toString();
	}

}
