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
public class QueryProvider<T extends JpaEntity> {

	private static final Logger logger = LoggerFactory.getLogger(QueryProvider.class);

	public String queryByCondition(T entity, Query query) {
		logger.trace("Query \n{}" , query);
		SQL sql = MapperMetadata.buildSelect(entity.getClass()).WHERE(QueryBuilder.build(query));
		
		if (query.getGroupBy() != null) {
			sql.GROUP_BY(QueryBuilder.buildGroupBy(query));
		}
		if (query.getOrderBy() != null) {
			sql.ORDER_BY(QueryBuilder.buildOrderBy(query));
		}
		logger.trace("filter By Query SQL \n{}" , sql.toString());
		return sql.toString();
	}



	public String query(T entity) {
		SQL sql = MapperMetadata.buildSelect(entity.getClass());

		for (FieldColumnMapper fieldColumnMapper : MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName())) {
			Object fieldValue = BeanUtil.get(entity, fieldColumnMapper.getFieldName());
			String fieldType = fieldColumnMapper.getFieldType().toLowerCase();

			logger.trace("ColumnName {} , FieldType {} , value {}", fieldColumnMapper.getColumnName(), fieldType,
					fieldValue);

			if(fieldValue == null ) {
				logger.trace("skip  {} ({}) is null ",fieldColumnMapper.getFieldName(),fieldColumnMapper.getColumnName());
				// skip null field value
			} else if((fieldType.equals("string") && fieldValue.equals(""))
					|| (fieldType.startsWith("byte"))
					|| (fieldType.equals("Int") && fieldValue.equals("0"))
					|| (fieldType.equals("Long")&& fieldValue.equals("0"))
					|| (fieldType.equals("Integer")&& fieldValue.equals("0"))
					|| (fieldType.equals("Float")&& fieldValue.equals("0.0"))
					|| (fieldType.equals("Double")&& fieldValue.equals("0.0"))){
				
			}else {
				sql.WHERE(fieldColumnMapper.getColumnName() + " = #{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		logger.trace("filter By Entity SQL \n{}" , sql.toString());
		return sql.toString();
	}

}
