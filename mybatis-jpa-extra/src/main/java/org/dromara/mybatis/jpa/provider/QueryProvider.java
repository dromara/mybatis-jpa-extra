/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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

package org.dromara.mybatis.jpa.provider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.builder.LambdaQueryBuilder;
import org.dromara.mybatis.jpa.query.builder.QueryBuilder;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class QueryProvider<T extends JpaEntity> {
	static final Logger logger = LoggerFactory.getLogger(QueryProvider.class);

	public String queryByQuery(Class<?> entityClass, Query query) {
		logger.trace("Query \n{}" , query);
		SQL sql = TableMetadata.buildSelect(entityClass);
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && query.isSoftDelete()) {
			sql.WHERE("( %s ) and %s = '%s'"
					.formatted(
							QueryBuilder.build(query),
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}else {
			sql.WHERE(QueryBuilder.build(query));
		}
		
		if (query.getGroupBy() != null) {
			sql.GROUP_BY(QueryBuilder.buildGroupBy(query));
		}
		if (query.getOrderBy() != null) {
			sql.ORDER_BY(QueryBuilder.buildOrderBy(query));
		}
		logger.trace("filter By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String queryByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
		logger.trace("LambdaQuery \n{}" , lambdaQuery);
		
		
		SQL sql = TableMetadata.buildSelect(entityClass);
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && lambdaQuery.isSoftDelete()) {
			sql.WHERE("( %s ) and %s = '%s'"
					.formatted(
							LambdaQueryBuilder.build(lambdaQuery),
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}else {
			sql.WHERE(LambdaQueryBuilder.build(lambdaQuery));
		}
		
		if (CollectionUtils.isNotEmpty(lambdaQuery.getGroupBy())) {
			sql.GROUP_BY(LambdaQueryBuilder.buildGroupBy(lambdaQuery));
		}
		if (CollectionUtils.isNotEmpty(lambdaQuery.getOrderBy())) {
			sql.ORDER_BY(LambdaQueryBuilder.buildOrderBy(lambdaQuery));
		}
		logger.trace("filter By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String query(T entity) {
		SQL sql = TableMetadata.buildSelect(entity.getClass());

		for (FieldColumnMapper fieldColumnMapper : FieldMetadata.buildColumnMapper(entity.getClass())) {
			Object fieldValue = BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			String fieldType = fieldColumnMapper.getFieldType().toLowerCase();

			logger.trace("ColumnName {} , FieldType {} , value {}", fieldColumnMapper.getColumnName(), fieldType,
					fieldValue);

			if(fieldValue == null ) {
				logger.trace("skip  {} ({}) is null ",fieldColumnMapper.getFieldName(),fieldColumnMapper.getColumnName());
				// skip null field value
			} else if(("string".equals(fieldType) && "".equals(fieldValue))
					|| ("byte".startsWith(fieldType))
					|| ("Int".equals(fieldType) && "0".equals(fieldValue))
					|| ("Long".equals(fieldType)&& "0".equals(fieldValue))
					|| ("Integer".equals(fieldType)&& "0".equals(fieldValue))
					|| ("Float".equals(fieldType)&& "0.0".equals(fieldValue))
					|| ("Double".equals(fieldType)&& "0.0".equals(fieldValue))){
				// skip default field value
			}else {
				if(fieldColumnMapper.isLogicDelete()) {
					sql.WHERE(fieldColumnMapper.getColumnName() + " = '" + fieldColumnMapper.getSoftDelete().value() + "'");
				}else {
					sql.WHERE(fieldColumnMapper.getColumnName() + " = #{" + fieldColumnMapper.getFieldName() + "}");
				}
			}
		}
		logger.trace("filter By Entity SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String countByQuery(Class<?> entityClass, Query query) {
		logger.trace("count Query \n{}" , query);
		SQL sql = TableMetadata.buildSelectCount(entityClass);
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && query.isSoftDelete()) {
			sql.WHERE("( %s ) and %s = '%s'"
					.formatted(
							QueryBuilder.build(query),
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}else {
			sql.WHERE(QueryBuilder.build(query));
		}
		
		logger.trace("count By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String countByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
		logger.trace("count LambdaQuery \n{}" , lambdaQuery);
		
		SQL sql = TableMetadata.buildSelectCount(entityClass);
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && lambdaQuery.isSoftDelete()) {
			sql.WHERE("( %s ) and %s = '%s'"
					.formatted(
							LambdaQueryBuilder.build(lambdaQuery),
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}else {
			sql.WHERE(LambdaQueryBuilder.build(lambdaQuery));
		}

		logger.trace("count By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
}
