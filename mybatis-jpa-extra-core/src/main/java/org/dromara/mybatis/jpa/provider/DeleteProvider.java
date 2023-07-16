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

import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata.SQL_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class DeleteProvider <T extends JpaEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(DeleteProvider.class);
	
	public String remove(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(tableName + SQL_TYPE.REMOVE_SQL)) {
			return MapperMetadata.sqlsMap.get(tableName + SQL_TYPE.REMOVE_SQL);
		}
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn((entityClass).getSimpleName());
		
		SQL sql=new SQL()
        	.DELETE_FROM(tableName)
        	.WHERE(idFieldColumnMapper.getColumnName() 
        			+ " = #{" +idFieldColumnMapper.getFieldName() + ",javaType=string,jdbcType=VARCHAR}");  
		
        String deleteSql = sql.toString(); 
        MapperMetadata.sqlsMap.put(tableName + SQL_TYPE.REMOVE_SQL,deleteSql);
        _logger.trace("Delete SQL \n"+deleteSql);
        return deleteSql;  
    }  
	
	@SuppressWarnings("unchecked")
	public String batchDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get("idList");
		String keyValue="";
		for(String value : idValues) {
			if(value.trim().length() > 0) {
				keyValue += ",'" + value + "'";
				_logger.trace("batch delete by id " + value);
			}
		}
		//remove ;
		keyValue = keyValue.substring(1).replaceAll(";", "");
		FieldColumnMapper idFieldColumnMapper = MapperMetadata.getIdColumn(entityClass.getSimpleName());
		
		SQL sql=new SQL()
        	.DELETE_FROM(tableName)
        	.WHERE(idFieldColumnMapper.getColumnName()+" in ( "+keyValue+" )");  
		
        String deleteSql=sql.toString(); 
        MapperMetadata.sqlsMap.put(tableName + SQL_TYPE.BATCHDELETE_SQL,deleteSql);
        _logger.trace("Delete SQL \n" + deleteSql);
        return deleteSql;  
    } 
	
	@SuppressWarnings("unchecked")
	public String logicDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get("idList");
		String keyValue="";
		for(String value : idValues) {
			if(value.trim().length() > 0) {
				keyValue += ",'" + value + "'";
				_logger.trace("logic delete by id " + value);
			}
		}
		//remove ;
		keyValue = keyValue.substring(1).replaceAll(";", "");
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		
		SQL sql=new SQL()
        	.UPDATE(tableName)
        	.SET("status = 9")
        	.WHERE(idFieldColumnMapper.getColumnName()+" in ( "+keyValue+" )");  
		
        String deleteSql = sql.toString(); 
        MapperMetadata.sqlsMap.put(tableName + SQL_TYPE.LOGICDELETE_SQL,deleteSql);
        _logger.trace("logic Delete SQL \n" + deleteSql);
        return deleteSql;  
    } 
	

}
