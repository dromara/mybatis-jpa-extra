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

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.id.IdStrategy;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.dromara.mybatis.jpa.metadata.FieldColumnMapper;
import org.dromara.mybatis.jpa.metadata.FieldMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * @author Crystal.Sea
 *
 */
public class InsertProvider <T extends JpaEntity>{	
	static final Logger logger 	= 	LoggerFactory.getLogger(InsertProvider.class);
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		FieldMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = FieldMetadata.getFieldsMap(entity.getClass());
		
		SQL sql = new SQL().INSERT_INTO(TableMetadata.getTableName(entity.getClass()));
		if(logger.isTraceEnabled()) {
			for (FieldColumnMapper fieldColumnMapper : listFields) {
				logger.trace("fieldColumnMapper {} ",fieldColumnMapper);
			}
		}
		for (FieldColumnMapper fieldColumnMapper : listFields) {
			String columnName = fieldColumnMapper.getColumnName();
			String fieldName = fieldColumnMapper.getFieldName();
			String fieldType = fieldColumnMapper.getFieldType();
			Object fieldValue = BeanUtil.getValue(entity, fieldName);
			boolean isFieldValueNull = BeanUtil.isFieldBlank(fieldValue);
			
			if(fieldColumnMapper.getColumnAnnotation().insertable()) {
				if(fieldColumnMapper.getColumnDefault() != null) {
					sql.VALUES(columnName,"" + fieldColumnMapper.getColumnDefault().value() + "");
				}else if(fieldColumnMapper.isLogicDelete()) {
					sql.VALUES(columnName,"'" + fieldColumnMapper.getSoftDelete().value() + "'");
				}else if(isFieldValueNull && !fieldColumnMapper.isGenerated()) {
					//skip null field value
					if(logger.isTraceEnabled()) {
						logger.trace("Field {} , Type {} , Value is null , Skiped ",
							String.format(MapperMetadata.LOG_FORMAT, fieldName), String.format(MapperMetadata.LOG_FORMAT, fieldType));
					}
				}else {
					if(logger.isTraceEnabled()) {
						logger.trace("Field {} , Type {} , Value {}",
							String.format(MapperMetadata.LOG_FORMAT, fieldName), String.format(MapperMetadata.LOG_FORMAT, fieldType),fieldValue);
					}
					if(fieldColumnMapper.isGenerated() && fieldColumnMapper.getTemporalAnnotation() != null) {
						sql.VALUES(columnName,"'" + DateConverter.convert(entity, fieldColumnMapper,false) + "'");
					}else if((fieldColumnMapper.isGenerated()) && isFieldValueNull) {
						generatedValue(sql , entity , fieldColumnMapper);
					}else {
						sql.VALUES(columnName,"#{%s}".formatted(fieldName));
					}
				}
			}
		}
		logger.trace("Insert SQL : \n{}" , sql);
		return sql.toString();
	}
	
	private void  generatedValue(SQL sql , T entity , FieldColumnMapper fieldColumnMapper) {
		//have @GeneratedValue and (value is null or eq "")
		GeneratedValue generatedValue = fieldColumnMapper.getGeneratedValue();
		if(generatedValue == null || generatedValue.strategy() == GenerationType.AUTO) {
			String genValue = "";
			if(generatedValue == null ) {
				genValue = IdentifierGeneratorFactory.generate(IdStrategy.DEFAULT);
			}else if(IdentifierGeneratorFactory.exists(generatedValue.generator())) {
				genValue = IdentifierGeneratorFactory.generate(generatedValue.generator());
			}else {
				genValue = IdentifierGeneratorFactory.generate(IdStrategy.DEFAULT);
			}
			BeanUtil.set(entity, fieldColumnMapper.getFieldName(),genValue);
			sql.VALUES(fieldColumnMapper.getColumnName(),"#{%s}".formatted(fieldColumnMapper.getFieldName()));
		}else if(generatedValue.strategy()==GenerationType.SEQUENCE){
			sql.VALUES(fieldColumnMapper.getColumnName(),generatedValue.generator()+".nextval");
		}else if(generatedValue.strategy()==GenerationType.IDENTITY){
			//skip
		}else if(generatedValue.strategy()==GenerationType.TABLE){
			//skip
		}
	}
	
}
