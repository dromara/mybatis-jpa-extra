/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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
package org.dromara.mybatis.jpa.metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.annotations.ColumnDefault;
import org.dromara.mybatis.jpa.annotations.PartitionKey;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.Transient;

/**
 * @author Crystal.Sea
 *
 */
public class MapperMetadata <T extends JpaEntity>{
	
	private static final Logger logger 	= 	LoggerFactory.getLogger(MapperMetadata.class);
	
	public  static  class SQL_TYPE{
		public static final String 	GET_SQL							= "_GET_SQL";
		public static final String 	FINDALL_SQL						= "_FINDALL_SQL";
		public static final String 	REMOVE_SQL						= "_REMOVE_SQL";
		public static final String 	BATCHDELETE_SQL					= "_BATCHDELETE_SQL";
		public static final String 	LOGICDELETE_SQL					= "_LOGICDELETE_SQL";
	}
	
	public static 	final		String ENTITY_CLASS				= "entityClass";
	
	public static 	final		String ENTITY					= "entity";
	
	
	public static 	final		String PAGE						= "page";
	
	public static 	final		String CONDITION				= "condition";
	
	public static 	final		String QUERY_FILTER				= "filter";
	public static 	final		String QUERY_ARGS				= "args";
	public static 	final		String QUERY_ARGTYPES			= "argTypes";
	
	public static 	final		String PARAMETER_PARTITION_KEY	= "partitionKey";
	public static 	final		String PARAMETER_ID_LIST		= "idList";
	public static 	final		String PARAMETER_ID				= "id";
	/**
	 * 表名和字段名
	 */
	public static int 		TABLE_COLUMN_CASE 					= CASE_TYPE.LOWERCASE;
	public static boolean   TABLE_COLUMN_ESCAPE                 = false;
	public static String    TABLE_COLUMN_ESCAPE_CHAR            =  "`";
	public static String    PARTITION_COLUMN           			=  "inst_id";
	
	public static class CASE_TYPE{
		public static final int 		NORMAL 							= 0;
		public static final int 		LOWERCASE 						= 1;
		public static final int 		UPPERCASE 						= 2;
	}
	
	
	public static ConcurrentMap<String, List<FieldColumnMapper>> fieldsMap 	= 	new ConcurrentHashMap<>();
	
	public static ConcurrentMap<String, String>sqlsMap 	= 	new ConcurrentHashMap<>();
	
	public static ConcurrentMap<String, String>tableNameMap 	= 	new ConcurrentHashMap<>();
	
	public static IdentifierGeneratorFactory identifierGeneratorFactory = new IdentifierGeneratorFactory();
	
	/**
	 * getTableName and cache table name
	 * @param entityClass
	 * @return table name
	 */
	public static String getTableName(Class<?> entityClass) {
		String entityClassName = entityClass.getSimpleName();
		logger.debug("entity Class Name {}" , entityClassName);
		if(tableNameMap.containsKey(entityClassName)) {
			return tableNameMap.get(entityClassName);
		}
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
				tableName = entityClassName;
			}
			
			if(schema != null) {
				tableName = schema+"."+tableName;
			}
			
			if(catalog != null) {
				tableName = catalog+"."+tableName;
			}
		}
		
		tableName = tableColumnCaseConverter(tableName);
		
		tableName = TABLE_COLUMN_ESCAPE ? TABLE_COLUMN_ESCAPE_CHAR + tableName + TABLE_COLUMN_ESCAPE_CHAR : tableName;
		tableNameMap.put(entityClassName,tableName);
		logger.trace("Table Name {}" , tableName);
		return tableName;
	}
	
	public static  FieldColumnMapper getIdColumn(String  classSimpleName) {
		List<FieldColumnMapper> listFields = fieldsMap.get(classSimpleName);
		FieldColumnMapper idFieldColumnMapper = null;
		for (int i = 0; i < listFields.size(); i++) {
			if (listFields.get(i).isIdColumn()) {
				idFieldColumnMapper = listFields.get(i);
				break;
			}
		}
		return idFieldColumnMapper;
	}
	
	public static  FieldColumnMapper getLogicColumn(String  classSimpleName) {
		List<FieldColumnMapper> listFields = fieldsMap.get(classSimpleName);
		FieldColumnMapper logicColumnMapper = null;
		for (int i = 0; i < listFields.size(); i++) {
			if (listFields.get(i).isLogicDelete()) {
				logicColumnMapper = listFields.get(i);
				break;
			}
		}
		return logicColumnMapper;
	}
	
	public static  FieldColumnMapper getPartitionKey(String  classSimpleName) {
		List<FieldColumnMapper> listFields = fieldsMap.get(classSimpleName);
		FieldColumnMapper partitionKeyColumnMapper = null;
		for (FieldColumnMapper column : listFields) {
			if (column.getPartitionKey() != null) {
				partitionKeyColumnMapper = column;
				break;
			}
		}
		return partitionKeyColumnMapper;
	}

	/**
	 * get select table Column from entityClass, data cache in fieldsMap
	 * @param entityClass
	 * @return selectColumn
	 */
	public static String selectColumnMapper(Class<?> entityClass) {
		StringBuffer selectColumn = new StringBuffer("sel_tmp_table.* ");
		int columnCount = 0;
		for(FieldColumnMapper fieldColumnMapper  : fieldsMap.get(entityClass.getSimpleName())) {
			columnCount ++;
			//不同的属性和数据库字段不一致的需要进行映射
			if(!fieldColumnMapper.getColumnName().equalsIgnoreCase(fieldColumnMapper.getFieldName())) {
				selectColumn.append(",")
							.append(fieldColumnMapper.getColumnName())
							.append(" ")
							.append(fieldColumnMapper.getFieldName());
			}
			logger.trace("Column {} , ColumnName : {} , FieldName : {}"  ,
					columnCount,fieldColumnMapper.getColumnName(),fieldColumnMapper.getFieldName());
		}
		return selectColumn.toString();
	}
	
	/**
	 * build select from entity Class
	 * @param entityClass
	 * @return select columns  from table name sel_tmp_table
	 */
	public static SQL buildSelect(Class<?> entityClass) {
		buildColumnList(entityClass);
		return new SQL().SELECT(selectColumnMapper(entityClass))
				.FROM(getTableName(entityClass) + " sel_tmp_table ");
	}
	
	/**
	 * buildColumnList
	 * @param entityClass
	 */
	public static void buildColumnList(Class<?> entityClass) {
		if (fieldsMap.containsKey(entityClass.getSimpleName())) {
			//run one time
			return;
		}
		
		logger.trace("entityClass {}" , entityClass);
		
		Field[] fields = entityClass.getDeclaredFields();
		List<FieldColumnMapper>fieldColumnMapperList=new ArrayList<>(fields.length);

		for (Field field : fields) {
			//skip Transient field
			if(field.isAnnotationPresent(Transient.class)) {
				continue;
			}
			
			if (field.isAnnotationPresent(Column.class)) {
				FieldColumnMapper fieldColumnMapper = new FieldColumnMapper();
				fieldColumnMapper.setFieldName( field.getName());
				fieldColumnMapper.setFieldType(field.getType().getSimpleName());
				String columnName = "";
				Column columnAnnotation = field.getAnnotation(Column.class);
				fieldColumnMapper.setColumnAnnotation(columnAnnotation);
				if (columnAnnotation.name() != null && !columnAnnotation.name().equals("")) {
				    columnName = columnAnnotation.name();
				} else {
					columnName = field.getName();
				}
				
				columnName = tableColumnCaseConverter(columnName);
				
				columnName = TABLE_COLUMN_ESCAPE ? 
				        TABLE_COLUMN_ESCAPE_CHAR + columnName + TABLE_COLUMN_ESCAPE_CHAR 
				        : columnName;
				
				fieldColumnMapper.setColumnName(columnName);
				
				if(field.isAnnotationPresent(Id.class)) {
					fieldColumnMapper.setIdColumn(true);
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
				}
				if (field.isAnnotationPresent(SoftDelete.class)) {
					SoftDelete columnLogic = field.getAnnotation(SoftDelete.class);
					fieldColumnMapper.setSoftDelete(columnLogic);
					fieldColumnMapper.setLogicDelete(true);
				}
				logger.trace("FieldColumnMapper : {}" , fieldColumnMapper);
				fieldColumnMapperList.add(fieldColumnMapper);
			}
			
			
		}
		
		fieldsMap.put(entityClass.getSimpleName(), fieldColumnMapperList);
		logger.trace("fieldsMap : {}" , fieldsMap);

	}
	
	/**
	 * Case Converter
	 * @param name
	 * @return case
	 */
	public static String tableColumnCaseConverter(String name) {
		if(TABLE_COLUMN_CASE  == CASE_TYPE.LOWERCASE) {
			name = name.toLowerCase();
		}else if(TABLE_COLUMN_CASE  == CASE_TYPE.UPPERCASE) {
			name = name.toUpperCase();
		}
		return name;
	}
}
