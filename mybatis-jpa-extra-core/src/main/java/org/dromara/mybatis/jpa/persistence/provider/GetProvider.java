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

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.persistence.FieldColumnMapper;
import org.dromara.mybatis.jpa.persistence.JpaBaseEntity;
import org.dromara.mybatis.jpa.persistence.MapperMetadata;
import org.dromara.mybatis.jpa.persistence.MapperMetadata.SQL_TYPE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class GetProvider <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(GetProvider.class);
	
	public String get(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		String tableName = MapperMetadata.getTableName(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(tableName + SQL_TYPE.GET_SQL)) {
			return MapperMetadata.sqlsMap.get(tableName + SQL_TYPE.GET_SQL);
		}
		
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		
		SQL sql = MapperMetadata.buildSelect(entityClass)
        	.WHERE(idFieldColumnMapper.getColumnName() 
        			+ " = #{"+idFieldColumnMapper.getFieldName() + "}");  
		
        String getSql = sql.toString(); 
        MapperMetadata.sqlsMap.put(tableName + SQL_TYPE.GET_SQL,getSql);
        _logger.trace("Get SQL \n" + getSql);
        return getSql;  
    }
}
