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

import java.sql.Types;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
public class SqlProviderFind <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderFind.class);
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL);
		}
		
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entityClass))  
        	.FROM(MapperMetadata.getTableName(entityClass)+" sel_tmp_table ");  
		
        String findAllSql = sql.toString(); 
        _logger.trace("Find All SQL \n" + findAllSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL,findAllSql);
        return findAllSql;  
    }
	
	public String find(Map<String, Object>  parametersMap) throws Exception {  
		Class<?> entityClass=(Class<?>)	parametersMap.get(MapperMetadata.ENTITY_CLASS);
		Object[] args 	= (Object[])	parametersMap.get(MapperMetadata.QUERY_ARGS);
		int[] argTypes 	= (int[])		parametersMap.get(MapperMetadata.QUERY_ARGTYPES);
		String filterSql = parametersMap.get(MapperMetadata.QUERY_FILTER).toString().trim();
		
		MapperMetadata.buildColumnList(entityClass);
		
		if(filterSql.toLowerCase().startsWith("where")) {
			filterSql = filterSql.substring(5);
		}
		
		if(args == null || args.length == 0) {
			filterSql = replace(filterSql);
		}else {
			int countMatches = StringUtils.countMatches(filterSql, "?");
			if(args.length < countMatches) {
				_logger.error("args length {} < parameter placeholder {}" ,  countMatches,args.length);
				throw new Exception("args length < parameter placeholder");
			}
			
			String filterSqls [] = filterSql.split("\\?");
			StringBuffer sqlBuffer = new StringBuffer("");
			for(int i = 0 ;i < args.length ; i++){
				_logger.trace("Find args[{}] {}" , i, args[i]);
				if(argTypes[i] == Types.VARCHAR ||argTypes[i] == Types.NVARCHAR 
						||argTypes[i] == Types.CHAR||argTypes[i] == Types.NCHAR
						||argTypes[i] == Types.LONGVARCHAR ||argTypes[i] == Types.LONGNVARCHAR) {
					sqlBuffer
						.append(filterSqls[i])
						.append("'")
						.append(args[i].toString().replaceAll("'", ""))
						.append("'");
				}else {
					sqlBuffer
					.append(filterSqls[i])
					.append(args[i]);
				}
			}
			filterSql = replace(sqlBuffer.toString());
		}
		
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entityClass))  
        	.FROM(MapperMetadata.getTableName(entityClass)+" sel_tmp_table ")
			.WHERE(filterSql);
		
        String findSql = sql.toString(); 
        _logger.trace("Find SQL \n" + findSql);

        return findSql;  
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
