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

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class FindProvider <T extends JpaEntity>{
    static final Logger logger     =     LoggerFactory.getLogger(FindProvider.class);
    
    public String findAll(Map<String, Object>  parametersMap) {  
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        
        SQL sql=  TableMetadata.buildSelect(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            sql.WHERE(" %s = '%s'"
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        String findAllSql = sql.toString(); 
        logger.trace("Find All SQL \n{}" , findAllSql);
        return findAllSql;  
    }
    
    public String find(Map<String, Object>  parametersMap) throws Exception {  
        Class<?> entityClass = (Class<?>) parametersMap.get(ConstMetadata.ENTITY_CLASS);
        Object[] args      = (Object[]) parametersMap.get(ConstMetadata.QUERY_ARGS);
        int[] argTypes      = (int[]) parametersMap.get(ConstMetadata.QUERY_ARGTYPES);
        String filterSql = parametersMap.get(ConstMetadata.QUERY_FILTER).toString().trim();
        
        ColumnMetadata.buildColumnMapper(entityClass);
        
        if(filterSql.toLowerCase().startsWith("where")) {
            filterSql = filterSql.substring(5);
        }
        
        if(args == null || args.length == 0) {
            filterSql = StrUtils.lineBreakToBlank(filterSql);
        }else {
            int countMatches = StringUtils.countMatches(filterSql, "?");
            if(args.length < countMatches) {
                logger.error("args length {} < parameter placeholder {}" ,  countMatches,args.length);
                throw new Exception("args length < parameter placeholder");
            }
            
            String[] filterSqls  = filterSql.split("\\?");
            StringBuilder sqlBuffer = new StringBuilder("");
            for(int i = 0 ;i < filterSqls.length ; i++){
                logger.trace("Find filterSqls[{}] = {}" , i , filterSqls[i]);
                if(i < args.length) {
                    logger.trace("Find args[{}] {}" , i, args[i]);
                    if( argTypes[i] == Types.VARCHAR 
                            ||argTypes[i] == Types.NVARCHAR 
                            ||argTypes[i] == Types.CHAR
                            ||argTypes[i] == Types.NCHAR
                            ||argTypes[i] == Types.LONGVARCHAR 
                            ||argTypes[i] == Types.LONGNVARCHAR) {
                        sqlBuffer
                            .append(filterSqls[i])
                            .append("'")
                            .append(SafeValueHandler.valueOf(args[i].toString()))
                            .append("'");
                    }else {
                        sqlBuffer
                        .append(filterSqls[i])
                        .append(args[i]);
                    }
                }else {
                    sqlBuffer
                    .append(filterSqls[i]);
                }
                logger.trace("Find append {} time SQL [{}]" ,i + 1, sqlBuffer);
            }
            filterSql = StrUtils.lineBreakToBlank(sqlBuffer.toString());
        }
        
        SQL sql = TableMetadata.buildSelect(entityClass).WHERE("( " + filterSql +" )");
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            sql.WHERE(" %s = '%s'"
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        String findSql = sql.toString(); 
        logger.trace("Find SQL \n{}" , findSql);

        return findSql;  
    }
    
    @SuppressWarnings("unchecked")
    public String findByIds(Map<String, Object>  parametersMap) { 
        Class<?> parameterEntityClass = (Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(parameterEntityClass);
        List <String> parameterIds = (List<String>)parametersMap.get(ConstMetadata.PARAMETER_ID_LIST);
        
        StringBuilder keyValues = new StringBuilder();
        for(String value : parameterIds) {
            if(StringUtils.isNotBlank(value)) {
                keyValues.append(",'").append(SafeValueHandler.valueOf(value)).append("'");
            }
        }
        logger.trace("find by id {}" , keyValues);
        
        //remove ';'
        String idsValues = keyValues.substring(1).replace(";", "");
        String partitionKeyValue = (String) parametersMap.get(ConstMetadata.PARAMETER_PARTITION_KEY);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(parameterEntityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(parameterEntityClass);
        
        SQL sql = TableMetadata.buildSelect(parameterEntityClass);
        
        if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
            sql.WHERE("%s = #{%s} and %s  in ( %s )"
                    .formatted(
                            partitionKeyColumnMapper.getColumn() ,
                            partitionKeyValue,
                            idFieldColumnMapper.getColumn(),
                            idsValues)
                    );  
        }else {
            sql.WHERE(" %s in ( %s )".formatted(idFieldColumnMapper.getColumn(),idsValues));  
        }
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(parameterEntityClass);
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            sql.WHERE(" %s = '%s'"
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        
        String findByIdsSql = sql.toString(); 
        logger.trace("Find by ids SQL \n{}" , findByIdsSql);
        return findByIdsSql;  
    } 
    
}
