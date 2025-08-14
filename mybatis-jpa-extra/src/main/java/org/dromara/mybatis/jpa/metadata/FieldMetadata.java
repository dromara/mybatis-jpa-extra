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

import org.dromara.mybatis.jpa.annotations.ColumnDefault;
import org.dromara.mybatis.jpa.annotations.Encrypted;
import org.dromara.mybatis.jpa.annotations.PartitionKey;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.Transient;

public class FieldMetadata {
	private static final Logger logger 	= 	LoggerFactory.getLogger(FieldMetadata.class);
	
	static ConcurrentMap<String, List<FieldColumnMapper>> fieldsMap 	= 	new ConcurrentHashMap<>();
	
	static ConcurrentMap<String, FieldColumnMapper> logicColumnMap 		= 	new ConcurrentHashMap<>();
	
	static ConcurrentMap<String, FieldColumnMapper> idColumnMap 		= 	new ConcurrentHashMap<>();
	
	static ConcurrentMap<String, FieldColumnMapper> partitionKeyMap 		= 	new ConcurrentHashMap<>();
	
	
	public static  FieldColumnMapper getIdColumn(Class<?> entityClass) {
		return idColumnMap.get(entityClass.getName());
	}
	
	public static  FieldColumnMapper getLogicColumn(Class<?> entityClass) {
		return logicColumnMap.get(entityClass.getName());
	}
	
	public static  FieldColumnMapper getPartitionKey(Class<?> entityClass) {
		return partitionKeyMap.get(entityClass.getName());
	}
	
	/**
	 * get select table Column from entityClass, data cache in fieldsMap
	 * @param entityClass
	 * @return selectColumn
	 */
	public static String selectColumnMapper(Class<?> entityClass) {
		StringBuffer selectColumn = new StringBuffer(TableMetadata.SELECT_TMP_TABLE + ".* ");
		int columnCount = 0;
		for(FieldColumnMapper fieldColumnMapper  : fieldsMap.get(entityClass.getName())) {
			columnCount ++;
			//不同的属性和数据库字段不一致的需要进行映射
			if(!fieldColumnMapper.getColumnName().equalsIgnoreCase(fieldColumnMapper.getFieldName())) {
				selectColumn.append(",")
							.append(fieldColumnMapper.getColumnName())
							.append(" ")
							.append(fieldColumnMapper.getFieldName());
			}
			if(logger.isTraceEnabled()) {
				logger.trace("Column {} , ColumnName : {} , FieldName : {}"  ,
					String.format(MapperMetadata.LOG_FORMAT_COUNT, columnCount),String.format(MapperMetadata.LOG_FORMAT, fieldColumnMapper.getColumnName()),fieldColumnMapper.getFieldName());
			}
		}
		return selectColumn.toString();
	}
	
	/**
	 * buildColumnList
	 * @param entityClass
	 */
	public static List<FieldColumnMapper> buildColumnMapper(Class<?> entityClass) {
		String entityClassName = entityClass.getName();
		if (!fieldsMap.containsKey(entityClassName)) {	
			logger.trace("entityClass {}" , entityClass);
			Field[] fields = entityClass.getDeclaredFields();
			List<FieldColumnMapper>fieldColumnMapperList = new ArrayList<>(fields.length);
	
			for (Field field : fields) {
				//skip Transient field
				if(field.isAnnotationPresent(Transient.class)) {
					continue;
				}
				
				if (field.isAnnotationPresent(Column.class)) {
					String columnName = "";
					Column columnAnnotation = field.getAnnotation(Column.class);
					//if column name is null or '' , then set as field name
					if (columnAnnotation.name() != null && !columnAnnotation.name().equals("")) {
					    columnName = columnAnnotation.name();
					} else {
						columnName = field.getName();
					}
					columnName = MapperMetadata.columnCaseConverter(columnName);
					columnName = MapperMetadata.columnEscape(columnName);
					
					FieldColumnMapper fieldColumnMapper = 
							new FieldColumnMapper(field,field.getName(),field.getType().getSimpleName(),columnName);
					fieldColumnMapper.setColumnAnnotation(columnAnnotation);
					
					if(field.isAnnotationPresent(Id.class)) {
						fieldColumnMapper.setIdColumn(true);
						idColumnMap.put(entityClassName, fieldColumnMapper);
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
						partitionKeyMap.put(entityClassName, fieldColumnMapper);
					}
					if (field.isAnnotationPresent(SoftDelete.class)) {
						SoftDelete columnLogic = field.getAnnotation(SoftDelete.class);
						fieldColumnMapper.setSoftDelete(columnLogic);
						fieldColumnMapper.setLogicDelete(true);
						logicColumnMap.put(entityClassName, fieldColumnMapper);
					}
					
					if (field.isAnnotationPresent(Encrypted.class)) {
						Encrypted columnEncrypted = field.getAnnotation(Encrypted.class);
						fieldColumnMapper.setEncrypted(true);
						fieldColumnMapper.setEncryptedAnnotation(columnEncrypted);
					}
					
					logger.trace("FieldColumnMapper : {}" , fieldColumnMapper);
					fieldColumnMapperList.add(fieldColumnMapper);
				}
			}
			
			fieldsMap.put(entityClassName, fieldColumnMapperList);
			logger.trace("fieldsMap : {}" , fieldsMap);
		}
		return fieldsMap.get(entityClassName);
	}

}
