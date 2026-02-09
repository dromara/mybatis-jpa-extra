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

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
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
public class CountProvider<T extends JpaEntity,ID extends Serializable> {
    static final Logger logger = LoggerFactory.getLogger(CountProvider.class);
    
    public String countById(Class<?> entityClass, ID id) {
        logger.trace("count by Id \n{}" , id);
        SQL sql = TableMetadata.buildSelectCount(entityClass);
        //id
        ColumnMapper idFieldColumnMapper = ColumnMetadata.getIdColumn(entityClass);
        sql.WHERE(" %s = %s ".formatted(idFieldColumnMapper.getColumn(),SafeValueHandler.valueOfType(id)));
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);

        //逻辑删除
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            sql.WHERE(" ( %s = '%s' )" 
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        
        logger.trace("count by Id SQL \n{}" , sql);
        return sql.toString();
    }
    
    public String countByQuery(Class<?> entityClass, Query query) {
        logger.trace("count Query \n{}" , query);
        SQL sql = TableMetadata.buildSelectCount(entityClass);
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        StringBuilder whereSql = new StringBuilder("");
        //查询语句
        String querySql = QueryBuilder.build(query);
        if(StringUtils.isNotBlank(querySql)) {
            sql.WHERE("( " + querySql +" ) ");
        }
        
        //逻辑删除
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && query.isSoftDelete()) {
            sql.WHERE(" ( %s = '%s' )" 
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        
        if(StringUtils.isNotBlank(whereSql)) {
            sql.WHERE(whereSql.toString());
        }
        
        logger.trace("count By Query SQL \n{}" , sql);
        return sql.toString();
    }
    
    public String countByLambdaQuery(Class<?> entityClass, LambdaQuery<T> lambdaQuery) {
        logger.trace("count LambdaQuery \n{}" , lambdaQuery);
        
        SQL sql = TableMetadata.buildSelectCount(entityClass);
        
        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(entityClass);
        StringBuilder whereSql = new StringBuilder("");
        //查询语句
        String querySql = LambdaQueryBuilder.build(lambdaQuery);
        if(StringUtils.isNotBlank(querySql)) {
            sql.WHERE("( " + querySql +" ) ");
        }
        //逻辑删除
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete() && lambdaQuery.isSoftDelete()) {
            sql.WHERE(" ( %s = '%s' )" 
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        
        if(StringUtils.isNotBlank(whereSql)) {
            sql.WHERE(whereSql.toString());
        }

        logger.trace("count By Query SQL \n{}" , sql);
        return sql.toString();
    }
    
}
