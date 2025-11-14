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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.annotations.ColumnDefault;
import org.dromara.mybatis.jpa.annotations.Encrypted;
import org.dromara.mybatis.jpa.annotations.PartitionKey;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.Transient;

public class ColumnMetadata {
    private static final Logger logger     =     LoggerFactory.getLogger(ColumnMetadata.class);
    
    static ConcurrentMap<String, List<ColumnMapper>> columnMapper     =     new ConcurrentHashMap<>();
    
    static ConcurrentMap<String, ColumnMapper> logicColumnMapper     =     new ConcurrentHashMap<>();
    
    static ConcurrentMap<String, ColumnMapper> idColumnMapper         =     new ConcurrentHashMap<>();
    
    static ConcurrentMap<String, ColumnMapper> partitionKeyMapper     =     new ConcurrentHashMap<>();
    
    
    public static  ColumnMapper getIdColumn(Class<?> entityClass) {
        return idColumnMapper.get(entityClass.getName());
    }
    
    public static  ColumnMapper getLogicColumn(Class<?> entityClass) {
        return logicColumnMapper.get(entityClass.getName());
    }
    
    public static  ColumnMapper getPartitionKey(Class<?> entityClass) {
        return partitionKeyMapper.get(entityClass.getName());
    }
    
    /**
     * get select table Column from entityClass, data cache in fieldsMap
     * @param entityClass
     * @return selectColumn
     */
    public static String selectColumnMapper(Class<?> entityClass) {
        StringBuilder selectColumn = new StringBuilder(ConstMetadata.SELECT_TMP_TABLE + ".* ");
        int columnCount = 0;
        for(ColumnMapper fieldColumnMapper  : columnMapper.get(entityClass.getName())) {
            columnCount ++;
            //不同的属性和数据库字段不一致的需要进行映射
            if(!fieldColumnMapper.getColumn().equalsIgnoreCase(fieldColumnMapper.getField())) {
                selectColumn.append(",")
                            .append(fieldColumnMapper.getColumn())
                            .append(" ")
                            .append(fieldColumnMapper.getField());
            }
            if(logger.isTraceEnabled()) {
                logger.trace("Column {} , ColumnName : {} , FieldName : {}"  ,
                    String.format(ConstMetadata.LOG_FORMAT_COUNT, columnCount),String.format(ConstMetadata.LOG_FORMAT, fieldColumnMapper.getColumn()),fieldColumnMapper.getField());
            }
        }
        return selectColumn.toString();
    }
    
    /**
     * buildColumnMapper
     * @param entityClass
     */
    public static List<ColumnMapper> buildColumnMapper(Class<?> entityClass) {
        String entityClassName = entityClass.getName();
        if (!columnMapper.containsKey(entityClassName)) {
            logger.trace("entityClass {}" , entityClass);
            Field[] fields = entityClass.getDeclaredFields();
            List<ColumnMapper>columnMapperList = new ArrayList<>(fields.length);
    
            for (Field field : fields) {
                //skip Transient field
                if(field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                
                if (field.isAnnotationPresent(Column.class)) {
                    String columnName = "";
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    //if column name is null or '' , then set as field name
                    if (StringUtils.isNotBlank(columnAnnotation.name())) {
                        columnName = columnAnnotation.name();
                    } else {
                    	if(MapperMetadata.isMapUnderscoreToCamelCase()) {
                    		columnName = StrUtils.camelToUnderline(field.getName());
                    	}else {
                    		columnName = field.getName();
                    	}
                    }
                    columnName = MapperMetadata.tableOrColumnCaseConverter(columnName);
                    columnName = MapperMetadata.tableOrColumnEscape(columnName);
                    
                    ColumnMapper fieldColumnMapper = 
                            new ColumnMapper(field,field.getName(),field.getType().getSimpleName(),columnName);
                    fieldColumnMapper.setColumnAnnotation(columnAnnotation);
                    
                    if(field.isAnnotationPresent(Id.class)) {
                        fieldColumnMapper.setIdColumn(true);
                        idColumnMapper.put(entityClassName, fieldColumnMapper);
                    }
                    
                    if(field.isAnnotationPresent(GeneratedValue.class)) {
                        GeneratedValue generatedValue=field.getAnnotation(GeneratedValue.class);
                        fieldColumnMapper.setGeneratedValue(generatedValue);
                        fieldColumnMapper.setGenerated(true);
                    }
                    if (field.isAnnotationPresent(Temporal.class)) {
                        Temporal temporalAnnotation = field.getAnnotation(Temporal.class);
                        fieldColumnMapper.setTemporalAnnotation(temporalAnnotation);
                    }
                    if (field.isAnnotationPresent(ColumnDefault.class)) {
                        ColumnDefault columnDefault = field.getAnnotation(ColumnDefault.class);
                        fieldColumnMapper.setColumnDefault(columnDefault);
                    }
                    if (field.isAnnotationPresent(PartitionKey.class)) {
                        PartitionKey partitionKey = field.getAnnotation(PartitionKey.class);
                        fieldColumnMapper.setPartitionKey(partitionKey);
                        partitionKeyMapper.put(entityClassName, fieldColumnMapper);
                    }
                    if (field.isAnnotationPresent(SoftDelete.class)) {
                        SoftDelete columnLogic = field.getAnnotation(SoftDelete.class);
                        fieldColumnMapper.setSoftDelete(columnLogic);
                        fieldColumnMapper.setLogicDelete(true);
                        logicColumnMapper.put(entityClassName, fieldColumnMapper);
                    }
                    
                    if (field.isAnnotationPresent(Encrypted.class)) {
                        Encrypted columnEncrypted = field.getAnnotation(Encrypted.class);
                        fieldColumnMapper.setEncrypted(true);
                        fieldColumnMapper.setEncryptedAnnotation(columnEncrypted);
                    }
                    
                    logger.trace("FieldColumnMapper : {}" , fieldColumnMapper);
                    columnMapperList.add(fieldColumnMapper);
                }
            }
            logger.trace("Class {} , Column List : {}" , entityClassName,columnMapperList);
            
            columnMapper.put(entityClassName, columnMapperList);
            logger.trace("Column Mapper : {}" , columnMapper);
        }
        return columnMapper.get(entityClassName);
    }

}
