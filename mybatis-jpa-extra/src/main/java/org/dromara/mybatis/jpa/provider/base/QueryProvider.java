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

package org.dromara.mybatis.jpa.provider.base;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.builder.ConditionBuilder;
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
		
		ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
		
		//查询语句
		String querySql = QueryBuilder.build(query);
		if(StringUtils.isNotBlank(querySql)) {
			sql.WHERE("( " + querySql +" ) ");
		}
		
		//逻辑删除
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && query.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumn(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
		if (query.getGroupBy() != null) {
			sql.GROUP_BY(ConditionBuilder.buildGroupBy(query.getGroupBy()));
		}
		if (query.getOrderBy() != null) {
			sql.ORDER_BY(ConditionBuilder.buildOrderBy(query.getOrderBy()));
		}
		logger.trace("filter By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String queryByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
		logger.trace("LambdaQuery \n{}" , lambdaQuery);
		
		SQL sql = TableMetadata.buildSelect(entityClass);
		
		ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
		
		StringBuilder whereSql = new StringBuilder("");
		//查询语句
		String querySql = LambdaQueryBuilder.build(lambdaQuery);
		if(StringUtils.isNotBlank(querySql)) {
			sql.WHERE("( " + querySql +" ) ");
		}
		//逻辑删除
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && lambdaQuery.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumn(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
		if(StringUtils.isNotBlank(whereSql)) {
			sql.WHERE(whereSql.toString());
		}
		
		if (CollectionUtils.isNotEmpty(lambdaQuery.getGroupBy())) {
			sql.GROUP_BY(ConditionBuilder.buildGroupBy(lambdaQuery.getGroupBy()));
		}
		if (CollectionUtils.isNotEmpty(lambdaQuery.getOrderBy())) {
			sql.ORDER_BY(ConditionBuilder.buildOrderBy(lambdaQuery.getOrderBy()));
		}
		logger.trace("filter By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String query(T entity) {
		SQL sql = TableMetadata.buildSelect(entity.getClass());

		for (ColumnMapper fieldColumnMapper : ColumnMetadata.buildColumnMapper(entity.getClass())) {
			Object fieldValue = BeanUtil.get(entity, fieldColumnMapper.getField());
			String fieldType = fieldColumnMapper.getFieldType().toLowerCase();

			logger.trace("ColumnName {} , FieldType {} , value {}", fieldColumnMapper.getColumn(), fieldType,
					fieldValue);
			if(fieldColumnMapper.isLogicDelete()) {
			    sql.WHERE(fieldColumnMapper.getColumn() + " = '" + fieldColumnMapper.getSoftDelete().value() + "'");
			} else {
    			if(fieldValue == null ) {
    				logger.trace("skip  {} ({}) is null ",fieldColumnMapper.getField(),fieldColumnMapper.getColumn());
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
    			    sql.WHERE(fieldColumnMapper.getColumn() + " = #{" + fieldColumnMapper.getField() + "}");
    			}
			}
		}
		logger.trace("filter By Entity SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String countByQuery(Class<?> entityClass, Query query) {
		logger.trace("count Query \n{}" , query);
		SQL sql = TableMetadata.buildSelectCount(entityClass);
		
		ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
		StringBuilder whereSql = new StringBuilder("");
		//查询语句
		String querySql = QueryBuilder.build(query);
		if(StringUtils.isNotBlank(querySql)) {
			sql.WHERE("( " + querySql +" ) ");
		}
		
		//逻辑删除
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && query.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumn(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
		if(StringUtils.isNotBlank(whereSql)) {
			sql.WHERE(whereSql.toString());
		}
		
		logger.trace("count By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String countByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
		logger.trace("count LambdaQuery \n{}" , lambdaQuery);
		
		SQL sql = TableMetadata.buildSelectCount(entityClass);
		
		ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
		StringBuilder whereSql = new StringBuilder("");
		//查询语句
		String querySql = LambdaQueryBuilder.build(lambdaQuery);
		if(StringUtils.isNotBlank(querySql)) {
			sql.WHERE("( " + querySql +" ) ");
		}
		//逻辑删除
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && lambdaQuery.isSoftDelete()) {
			sql.WHERE(" ( %s = '%s' )" 
					.formatted(
							logicColumnMapper.getColumn(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
		if(StringUtils.isNotBlank(whereSql)) {
			sql.WHERE(whereSql.toString());
		}

		logger.trace("count By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
}
