package org.dromara.mybatis.jpa.meta;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

public class TableMetadata {
	private static final Logger logger 	= 	LoggerFactory.getLogger(TableMetadata.class);
	
	static ConcurrentMap<String, String> tableNameMap 	= 	new ConcurrentHashMap<>();
	
	/**
	 * build select from entity Class
	 * @param entityClass
	 * @return select columns  from table name sel_tmp_table
	 */
	public static SQL buildSelect(Class<?> entityClass) {
		FieldMetadata.buildColumnList(entityClass);
		return new SQL().SELECT(FieldMetadata.selectColumnMapper(entityClass))
				.FROM(TableMetadata.getTableName(entityClass) + " sel_tmp_table ");
	}
	
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
		
		tableName = MapperMetadata.tableColumnCaseConverter(tableName);
		
		tableName = MapperMetadata.tableColumnEscape(tableName);
		
		tableNameMap.put(entityClassName,tableName);
		logger.trace("Table Name {}" , tableName);
		return tableName;
	}
	

}
