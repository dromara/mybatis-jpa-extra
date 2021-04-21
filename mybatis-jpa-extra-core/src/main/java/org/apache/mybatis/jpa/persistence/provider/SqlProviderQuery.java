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
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(MapperMetadata.getTableName(entityClass));  
        sql.WHERE(idFieldColumnMapper.getColumnName()+" = #{"+idFieldColumnMapper.getFieldName()+"}");  
        String getSql=sql.toString(); 
        _logger.trace("Get SQL \n"+getSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.GET_SQL,getSql);
        return getSql;  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		MapperMetadata.buildColumnList(entityClass);
		if (MapperMetadata.sqlsMap.containsKey(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL)) {
			return MapperMetadata.sqlsMap.get(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL);
		}
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(MapperMetadata.getTableName(entityClass));  
        String findAllSql=sql.toString(); 
        _logger.trace("Find All SQL \n"+findAllSql);
        MapperMetadata.sqlsMap.put(MapperMetadata.getTableName(entityClass) + SQL_TYPE.FINDALL_SQL,findAllSql);
        return findAllSql;  
    }

	public String execute(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());
		SQL sql=new SQL();
		sql.SELECT(" * ");  
        sql.FROM(MapperMetadata.getTableName(entity.getClass()));  
        
        for(FieldColumnMapper fieldColumnMapper  : MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName())) {
        	if((
					fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
					||fieldColumnMapper.getFieldType().startsWith("byte")
				)
        		&&BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null) {
				//skip null field value
			}else {
				sql.WHERE(fieldColumnMapper.getColumnName() + "=#{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		
		return sql.toString();
	}
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String executePageResultsCount(T entity) {
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
		
		if(countSql.toString().toLowerCase().indexOf("distinct")>0) {
			sql.append("select count(1) countrows_ from (").append(countSql).append(" ) count_table_");
		}else {
			sql.append("select count(1) countrows_ ").append(
					countSql.substring(countSql.toString().toLowerCase().indexOf("from"))
			);
		}
		//删除缓存
		JpaBaseService.pageResultsBoundSqlCache.remove(pagination.getPageResultSelectUUID());
		_logger.trace("Count SQL : \n" + sql);
		return sql.toString();
	}
	
}
