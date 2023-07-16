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
package org.dromara.mybatis.jpa.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.annotations.ColumnDefault;
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
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(MapperMetadata.class);
	
	public  static class SQL_TYPE{
		public static String 	GET_SQL							= "_GET_SQL";
		public static String 	FINDALL_SQL						= "_FINDALL_SQL";
		public static String 	REMOVE_SQL						= "_REMOVE_SQL";
		public static String 	BATCHDELETE_SQL					= "_BATCHDELETE_SQL";
		public static String 	LOGICDELETE_SQL					= "_LOGICDELETE_SQL";
		
		
	}
	
	public static 			String ENTITY_CLASS					= "entityClass";
	
	public static 			String QUERY_FILTER					= "filter";
	public static 			String QUERY_ARGS					= "args";
	public static 			String QUERY_ARGTYPES				= "argTypes";
	/**
	 * 表名和字段名
	 */
	public static int 		TABLE_COLUMN_CASE 					= CASE_TYPE.LOWERCASE;
	public static boolean   TABLE_COLUMN_ESCAPE                 = false;
	public static String    TABLE_COLUMN_ESCAPE_CHAR            =  "`";
	
	public static class CASE_TYPE{
		public static int 		NORMAL 							= 0;
		public static int 		LOWERCASE 						= 1;
		public static int 		UPPERCASE 						= 2;
	}
	
	
	public transient static ConcurrentMap<String, List<FieldColumnMapper>> 	
											fieldsMap 	= 	new ConcurrentHashMap<String, List<FieldColumnMapper>>();
	
	public transient static ConcurrentMap<String, String>sqlsMap 	= 	new ConcurrentHashMap<String, String>();
	
	public transient static ConcurrentMap<String, String>tableNameMap 	= 	new ConcurrentHashMap<String, String>();
	
	public static IdentifierGeneratorFactory identifierGeneratorFactory = new IdentifierGeneratorFactory();
	
	/**
	 * getTableName and cache table name
	 * @param entityClass
	 * @return table name
	 */
	public static String getTableName(Class<?> entityClass) {
		String entityClassName = entityClass.getSimpleName();
		_logger.debug("entity Class Name {}" , entityClassName);
		if(tableNameMap.containsKey(entityClassName)) {
			return tableNameMap.get(entityClassName);
		}
		String tableName = null;
		String schema = null;
		String catalog = null;
		//must use @Entity to ORM class
		Entity entity =(Entity)entityClass.getAnnotation(Entity.class);
		_logger.trace("entity {}" , entity);
		Table table = (Table)entityClass.getAnnotation(Table.class);
		_logger.trace("table {}" , table);
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
					_logger.trace("schema {}" , schema);
				}
				
				if(table.catalog() != null && !table.catalog().equals("")) {
					catalog = table.catalog();
					_logger.trace("catalog {}" , catalog);
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
		_logger.trace("Table Name {}" , tableName);
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
			_logger.trace("Column {} , ColumnName : {} , FieldName : {}"  ,
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
		
		_logger.trace("entityClass " +entityClass);
		
		Field[] fields = entityClass.getDeclaredFields();
		List<FieldColumnMapper>fieldColumnMapperList=new ArrayList<FieldColumnMapper>(fields.length);

		for (Field field : fields) {
			//skip Transient field
			if(field.isAnnotationPresent(Transient.class)) {
				continue;
			}
			
			if (field.isAnnotationPresent(Column.class)) {
				FieldColumnMapper fieldColumnMapper=new FieldColumnMapper();
				fieldColumnMapper.setFieldName( field.getName());
				fieldColumnMapper.setFieldType(field.getType().getSimpleName());
				String columnName = "";
				Column columnAnnotation = (Column) field.getAnnotation(Column.class);
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
					GeneratedValue generatedValue=(GeneratedValue) field.getAnnotation(GeneratedValue.class);
					fieldColumnMapper.setGeneratedValue(generatedValue);
					fieldColumnMapper.setGenerated(true);
				}
				if (field.isAnnotationPresent(Temporal.class)) {
					Temporal temporalAnnotation = (Temporal) field.getAnnotation(Temporal.class);
					fieldColumnMapper.setTemporalAnnotation(temporalAnnotation);
				}
				if (field.isAnnotationPresent(ColumnDefault.class)) {
					ColumnDefault columnDefault = (ColumnDefault) field.getAnnotation(ColumnDefault.class);
					fieldColumnMapper.setColumnDefault(columnDefault);
				}
				_logger.trace("FieldColumnMapper : " + fieldColumnMapper);
				fieldColumnMapperList.add(fieldColumnMapper);
			}
			
			
		}
		
		fieldsMap.put(entityClass.getSimpleName(), fieldColumnMapperList);
		_logger.trace("fieldsMap : " + fieldsMap);

	}
	
	/**
	 * Case Converter
	 * @param name
	 * @return case
	 */
	public static String tableColumnCaseConverter(String name) {
		if(TABLE_COLUMN_CASE  == CASE_TYPE.NORMAL) {}
		else if(TABLE_COLUMN_CASE  == CASE_TYPE.LOWERCASE) {
			name = name.toLowerCase();
		}else if(TABLE_COLUMN_CASE  == CASE_TYPE.UPPERCASE) {
			name = name.toUpperCase();
		}
		return name;
	}
}
