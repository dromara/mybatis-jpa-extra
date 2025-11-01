/*
 * Copyright [2023] [MaxKey of copyright http://www.maxkey.top]
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

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.builder.LambdaQueryBuilder;
import org.dromara.mybatis.jpa.query.builder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class DeleteProvider <T extends JpaEntity>{    
    static final Logger logger     =     LoggerFactory.getLogger(DeleteProvider.class);
    
    public String deleteById(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        
        String idValue = (String) parametersMap.get(ConstMetadata.PARAMETER_ID);
        String partitionKeyValue = (String) parametersMap.get(ConstMetadata.PARAMETER_PARTITION_KEY);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        
        SQL sql=new SQL().DELETE_FROM(TableMetadata.getTableName(entityClass));
        if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
            sql.WHERE(" %s = #{%s} and %s = '%s' "
                    .formatted(
                            partitionKeyColumnMapper.getColumn() ,
                            partitionKeyValue,
                            idFieldColumnMapper.getColumn(),
                            SafeValueHandler.valueOf(idValue))
                    );  
        }else {
            sql.WHERE("%s = '%s'"
                    .formatted(
                            idFieldColumnMapper.getColumn(),
                            SafeValueHandler.valueOf(idValue)) 
                    );  
        }
        
        String deleteSql = sql.toString(); 
        logger.trace("Delete SQL \n{}" , deleteSql);
        return deleteSql;  
    }  
    
    @SuppressWarnings("unchecked")
    public String batchDelete(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ArrayList <String> idValues=(ArrayList<String>)parametersMap.get(ConstMetadata.PARAMETER_ID_LIST);
        
        StringBuilder keyValue = new StringBuilder();
        for(String value : idValues) {
            if( StringUtils.isNotBlank(value)) {
                keyValue.append(",'").append(SafeValueHandler.valueOf(value)).append("'");
                logger.trace("delete by id {}" , value);
            }
        }
        /**
         * remove ;
         */
        String keyValues = keyValue.substring(1).replace(";", "");
        
        String partitionKeyValue = (String) parametersMap.get(ConstMetadata.PARAMETER_PARTITION_KEY);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        
        SQL sql=new SQL().DELETE_FROM(TableMetadata.getTableName(entityClass));
        
        if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
            sql.WHERE("%s = #{%s} and %s  in ( %s )"
                    .formatted(
                            partitionKeyColumnMapper.getColumn() ,
                            partitionKeyValue,
                            idFieldColumnMapper.getColumn(),
                            idFieldColumnMapper.getField())
                    );  
        }else {
            sql.WHERE(" %s in ( %s )".formatted(idFieldColumnMapper.getColumn(),keyValues));  
        }
        
        String deleteSql = sql.toString(); 
        logger.trace("Delete SQL \n{}" , deleteSql);
        return deleteSql;  
    } 
    
    public String deleteByQuery(Class<?> entityClass, Query query) {
        logger.trace("delete By Query \n{}" , query);
        ColumnMetadata.buildColumnMapper(entityClass);
        SQL sql = new SQL()
                    .DELETE_FROM(TableMetadata.getTableName(entityClass))
                    .WHERE(QueryBuilder.build(query));
        
        logger.trace("delete By Query SQL \n{}" , sql);
        return sql.toString();
    }
    
    public String deleteByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
        logger.trace("delete By LambdaQuery \n{}" , lambdaQuery);
        ColumnMetadata.buildColumnMapper(entityClass);
        SQL sql = new SQL()
                    .DELETE_FROM(TableMetadata.getTableName(entityClass))
                    .WHERE(LambdaQueryBuilder.build(lambdaQuery));
        
        logger.trace("delete By LambdaQuery SQL \n{}" , sql);
        return sql.toString();
    }
    
}
