/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.metadata;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class TableMetadata {
    private static final Logger logger     =     LoggerFactory.getLogger(TableMetadata.class);
    
    static ConcurrentMap<String, String> tableNameMap     =     new ConcurrentHashMap<>();

    /**
     * build select from entity Class
     * @param entityClass
     * @return select columns  from table name sel_tmp_table
     */
    public static SQL buildSelect(Class<?> entityClass) {
        ColumnMetadata.buildColumnMapper(entityClass);
        return new SQL().SELECT(ColumnMetadata.selectColumnMapper(entityClass))
                .FROM(TableMetadata.getTableName(entityClass) + ConstMetadata.SELECT_TMP_TABLE);
    }
    
    /**
     * build select count(1) from entity Class
     * @param entityClass
     * @return select columns  from table name sel_tmp_table
     */
    public static SQL buildSelectCount(Class<?> entityClass) {
        ColumnMetadata.buildColumnMapper(entityClass);
        return new SQL().SELECT(" count(1) as _select_count ")
                .FROM(TableMetadata.getTableName(entityClass) + ConstMetadata.SELECT_TMP_TABLE);
    }
    
    /**
     * build select from entity Class
     * @param entityClass
     * @return select columns  from table name sel_tmp_table
     */
    public static SQL buildSelect(Class<?> entityClass , boolean distinct) {
        ColumnMetadata.buildColumnMapper(entityClass);
        if(distinct) {
            return new SQL().SELECT_DISTINCT(ColumnMetadata.selectColumnMapper(entityClass))
                .FROM(TableMetadata.getTableName(entityClass) + ConstMetadata.SELECT_TMP_TABLE);
        }else {
            return new SQL().SELECT(ColumnMetadata.selectColumnMapper(entityClass))
                    .FROM(TableMetadata.getTableName(entityClass) + ConstMetadata.SELECT_TMP_TABLE);
        }
    }
    
    
    /**
     * getTableName and cache table name
     * @param entityClass
     * @return table name
     */
    public static String getTableName(Class<?> entityClass) {
        String entityClassName = entityClass.getName();
        if(!tableNameMap.containsKey(entityClassName)) {

            logger.debug("entity Class Name {}" , entityClassName);
            String tableName = null;
            String schema = null;
            String catalog = null;
            //must use @Entity to ORM class
            Entity entity = entityClass.getAnnotation(Entity.class);
            logger.trace("entity {}" , entity);
            Table table = entityClass.getAnnotation(Table.class);
            logger.trace("table {}" , table);
            if(entity != null ) {
                if(entity.name() != null && !entity.name().equals("")) {
                    tableName = entity.name();
                }
                if (table != null) {
                    if(table.name() != null && !table.name().equals("")) {
                        tableName = table.name();
                    }
                    if(table.schema() != null && !table.schema().equals("")) {
                        schema = table.schema();
                        logger.trace("schema {}" , schema);
                    }
                    
                    if(table.catalog() != null && !table.catalog().equals("")) {
                        catalog = table.catalog();
                        logger.trace("catalog {}" , catalog);
                    }
                }
                
                if(tableName == null) {
                    tableName = entityClass.getSimpleName();
                }
                
                if(schema != null) {
                    tableName = schema+"."+tableName;
                }
                
                if(catalog != null) {
                    tableName = catalog+"."+tableName;
                }
            }
            
            tableName = MapperMetadata.tableOrColumnCaseConverter(tableName);
            
            tableName = MapperMetadata.tableOrColumnEscape(tableName);
            
            tableNameMap.put(entityClassName,tableName);
            logger.trace("Table Name {}" , tableName);
        }
        return tableNameMap.get(entityClassName);
    }
    
}
