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
package org.dromara.mybatis.jpa.provider.impl;

import java.io.Serializable;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
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
public class SoftDeleteProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(SoftDeleteProvider.class);

    
    public String softDeleteById(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        
        SQL sql=new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        
        if(partitionKeyColumnMapper != null) {
            sql.WHERE(" %s = #{partitionKey} "
                    .formatted(partitionKeyColumnMapper.getColumn()));  
        }
        
        sql.WHERE("%s = #{id}".formatted(idFieldColumnMapper.getColumn()) );  
        
        String deleteSql = sql.toString(); 
        logger.trace("softDelete SQL \n{}" , deleteSql);
        return deleteSql;  
    }
    
    public String softDelete(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        ColumnMapper partitionKeyColumnMapper = ColumnMetadata.getPartitionKey(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        
        SQL sql=new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        StringBuilder deleteSql = new StringBuilder("");
        deleteSql.append("<script>").append("\n").append("\n")
            .append(sql.toString()).append("\n")
            .append("WHERE ")
            .append(idFieldColumnMapper.getColumn())
            .append(" in (\n")
            .append(" <foreach collection =\"idList\" item=\"item\" separator =\",\">").append("\n")
            .append("  #{item} ").append("\n")
            .append(" </foreach>")
            .append(" )\n");
        if(partitionKeyColumnMapper != null) {
            deleteSql.append(" and %s = #{partitionKey} )"
                    .formatted(partitionKeyColumnMapper.getColumn()));  
        }
        deleteSql.append("\n").append("</script>");
        logger.trace("softDelete SQL \n{}" , deleteSql);
        return deleteSql.toString();  
    } 
    
    public String softDeleteByQuery(Class<?> entityClass, Query query) {
        logger.trace("softDelete By Query \n{}" , query);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        
        SQL sql = new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                ).WHERE(QueryBuilder.build(query));
        
        logger.trace("softDelete By Query  SQL \n{}" , sql);
        return sql.toString();
    }
    
    public String softDeleteByLambdaQuery(Class<?> entityClass, LambdaQuery <T> lambdaQuery) {
        logger.trace("softDelete By LambdaQuery \n{}" , lambdaQuery);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        
        SQL sql = new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                ).WHERE(LambdaQueryBuilder.build(lambdaQuery));
        
        logger.trace("softDelete By LambdaQuery  SQL \n{}" , sql);
        return sql.toString();
    }

}
