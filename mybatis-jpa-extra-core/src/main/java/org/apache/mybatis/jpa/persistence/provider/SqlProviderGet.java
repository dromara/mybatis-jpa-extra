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
 

/**
 * 
 */
package org.apache.mybatis.jpa.persistence.provider;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.apache.mybatis.jpa.persistence.FieldColumnMapper;
import org.apache.mybatis.jpa.persistence.JpaBaseEntity;
import org.apache.mybatis.jpa.persistence.MapperMetadata;
import org.apache.mybatis.jpa.persistence.MapperMetadata.SQL_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SqlProviderGet <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderGet.class);
	
	public String get(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL);
		}
		
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entityClass)) 
        	.FROM(MapperMetadata.getTableName(entityClass)+" sel_tmp_table ")
        	.WHERE(idFieldColumnMapper.getColumnName() 
        			+ " = #{"+idFieldColumnMapper.getFieldName() + "}");  
		
        String getSql = sql.toString(); 
        _logger.trace("Get SQL \n" + getSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL,getSql);
        return getSql;  
    }
	
	public String selectColumnMapper(Class<?> entityClass) {
		StringBuffer selectColumn =new StringBuffer("sel_tmp_table.* ");
		int columnCount = 0;
		for(FieldColumnMapper fieldColumnMapper  : MapperMetadata.fieldsMap.get(entityClass.getSimpleName())) {
			columnCount ++;
			//不同的属性和数据库字段不一致的需要进行映射
			if(!fieldColumnMapper.getColumnName().equalsIgnoreCase(fieldColumnMapper.getFieldName())) {
				selectColumn.append(",")
							.append(fieldColumnMapper.getColumnName())
							.append(" ")
							.append(fieldColumnMapper.getFieldName());
			}
			_logger.trace("Column {} , ColumnName : {} , FieldName : {}"  ,
					columnCount,fieldColumnMapper.getColumnName(),fieldColumnMapper.getFieldName());
		}
		return selectColumn.toString();
	}
	
	public static String replace(String sql) {
		return 	sql.replaceAll("\r\n+", " \n")
				   .replaceAll("\n+", " \n")
				   .replaceAll("\t", " ")
				   .replaceAll(" +"," ");
	}
	
}
