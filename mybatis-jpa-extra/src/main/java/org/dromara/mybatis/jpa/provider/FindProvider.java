/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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
package org.dromara.mybatis.jpa.provider;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class FindProvider <T extends JpaEntity>{
	static final Logger logger 	= 	LoggerFactory.getLogger(FindProvider.class);
	
	public String findAll(Map<String, Object>  parametersMap) {  
		Class<?> entityClass=(Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		FieldMetadata.buildColumnList(entityClass);
		
		SQL sql=  TableMetadata.buildSelect(entityClass);
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
			sql.WHERE(" %s = '%s'"
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
        String findAllSql = sql.toString(); 
        logger.trace("Find All SQL \n{}" , findAllSql);
        return findAllSql;  
    }
	
	public String find(Map<String, Object>  parametersMap) throws Exception {  
		Class<?> entityClass = (Class<?>) parametersMap.get(MapperMetadata.ENTITY_CLASS);
		Object[] args 	 = (Object[]) parametersMap.get(MapperMetadata.QUERY_ARGS);
		int[] argTypes 	 = (int[]) parametersMap.get(MapperMetadata.QUERY_ARGTYPES);
		String filterSql = parametersMap.get(MapperMetadata.QUERY_FILTER).toString().trim();
		
		FieldMetadata.buildColumnList(entityClass);
		
		if(filterSql.toLowerCase().startsWith("where")) {
			filterSql = filterSql.substring(5);
		}
		
		if(args == null || args.length == 0) {
			filterSql = StrUtils.lineBreak2Blank(filterSql);
		}else {
			int countMatches = StringUtils.countMatches(filterSql, "?");
			if(args.length < countMatches) {
				logger.error("args length {} < parameter placeholder {}" ,  countMatches,args.length);
				throw new Exception("args length < parameter placeholder");
			}
			
			String[] filterSqls  = filterSql.split("\\?");
			StringBuffer sqlBuffer = new StringBuffer("");
			for(int i = 0 ;i < filterSqls.length ; i++){
				logger.trace("Find filterSqls[{}] = {}" , i , filterSqls[i]);
				if(i < args.length) {
					logger.trace("Find args[{}] {}" , i, args[i]);
					if( argTypes[i] == Types.VARCHAR 
							||argTypes[i] == Types.NVARCHAR 
							||argTypes[i] == Types.CHAR
							||argTypes[i] == Types.NCHAR
							||argTypes[i] == Types.LONGVARCHAR 
							||argTypes[i] == Types.LONGNVARCHAR) {
						sqlBuffer
							.append(filterSqls[i])
							.append("'")
							.append(SafeValueHandler.valueOf(args[i].toString()))
							.append("'");
					}else {
						sqlBuffer
						.append(filterSqls[i])
						.append(args[i]);
					}
				}else {
					sqlBuffer
					.append(filterSqls[i]);
				}
				logger.trace("Find append {} time SQL [{}]" ,i + 1, sqlBuffer);
			}
			filterSql = StrUtils.lineBreak2Blank(sqlBuffer.toString());
		}
		
		SQL sql = TableMetadata.buildSelect(entityClass).WHERE("( " + filterSql +" )");
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(entityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
			sql.WHERE(" %s = '%s'"
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
        String findSql = sql.toString(); 
        logger.trace("Find SQL \n{}" , findSql);

        return findSql;  
    }
	
	@SuppressWarnings("unchecked")
	public String findByIds(Map<String, Object>  parametersMap) { 
		Class<?> parameterEntityClass = (Class<?>)parametersMap.get(MapperMetadata.ENTITY_CLASS);
		FieldMetadata.buildColumnList(parameterEntityClass);
		List <String> parameterIds = (List<String>)parametersMap.get(MapperMetadata.PARAMETER_ID_LIST);
		
		StringBuffer keyValues = new StringBuffer();
		for(String value : parameterIds) {
			if(value.trim().length() > 0) {
				keyValues.append(",'").append(SafeValueHandler.valueOf(value)).append("'");
			}
		}
		logger.trace("find by id {}" , keyValues);
		
		//remove ';'
		String idsValues = keyValues.substring(1).replace(";", "");
		String partitionKeyValue = (String) parametersMap.get(MapperMetadata.PARAMETER_PARTITION_KEY);
		FieldColumnMapper partitionKeyColumnMapper = FieldMetadata.getPartitionKey(parameterEntityClass);
		FieldColumnMapper idFieldColumnMapper = FieldMetadata.getIdColumn(parameterEntityClass);
		
		SQL sql = TableMetadata.buildSelect(parameterEntityClass);
		
		if(partitionKeyColumnMapper != null && partitionKeyValue != null) {
			sql.WHERE("%s = #{%s} and %s  in ( %s )"
					.formatted(
							partitionKeyColumnMapper.getColumnName() ,
							partitionKeyValue,
							idFieldColumnMapper.getColumnName(),
							idsValues)
        			);  
		}else {
			sql.WHERE(" %s in ( %s )".formatted(idFieldColumnMapper.getColumnName(),idsValues));  
		}
		
		FieldColumnMapper logicColumnMapper = FieldMetadata.getLogicColumn(parameterEntityClass);
		if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
			sql.WHERE(" %s = '%s'"
					.formatted(
							logicColumnMapper.getColumnName(),
							logicColumnMapper.getSoftDelete().value())
					);
		}
		
        String findByIdsSql = sql.toString(); 
        logger.trace("Find by ids SQL \n{}" , findByIdsSql);
        return findByIdsSql;  
    } 
	
}
