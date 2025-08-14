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

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.LambdaQueryBuilder;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SoftDeleteProvider <T extends JpaEntity>{	
	static final Logger logger 	= 	LoggerFactory.getLogger(SoftDeleteProvider.class);

	
	@SuppressWarnings("unchecked")
	public String softDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		FieldMetadata.buildColumnMapper(entityClass);
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get(MapperMetadata.PARAMETER_ID_LIST);
		
		StringBuffer keyValue = new StringBuffer();
		for(String value : idValues) {
			if(StringUtils.isNotBlank(value)) {
				keyValue.append(",'").append(SafeValueHandler.valueOf(value)).append("'");
				logger.trace("softDelete by id {}" , value);
			}
		}
		// remove ;
		String keyValues = keyValue.substring(1).replace(";", "");
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		String partitionKeyValue = (String) parametersMap.get(MapperMetadata.PARAMETER_PARTITION_KEY);
		FieldColumnMapper partitionKeyColumnMapper = FieldMetadata.getPartitionKey(entityClass);
		FieldColumnMapper idFieldColumnMapper = FieldMetadata.getIdColumn(entityClass);
		
		SQL sql=new SQL()
				.UPDATE(TableMetadata.getTableName(entityClass))
				.SET(" %s = '%s' ".formatted(
						logicColumnMapper.getColumnName(),
						logicColumnMapper.getSoftDelete().delete()
					)
				);
		
		if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
			sql.WHERE("%s = #{%s} and %s  in ( %s )"
					.formatted(
							partitionKeyColumnMapper.getColumnName() ,
							partitionKeyValue,
							idFieldColumnMapper.getColumnName(),
							idFieldColumnMapper.getFieldName())
        			);  
		}else {
			sql.WHERE(" %s in ( %s )".formatted(idFieldColumnMapper.getColumnName(),keyValues));  
		}
		
        String deleteSql = sql.toString(); 
        logger.trace("softDelete SQL \n{}" , deleteSql);
        return deleteSql;  
    } 
	
	public String softDeleteByQuery(Class<?> entityClass, Query query) {
		logger.trace("softDelete By Query \n{}" , query);
		FieldMetadata.buildColumnMapper(entityClass);
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(TableMetadata.getTableName(entityClass))
				.SET(" %s = '%s' ".formatted(
						logicColumnMapper.getColumnName(),
						logicColumnMapper.getSoftDelete().delete()
					)
				).WHERE(QueryBuilder.build(query));
		
		logger.trace("softDelete By Query  SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String softDeleteByLambdaQuery(Class<?> entityClass, LambdaQuery <T> lambdaQuery) {
		logger.trace("softDelete By LambdaQuery \n{}" , lambdaQuery);
		FieldMetadata.buildColumnMapper(entityClass);
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		
		SQL sql = new SQL()
				.UPDATE(TableMetadata.getTableName(entityClass))
				.SET(" %s = '%s' ".formatted(
						logicColumnMapper.getColumnName(),
						logicColumnMapper.getSoftDelete().delete()
					)
				).WHERE(LambdaQueryBuilder.build(lambdaQuery));
		
		logger.trace("softDelete By LambdaQuery  SQL \n{}" , sql);
		return sql.toString();
	}

}
