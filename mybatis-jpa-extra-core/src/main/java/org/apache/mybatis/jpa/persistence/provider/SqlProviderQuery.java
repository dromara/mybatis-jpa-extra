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
package org.apache.mybatis.jpa.persistence.provider;

import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.mybatis.jpa.PageResultsSqlCache;
import org.apache.mybatis.jpa.persistence.FieldColumnMapper;
import org.apache.mybatis.jpa.persistence.JpaBaseEntity;
import org.apache.mybatis.jpa.persistence.JpaBaseService;
import org.apache.mybatis.jpa.persistence.JpaPagination;
import org.apache.mybatis.jpa.persistence.MapperMetadata;
import org.apache.mybatis.jpa.persistence.MapperMetadata.SQL_TYPE;
import org.apache.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class SqlProviderQuery <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(SqlProviderQuery.class);


	public String get(Map<String, Object>  parametersMap) {
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL);
		}
		
		FieldColumnMapper idFieldColumnMapper=MapperMetadata.getIdColumn(entityClass.getSimpleName());
		
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entityClass)) 
        	.FROM(MapperMetadata.getTableName(entityClass)+" sel_tmp_table ")
        	.WHERE(idFieldColumnMapper.getColumnName() 
        			+ " = #{"+idFieldColumnMapper.getFieldName() + "}");  
		
        String getSql = sql.toString(); 
        _logger.trace("Get SQL \n" + getSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL,getSql);
        return getSql;  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL);
		}
		
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entityClass))  
        	.FROM(MapperMetadata.getTableName(entityClass)+" sel_tmp_table ");  
		
        String findAllSql = sql.toString(); 
        _logger.trace("Find All SQL \n" + findAllSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL,findAllSql);
        return findAllSql;  
    }

	public String execute(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());
		SQL sql=new SQL()
			.SELECT(selectColumnMapper(entity.getClass())) 
        	.FROM(MapperMetadata.getTableName(entity.getClass())+" sel_tmp_table ");  
        
        for(FieldColumnMapper fieldColumnMapper  : MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName())) {
        	 String fieldValue = BeanUtil.getValue(entity, fieldColumnMapper.getFieldName());
        	 String fieldType=fieldColumnMapper.getFieldType().toLowerCase();
        	 
        	 _logger.trace("ColumnName "+fieldColumnMapper.getColumnName()
        	 				+" , FieldType "+fieldType
        	 				+" , value " + fieldValue);
        	
        	if(fieldValue==null
        		||(fieldType.equals("string")&&fieldValue.equals(""))
        		||(fieldType.startsWith("byte")&&fieldValue==null)
        		||(fieldType.equals("int")&&fieldValue.equals("0"))
        		||(fieldType.equals("long")&&fieldValue.equals("0"))
        		||(fieldType.equals("integer")&&fieldValue.equals("0"))
        		||(fieldType.equals("float")&&fieldValue.equals("0.0"))
        		||(fieldType.equals("double")&&fieldValue.equals("0.0"))
        		) {
				//skip null field value
			}else {
				sql.WHERE(fieldColumnMapper.getColumnName() + "=#{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		
		return sql.toString();
	}
	
	public String selectColumnMapper(Class<?> entityClass) {
		StringBuffer selectColumn =new StringBuffer("sel_tmp_table.* ");
		int columnCount = 0;
		for(FieldColumnMapper fieldColumnMapper  : MapperMetadata.fieldsMap.get(entityClass.getSimpleName())) {
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
	 * @param entity
	 * @return insert sql String
	 */
	public String executePageResultsCount(T entity) {
		JpaPagination pagination=(JpaPagination)entity;
		//获取缓存数据
		PageResultsSqlCache pageResultsSqlCache = 
				JpaBaseService.pageResultsBoundSqlCache.getIfPresent(pagination.getPageResultSelectUUID());
		//多个空格 tab 替换成1个空格
		String selectSql = pageResultsSqlCache.getSql()
								.replaceAll("\r\n+", " \n")
								.replaceAll("\n+", " \n")
								.replaceAll("\t", " ")
								.replaceAll(" +"," ");
		BoundSql boundSql = (BoundSql)pageResultsSqlCache.getBoundSql();
		_logger.trace("Count original SQL  :\n{}" , selectSql);
		
		StringBuffer sql = new StringBuffer(SqlSyntax.SELECT +" "+ SqlSyntax.Functions.COUNT_ONE +" countrows_ ");
		StringBuffer countSql = new StringBuffer();
		
		if(boundSql.getParameterMappings() == null ||boundSql.getParameterMappings().isEmpty()) {
			countSql.append(selectSql);
		}else {
			for (ParameterMapping parameterMapping:boundSql.getParameterMappings()) {
				countSql.append(selectSql.substring(0, selectSql.indexOf("?")));
				countSql.append("#{"+parameterMapping.getProperty()+"}");
				selectSql = selectSql.substring(selectSql.indexOf("?")+1);
			}
			countSql.append(selectSql);
		}
		String countSqlLowerCase = countSql.toString().toLowerCase();
		_logger.trace("Count SQL LowerCase  :\n{}" , countSqlLowerCase);
		
		if(countSqlLowerCase.indexOf(SqlSyntax.DISTINCT + " ")>0 //去重
				||countSqlLowerCase.indexOf(" " + SqlSyntax.GROUPBY + " ")>0 //分组
				||countSqlLowerCase.indexOf(" " + SqlSyntax.HAVING + " ")>0 //聚合函数
				||(countSqlLowerCase.indexOf(" " + SqlSyntax.FROM + " ") 
						!= countSqlLowerCase.lastIndexOf(" " + SqlSyntax.FROM + " ")
				) //嵌套
				) {
			_logger.trace("Count SQL Complex ");
			sql.append(SqlSyntax.FROM).append(" (").append(countSql).append(" ) count_table_");
		}else {
			int fromIndex = countSqlLowerCase.indexOf(" " + SqlSyntax.FROM + " ");
			int orderByIndex = countSqlLowerCase.indexOf(" " + SqlSyntax.ORDERBY + " ");
			_logger.trace("Count SQL from Index {} , order by {}" ,fromIndex,orderByIndex);
			if(orderByIndex > -1) {
				sql.append(countSql.substring(fromIndex,orderByIndex));
			}else {
				sql.append(countSql.substring(fromIndex));
			}
		}
		//删除缓存
		JpaBaseService.pageResultsBoundSqlCache.invalidate(pagination.getPageResultSelectUUID());
		_logger.trace("Count SQL : \n{}" , sql);
		return sql.toString();
	}
	
}
