/*
 * Copyright [2026] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.provider.impl;

import java.util.Map;

import org.apache.commons.lang3.Strings;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;

public abstract class AbstractProvider{

	protected void appendPartitionWhere(SQL sql , Class<?> entityClass,Map<String, Object>  parametersMap) {
		ColumnMapper partitionKeyMapper = ColumnMetadata.getPartitionKey(entityClass);
		if(partitionKeyMapper != null) {
		    parametersMap.put(partitionKeyMapper.getField(), parametersMap.get(ConstMetadata.PARAMETER_PARTITION_KEY));
            sql.WHERE(" %s = #{%s} \n".formatted(
                        partitionKeyMapper.getColumn(),
                        partitionKeyMapper.getField()
                    )); 
        }
	}
	
	protected void appendPartitionWhere(SQL sql , Class<?> entityClass , Object partitionKey) {
	    if(partitionKey != null) {
            ColumnMapper partitionKeyMapper = ColumnMetadata.getPartitionKey(entityClass);
            if(partitionKeyMapper != null ) {
                sql.WHERE(" %s = %s ".formatted(
                            partitionKeyMapper.getColumn(),
                            SafeValueHandler.valueOfType(partitionKey)
                        ));
            }
	    }
    }
	
	protected void appendSoftDeleteWhere(SQL sql , Class<?> entityClass) {
		ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
			sql.WHERE(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().value()
                      ));
		}
	}
	
	protected void appendQuerySoftDeleteWhere(SQL sql, Class<?> entityClass, boolean isSoftDelete) {
        if (isSoftDelete) {
            appendSoftDeleteWhere(sql,entityClass);
        }
    }
	
	protected  String appendWhereOrAnd(String sql){
	    return Strings.CS.contains(sql.toUpperCase(), "WHERE") ? " and " : " WHERE " ;
	}
	
}
