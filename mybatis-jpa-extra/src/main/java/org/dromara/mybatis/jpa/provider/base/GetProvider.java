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
package org.dromara.mybatis.jpa.provider.base;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MetadataConstants;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class GetProvider <T extends JpaEntity>{	
	static final Logger logger 	= 	LoggerFactory.getLogger(GetProvider.class);
	
	public String get(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MetadataConstants.ENTITY_CLASS);
		FieldMetadata.buildColumnMapper(entityClass);

		String partitionKeyValue = (String) parametersMap.get(MetadataConstants.PARAMETER_PARTITION_KEY);
		FieldColumnMapper partitionKeyColumnMapper = FieldMetadata.getPartitionKey(entityClass);
		FieldColumnMapper idFieldColumnMapper = FieldMetadata.getIdColumn(entityClass);
		
		SQL sql = TableMetadata.buildSelect(entityClass);
		
		sql.WHERE(" %s = #{%s}"
				.formatted(
						idFieldColumnMapper.getColumnName(),
						idFieldColumnMapper.getFieldName())
				);
		
		if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
			sql.WHERE(" %s = #{%s} ".formatted(
					partitionKeyColumnMapper.getColumnName() ,
					partitionKeyValue));
		}
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
			sql.WHERE(" %s = '%s'"
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
        String getSql = sql.toString(); 
        logger.trace("Get SQL \n{}" , getSql);
        return getSql;  
    }
}
