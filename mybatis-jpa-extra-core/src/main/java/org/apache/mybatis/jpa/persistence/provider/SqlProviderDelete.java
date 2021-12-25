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
package org.apache.mybatis.jpa.persistence.provider;

import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.apache.mybatis.jpa.persistence.FieldColumnMapper;
import org.apache.mybatis.jpa.persistence.JpaBaseEntity;
import org.apache.mybatis.jpa.persistence.MapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SqlProviderDelete <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderDelete.class);
	
	public String execute(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL);
		}
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn((entityClass).getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(MapperMetadata.getTableName(entityClass))
        	.WHERE(idFieldColumnMapper.getColumnName() 
        			+ " = #{" +idFieldColumnMapper.getFieldName() + ",javaType=string,jdbcType=VARCHAR}");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL,deleteSql);
        return deleteSql;  
    }  
	
	@SuppressWarnings("unchecked")
	public String batchDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
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
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(MapperMetadata.getTableName(entityClass))
        	.WHERE(idFieldColumnMapper.getColumnName()+" in ( "+keyValue+" )");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.BATCHDELETE_SQL,deleteSql);
        return deleteSql;  
    } 
	
	@SuppressWarnings("unchecked")
	public String logicDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
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
		SQL sql=new SQL();
        sql.UPDATE(MapperMetadata.getTableName(entityClass))
        	.SET("status = 9")
        	.WHERE(idFieldColumnMapper.getColumnName()+" in ( "+keyValue+" )");  
        String deleteSql=sql.toString(); 
        _logger.trace("logic Delete SQL \n"+deleteSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.LOGICDELETE_SQL,deleteSql);
        return deleteSql;  
    } 
	

}
