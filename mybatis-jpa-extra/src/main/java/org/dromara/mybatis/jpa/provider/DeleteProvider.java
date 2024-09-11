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
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata.SQL_TYPE;
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
public class DeleteProvider <T extends JpaEntity>{	
	static final Logger logger 	= 	LoggerFactory.getLogger(DeleteProvider.class);
	
	public String deleteById(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		if (MapperMetadata.getSqlsMap().containsKey(tableName + SQL_TYPE.REMOVE_SQL)) {
			return MapperMetadata.getSqlsMap().get(tableName + SQL_TYPE.REMOVE_SQL);
		}
		
		String idValue = (String) parametersMap.get(MapperMetadata.PARAMETER_ID);
		String partitionKeyValue = (String) parametersMap.get(MapperMetadata.PARAMETER_PARTITION_KEY);
		FieldColumnMapper partitionKeyColumnMapper = FieldMetadata.getPartitionKey(entityClass);
		FieldColumnMapper idFieldColumnMapper = FieldMetadata.getIdColumn(entityClass);
		
		SQL sql=new SQL().DELETE_FROM(tableName);
		if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
			sql.WHERE(" %s = #{%s} and %s = '%s' "
					.formatted(
							partitionKeyColumnMapper.getColumnName() ,
							partitionKeyValue,
							idFieldColumnMapper.getColumnName(),
							idValue)
        			);  
		}else {
			sql.WHERE("%s = '%s'"
					.formatted(
							idFieldColumnMapper.getColumnName(),
							idValue) 
					);  
		}
		
        String deleteSql = sql.toString(); 
        MapperMetadata.getSqlsMap().put(tableName + SQL_TYPE.REMOVE_SQL,deleteSql);
        logger.trace("Delete SQL \n{}" , deleteSql);
        return deleteSql;  
    }  
	
	@SuppressWarnings("unchecked")
	public String batchDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get(MapperMetadata.PARAMETER_ID_LIST);
		
		StringBuffer keyValue = new StringBuffer();
		for(String value : idValues) {
			if(value.trim().length() > 0) {
				keyValue.append(",'").append(value).append("'");
				logger.trace("delete by id {}" , value);
			}
		}
		//remove ;
		String keyValues = keyValue.substring(1).replace(";", "");
		
		String partitionKeyValue = (String) parametersMap.get(MapperMetadata.PARAMETER_PARTITION_KEY);
		FieldColumnMapper partitionKeyColumnMapper = FieldMetadata.getPartitionKey(entityClass);
		FieldColumnMapper idFieldColumnMapper = FieldMetadata.getIdColumn(entityClass);
		
		SQL sql=new SQL().DELETE_FROM(tableName);
		
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
		
        String deleteSql=sql.toString(); 
        MapperMetadata.getSqlsMap().put(tableName + SQL_TYPE.BATCHDELETE_SQL,deleteSql);
        logger.trace("Delete SQL \n{}" , deleteSql);
        return deleteSql;  
    } 
	
	public String deleteByQuery(Class<?> entityClass, Query query) {
		logger.trace("delete By Query \n{}" , query);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		SQL sql = new SQL().DELETE_FROM(tableName).WHERE(QueryBuilder.build(query));
		
		logger.trace("delete By Query SQL \n{}" , sql);
		return sql.toString();
	}
	
	public String deleteByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
		logger.trace("delete By LambdaQuery \n{}" , lambdaQuery);
		FieldMetadata.buildColumnList(entityClass);
		String tableName = TableMetadata.getTableName(entityClass);
		SQL sql = new SQL().DELETE_FROM(tableName).WHERE(LambdaQueryBuilder.build(lambdaQuery));
		
		logger.trace("delete By LambdaQuery SQL \n{}" , sql);
		return sql.toString();
	}
	
	
	
}
