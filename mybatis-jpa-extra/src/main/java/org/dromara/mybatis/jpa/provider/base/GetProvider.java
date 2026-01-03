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

import java.io.Serializable;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class GetProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(GetProvider.class);
    
    public String get(Map<String, Object>  parametersMap) {
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        
        SQL sql = TableMetadata.buildSelect(entityClass);
        sql.WHERE(" %s = #{id} ".formatted(idFieldColumnMapper.getColumn()));
        
        if(partitionKeyColumnMapper != null) {
            sql.WHERE(" %s = #{partitionKey} \n".formatted(partitionKeyColumnMapper.getColumn()));
        }
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            sql.WHERE(" %s = '%s'".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().value()));
        }
        
        String getSql = sql.toString(); 
        logger.trace("Get SQL \n{}" , getSql);
        return getSql;  
    }
}
