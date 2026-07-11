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
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.constants.ConstSqlSyntax;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.exceptions.MybatisJpaException;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
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
public class SoftDeleteProvider <T extends JpaEntity,ID extends Serializable>  extends AbstractProvider{    
    static final Logger logger     =     LoggerFactory.getLogger(SoftDeleteProvider.class);

    
    public String softDeleteById(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        if (idFieldColumnMapper == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @Id column for soft delete.");
        }
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if (logicColumnMapper == null || logicColumnMapper.getSoftDelete() == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @SoftDelete annotation.");
        }
        SQL sql=new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        
        appendPartitionWhere(sql , entityClass,parametersMap);
        
        sql.WHERE("%s = #{id}".formatted(idFieldColumnMapper.getColumn()) );  
        
        String deleteSql = sql.toString(); 
        logger.trace("softDelete SQL \n{}" , deleteSql);
        return deleteSql;  
    }
    
    public String softDelete(Map<String, Object>  parametersMap) { 
        Class<?> entityClass=(Class<?>)parametersMap.get(ConstMetadata.ENTITY_CLASS);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if (logicColumnMapper == null || logicColumnMapper.getSoftDelete() == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @SoftDelete annotation.");
        }
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        if (idFieldColumnMapper == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @Id column for soft delete.");
        }
        
        SQL sql=new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        appendPartitionWhere(sql , entityClass,parametersMap);
        StringBuilder deleteSql = new StringBuilder("");
        deleteSql.append("<script>").append("\n").append("\n")
            .append(sql.toString()).append("\n");
        deleteSql.append( appendWhereOrAnd(deleteSql.toString()));
        deleteSql.append(idFieldColumnMapper.getColumn()).append(ConstSqlSyntax.IN)
            .append(" (\n")
            .append(MapperMetadata.buildForeachCollection("idList","item"))
            .append(" )\n");
        deleteSql.append("\n").append("</script>");
        logger.trace("softDelete SQL \n{}" , deleteSql);
        return deleteSql.toString();  
    } 
    
    public String softDeleteByQuery(Class<?> entityClass, Query query) {
        logger.trace("softDelete By Query \n{}" , query);
        Objects.requireNonNull(query, "Query cannot be null");
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if (logicColumnMapper == null || logicColumnMapper.getSoftDelete() == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @SoftDelete annotation.");
        }
        SQL sql = new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        //处理动态查询条件
        String querySql = QueryBuilder.build(query);
        if (StringUtils.isNotBlank(querySql)) {
            sql.WHERE(querySql);
        }
        logger.trace("softDelete By Query  SQL \n{}" , sql);
        return sql.toString();
    }
    
    public String softDeleteByLambdaQuery(Class<?> entityClass, LambdaQuery <T> lambdaQuery) {
        logger.trace("softDelete By LambdaQuery \n{}" , lambdaQuery);
        ColumnMetadata.buildColumnMapper(entityClass);
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        if (logicColumnMapper == null || logicColumnMapper.getSoftDelete() == null) {
            throw new MybatisJpaException("Entity [" + entityClass.getName() + "] lacks @SoftDelete annotation.");
        }
        SQL sql = new SQL()
                .UPDATE(TableMetadata.getTableName(entityClass))
                .SET(" %s = '%s' ".formatted(
                        logicColumnMapper.getColumn(),
                        logicColumnMapper.getSoftDelete().delete()
                    )
                );
        //处理动态查询条件
        String querySql = LambdaQueryBuilder.build(lambdaQuery);
        if (StringUtils.isNotBlank(querySql)) {
            sql.WHERE(querySql);
        }
        logger.trace("softDelete By LambdaQuery  SQL \n{}" , sql);
        return sql.toString();
    }

}
