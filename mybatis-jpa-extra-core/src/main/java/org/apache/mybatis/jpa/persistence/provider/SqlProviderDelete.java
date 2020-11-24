/**
 * 
 */
package org.apache.mybatis.jpa.persistence.provider;

import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.apache.mybatis.jpa.persistence.FieldColumnMapper;
import org.apache.mybatis.jpa.persistence.JpaBaseDomain;
import org.apache.mybatis.jpa.persistence.MapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SqlProviderDelete <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderDelete.class);
	
	public String execute(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL);
		}
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn((entityClass).getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(MapperMetadata.getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" = #{"+idFieldColumnMapper.getFieldName()+",javaType=string,jdbcType=VARCHAR}");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL,deleteSql);
        return deleteSql;  
    }  
	
	public String batchDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		
		@SuppressWarnings("unchecked")
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get("idList");
		String keyValue="";
		for(String value : idValues) {
			keyValue+=",'"+value+"'";
		}
		keyValue=keyValue.substring(1);
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(MapperMetadata.getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" IN ( "+keyValue+" )");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + MapperMetadata.SQL_TYPE.REMOVE_SQL,deleteSql);
        return deleteSql;  
    } 
	

}
