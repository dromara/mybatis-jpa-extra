/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */

public class MapperMetadata <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(MapperMetadata.class);
	
	public  static class SQL_TYPE{
		public static String 	GET_SQL							=	"_GET_SQL";
		public static String 	FINDALL_SQL						=	"_FINDALL_SQL";
		public static String 	REMOVE_SQL						=	"_REMOVE_SQL";
	}
	
	public static 			String ENTITY_CLASS					=	"entityClass";
	/**
	 * 表名和字段名
	 */
	public static boolean 	TABLE_COLUMN_UPCASE 				=   true;
	public static boolean   TABLE_COLUMN_ESCAPE                 =  false;
	public static String    TABLE_COLUMN_ESCAPE_CHAR            =  "`";
	
	
	public transient static ConcurrentMap<String, List<FieldColumnMapper>> 	fieldsMap 	= 	new ConcurrentHashMap<String, List<FieldColumnMapper>>();
	public transient static ConcurrentMap<String, String> 		sqlsMap 	= 	new ConcurrentHashMap<String, String>();
	public static IdentifierGeneratorFactory identifierGeneratorFactory=new IdentifierGeneratorFactory();
	
	public static String getTableName(Class<?> entityClass) {
		Table table = (Table)entityClass.getAnnotation(Table.class);
		String tablename = "";
		if (table != null) {
			tablename = table.name();
		} else {
			tablename = entityClass.getClass().getSimpleName();
		}
		tablename = TABLE_COLUMN_UPCASE ? tablename.toUpperCase() : tablename;
		tablename = TABLE_COLUMN_ESCAPE ? TABLE_COLUMN_ESCAPE_CHAR + tablename + TABLE_COLUMN_ESCAPE_CHAR : tablename;
		return tablename;
	}
	
	public static  FieldColumnMapper getIdColumn(String  classSimpleName) {
		List<FieldColumnMapper> listFields = fieldsMap.get(classSimpleName);
		FieldColumnMapper idFieldColumnMapper=null;
		for (int i = 0; i < listFields.size(); i++) {
			if (listFields.get(i).isIdColumn()) {
				idFieldColumnMapper=listFields.get(i);
				break;
			}
		}
		return idFieldColumnMapper;
	}

	public static void buildColumnList(Class<?> entityClass) {
		if (fieldsMap.containsKey(entityClass.getSimpleName())) {
			//run one time
			return;
		}
		
		_logger.trace("entityClass " +entityClass);
		
		Field[] fields = entityClass.getDeclaredFields();
		List<FieldColumnMapper>fieldColumnMapperList=new ArrayList<FieldColumnMapper>(fields.length);

		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				FieldColumnMapper fieldColumnMapper=new FieldColumnMapper();
				fieldColumnMapper.setFieldName( field.getName());
				fieldColumnMapper.setFieldType(field.getType().getSimpleName());
				String columnName = "";
				Column columnAnnotation = (Column) field.getAnnotation(Column.class);
				if (columnAnnotation.name() != null && !columnAnnotation.name().equals("")) {
				    columnName = columnAnnotation.name();
				} else {
				    columnName = TABLE_COLUMN_UPCASE ? 
				            field.getName().toUpperCase() : field.getName();
				}
				
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
				}
				
				_logger.trace("FieldColumnMapper : " + fieldColumnMapper);
				fieldColumnMapperList.add(fieldColumnMapper);
			}
		}
		
		fieldsMap.put(entityClass.getSimpleName(), fieldColumnMapperList);
		_logger.debug("fieldsMap : " + fieldsMap);

	}
}
