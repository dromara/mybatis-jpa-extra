/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.mybatis.jpa.PageResultsSqlCache;
import org.apache.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.apache.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class MapperSqlProvider <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(MapperSqlProvider.class);
	
	public  static class SQL_TYPE{
		public static String 	GET_SQL							=	"_GET_SQL";
		public static String 	FINDALL_SQL						=	"_FINDALL_SQL";
		public static String 	REMOVE_SQL						=	"_REMOVE_SQL";
	}
	
	public static 			String ENTITY_CLASS					=	"entityClass";
	/**
	 * 表名和字段名
	 */
	private static boolean 	TABLE_COLUMN_UPCASE 					= 	true;
	private transient static ConcurrentMap<String, List<FieldColumnMapper>> 	fieldsMap 	= 	new ConcurrentHashMap<String, List<FieldColumnMapper>>();
	private transient static ConcurrentMap<String, String> 		sqlsMap 	= 	new ConcurrentHashMap<String, String>();
	private static IdentifierGeneratorFactory identifierGeneratorFactory=new IdentifierGeneratorFactory();
	
	public String get(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(ENTITY_CLASS);
		buildColumnList(entityClass);
		if (sqlsMap.containsKey(getTableName(entityClass) + SQL_TYPE.GET_SQL)) {
			return sqlsMap.get(getTableName(entityClass) + SQL_TYPE.GET_SQL);
		}
		
		FieldColumnMapper idFieldColumnMapper=getIdColumn(entityClass.getSimpleName());
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" = #{"+idFieldColumnMapper.getFieldName()+"}");  
        String getSql=sql.toString(); 
        _logger.trace("Get SQL \n"+getSql);
        sqlsMap.put(getTableName(entityClass) + SQL_TYPE.GET_SQL,getSql);
        return getSql;  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(ENTITY_CLASS);
		buildColumnList(entityClass);
		if (sqlsMap.containsKey(getTableName(entityClass) + SQL_TYPE.FINDALL_SQL)) {
			return sqlsMap.get(getTableName(entityClass) + SQL_TYPE.FINDALL_SQL);
		}
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(getTableName(entityClass));  
        String findAllSql=sql.toString(); 
        _logger.trace("Find All SQL \n"+findAllSql);
        sqlsMap.put(getTableName(entityClass) + SQL_TYPE.FINDALL_SQL,findAllSql);
        return findAllSql;  
    }
	
	public String remove(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(ENTITY_CLASS);
		buildColumnList(entityClass);
		if (sqlsMap.containsKey(getTableName(entityClass) + SQL_TYPE.REMOVE_SQL)) {
			return sqlsMap.get(getTableName(entityClass) + SQL_TYPE.REMOVE_SQL);
		}
		FieldColumnMapper idFieldColumnMapper=getIdColumn((entityClass).getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" = #{"+idFieldColumnMapper.getFieldName()+",javaType=string,jdbcType=VARCHAR}");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        sqlsMap.put(getTableName(entityClass) + SQL_TYPE.REMOVE_SQL,deleteSql);
        return deleteSql;  
    }  
	
	public String batchDelete(Map<String, Object>  parametersMap) { 
		Class<?> entityClass=(Class<?>)parametersMap.get(ENTITY_CLASS);
		buildColumnList(entityClass);
		
		@SuppressWarnings("unchecked")
		ArrayList <String> idValues=(ArrayList<String>)parametersMap.get("idList");
		String keyValue="";
		for(String value : idValues) {
			keyValue+=",'"+value+"'";
		}
		keyValue=keyValue.substring(1);
		FieldColumnMapper idFieldColumnMapper=getIdColumn(entityClass.getSimpleName());
		SQL sql=new SQL();
        sql.DELETE_FROM(getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" IN ( "+keyValue+" )");  
        String deleteSql=sql.toString(); 
        _logger.trace("Delete SQL \n"+deleteSql);
        sqlsMap.put(getTableName(entityClass) + SQL_TYPE.REMOVE_SQL,deleteSql);
        return deleteSql;  
    } 
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = fieldsMap.get(entity.getClass().getSimpleName());
		SQL sql = new SQL();
		sql.INSERT_INTO(getTableName(entity.getClass()));
		for (int i = 0; i < listFields.size(); i++) {
			FieldColumnMapper fieldColumnMapper=listFields.get(i);
			if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
					&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null
					&&fieldColumnMapper.getGeneratedValue()==null) {
				//skip null field value
			}else {
				if(fieldColumnMapper.getGeneratedValue()!=null) {
					GeneratedValue generatedValue=listFields.get(i).getGeneratedValue();
					if(generatedValue.strategy()==GenerationType.AUTO) {
						if(identifierGeneratorFactory.getGeneratorStrategyMap().containsKey(generatedValue.generator().toLowerCase())) {
							BeanUtil.set(entity, fieldColumnMapper.getFieldName(), identifierGeneratorFactory.generate(generatedValue.generator().toLowerCase()));
							sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
						}
					}else if(generatedValue.strategy()==GenerationType.SEQUENCE){
						sql.VALUES(fieldColumnMapper.getColumnName(),generatedValue.generator()+".NEXTVAL");
					}else if(generatedValue.strategy()==GenerationType.IDENTITY){
						//skip
					}else if(generatedValue.strategy()==GenerationType.TABLE){
						//TODO
					}
				}else {
					sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
				}
			}
		}
		_logger.trace("Insert SQL : \n" + sql);
		return sql.toString();
	}
	


	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		buildColumnList(entity.getClass());

		List<FieldColumnMapper> listFields = fieldsMap.get(entity.getClass().getSimpleName());
		SQL sql = new SQL();

		sql.UPDATE(getTableName(entity.getClass()));
		for (int i = 0; i < listFields.size(); i++) {
			FieldColumnMapper fieldColumnMapper=listFields.get(i);
			
			if (fieldColumnMapper.isIdColumn()) {
				continue;
			}
			
			if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null) {
				//skip null field value
			}else {
				sql.SET(fieldColumnMapper.getColumnName() + "=#{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		
		FieldColumnMapper idFieldColumnMapper=getIdColumn(entity.getClass().getSimpleName());
		sql.WHERE(idFieldColumnMapper.getColumnName() + "=#{"+idFieldColumnMapper.getFieldName()+"}");
		_logger.trace("Update SQL : \n" + sql);
		return sql.toString();
	}

	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String queryPageResultsCount(T entity) {
		JpaPagination pagination=(JpaPagination)entity;
		//获取缓存数据
		PageResultsSqlCache pageResultsSqlCache=JpaBaseService.pageResultsBoundSqlCache.get(pagination.getPageResultSelectUUID());
		String selectSql=pageResultsSqlCache.getSql();
		BoundSql boundSql=(BoundSql)pageResultsSqlCache.getBoundSql();
		
		StringBuffer sql=new StringBuffer();
		StringBuffer countSql=new StringBuffer();
		
		if(boundSql.getParameterMappings()==null ||boundSql.getParameterMappings().isEmpty()) {
			countSql.append(selectSql);
		}else {
			for (ParameterMapping parameterMapping:boundSql.getParameterMappings()) {
				countSql.append(selectSql.substring(0, selectSql.indexOf("?")));
				countSql.append("#{"+parameterMapping.getProperty()+"}");
				selectSql=selectSql.substring(selectSql.indexOf("?")+1);
			}
			countSql.append(selectSql);
		}
		
		if(countSql.toString().toUpperCase().indexOf("DISTINCT")>0) {
			sql.append("SELECT COUNT(1) COUNTROWS_ FROM (").append(countSql).append(" ) COUNT_TABLE_");
		}else {
			sql.append("SELECT COUNT(1) COUNTROWS_ ").append(
					countSql.substring(countSql.toString().toUpperCase().indexOf("FROM"))
			);
		}
		//删除缓存
		JpaBaseService.pageResultsBoundSqlCache.remove(pagination.getPageResultSelectUUID());
		_logger.trace("Count SQL : \n" + sql);
		return sql.toString();
	}
	
	public String query(T entity) {
		buildColumnList(entity.getClass());
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(getTableName(entity.getClass()));  
        
        for(FieldColumnMapper fieldColumnMapper  : fieldsMap.get(entity.getClass().getSimpleName())) {
        	if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null) {
				//skip null field value
			}else {
				sql.WHERE(fieldColumnMapper.getColumnName() + "=#{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		
		return sql.toString();
	}

	public String getTableName(Class<?> entityClass) {
		Table table = (Table)entityClass.getAnnotation(Table.class);
		String tablename = "";
		if (table != null) {
			tablename = table.name();
		} else {
			tablename = entityClass.getClass().getSimpleName();
		}
		return TABLE_COLUMN_UPCASE ? tablename.toUpperCase() : tablename;
	}
	
	public   FieldColumnMapper getIdColumn(String  classSimpleName) {
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

	public void buildColumnList(Class<?> entityClass) {
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
				
				Column columnAnnotation = (Column) field.getAnnotation(Column.class);
				if (columnAnnotation.name() != null && !columnAnnotation.name().equals("")) {
					fieldColumnMapper.setColumnName(columnAnnotation.name());
				} else {
					if (TABLE_COLUMN_UPCASE) {
						fieldColumnMapper.setColumnName(field.getName().toUpperCase());
					} else {
						fieldColumnMapper.setColumnName(field.getName());
					}
				}
				
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
