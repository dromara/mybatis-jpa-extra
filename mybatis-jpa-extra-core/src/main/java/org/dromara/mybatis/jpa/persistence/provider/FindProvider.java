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
package org.dromara.mybatis.jpa.persistence.provider;

import java.sql.Types;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.persistence.JpaBaseEntity;
import org.dromara.mybatis.jpa.persistence.MapperMetadata;
import org.dromara.mybatis.jpa.persistence.MapperMetadata.SQL_TYPE;
import org.dromara.mybatis.jpa.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class FindProvider <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(FindProvider.class);
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(tableName + SQL_TYPE.FINDALL_SQL)) {
			return MapperMetadata.sqlsMap.get(tableName + SQL_TYPE.FINDALL_SQL);
		}
		
		SQL sql=  MapperMetadata.buildSelect(entityClass);
		
        String findAllSql = sql.toString(); 
        _logger.trace("Find All SQL \n" + findAllSql);
        MapperMetadata.sqlsMap.put(tableName + SQL_TYPE.FINDALL_SQL,findAllSql);
        return findAllSql;  
    }
	
	public String find(Map<String, Object>  parametersMap) throws Exception {  
		Class<?> entityClass = (Class<?>) parametersMap.get(MapperMetadata.ENTITY_CLASS);
		Object[] args 	 = (Object[]) parametersMap.get(MapperMetadata.QUERY_ARGS);
		int[] argTypes 	 = (int[]) parametersMap.get(MapperMetadata.QUERY_ARGTYPES);
		String filterSql = parametersMap.get(MapperMetadata.QUERY_FILTER).toString().trim();
		
		MapperMetadata.buildColumnList(entityClass);
		
		if(filterSql.toLowerCase().startsWith("where")) {
			filterSql = filterSql.substring(5);
		}
		
		if(args == null || args.length == 0) {
			filterSql = StringUtils.lineBreak2Blank(filterSql);
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
				if( argTypes[i] == Types.VARCHAR 
						||argTypes[i] == Types.NVARCHAR 
						||argTypes[i] == Types.CHAR
						||argTypes[i] == Types.NCHAR
						||argTypes[i] == Types.LONGVARCHAR 
						||argTypes[i] == Types.LONGNVARCHAR) {
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
			filterSql = StringUtils.lineBreak2Blank(sqlBuffer.toString());
		}
		
		SQL sql = MapperMetadata.buildSelect(entityClass).WHERE(filterSql);
		
        String findSql = sql.toString(); 
        _logger.trace("Find SQL \n" + findSql);

        return findSql;  
    }
	
}
