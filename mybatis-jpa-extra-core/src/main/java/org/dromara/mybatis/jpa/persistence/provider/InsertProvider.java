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
package org.dromara.mybatis.jpa.persistence.provider;

import java.util.List;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.id.IdStrategy;
import org.dromara.mybatis.jpa.persistence.FieldColumnMapper;
import org.dromara.mybatis.jpa.persistence.JpaBaseEntity;
import org.dromara.mybatis.jpa.persistence.MapperMetadata;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * @author Crystal.Sea
 *
 */
public class InsertProvider <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(InsertProvider.class);
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		MapperMetadata.buildColumnList(entity.getClass());
		List<FieldColumnMapper> listFields = MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName());
		
		SQL sql = new SQL().INSERT_INTO(MapperMetadata.getTableName(entity.getClass()));
		
		for (int i = 0; i < listFields.size(); i++) {
			FieldColumnMapper fieldColumnMapper=listFields.get(i);
			if(
				(
					fieldColumnMapper.getFieldType().equalsIgnoreCase("String")
					||fieldColumnMapper.getFieldType().startsWith("byte")
				)
				&& BeanUtil.getValue(entity, fieldColumnMapper.getFieldName())==null
				&& fieldColumnMapper.getGeneratedValue() == null) {
				//skip null field value
				_logger.trace("skip  field value is null ");
			}else {
				//have GeneratedValue and (value is null or eq "")
				if(fieldColumnMapper.getGeneratedValue() != null 
						&& (
								BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == null
								|| BeanUtil.get(entity, fieldColumnMapper.getFieldName()) == ""
						)) {
					GeneratedValue generatedValue = listFields.get(i).getGeneratedValue();
					if(generatedValue.strategy() == GenerationType.AUTO) {
						if(MapperMetadata.identifierGeneratorFactory.getGeneratorStrategyMap()
								.containsKey(generatedValue.generator().toLowerCase())) {
							BeanUtil.set(entity, 
									fieldColumnMapper.getFieldName(), 
									MapperMetadata.identifierGeneratorFactory.generate(generatedValue.generator().toLowerCase()));
							sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
						}else {
							BeanUtil.set(entity, 
									fieldColumnMapper.getFieldName(), 
									MapperMetadata.identifierGeneratorFactory.generate(IdStrategy.DEFAULT));
							sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
						}
					}else if(generatedValue.strategy()==GenerationType.SEQUENCE){
						sql.VALUES(fieldColumnMapper.getColumnName(),generatedValue.generator()+".nextval");
					}else if(generatedValue.strategy()==GenerationType.IDENTITY){
						//skip
					}else if(generatedValue.strategy()==GenerationType.TABLE){
						//skip
					}
				}else {
					sql.VALUES(fieldColumnMapper.getColumnName(),"#{" + fieldColumnMapper.getFieldName() + "}");
				}
			}
		}
		_logger.trace("Insert SQL : \n" + sql);
		return sql.toString();
	}
	
}
