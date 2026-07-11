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
package org.dromara.mybatis.jpa.provider.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.exceptions.MybatisJpaException;
import org.dromara.mybatis.jpa.id.IdentifierStrategy;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
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
public class InsertProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(InsertProvider.class);
    
    /**
     * @param entity
     * @return insert sql String
     */
    public String insert(T entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        List<ColumnMapper> listFields = ColumnMetadata.buildColumnMapper(entity.getClass());
        SQL sql = new SQL().INSERT_INTO(TableMetadata.getTableName(entity.getClass()));
        //优化日志打印，避免集合遍历时产生不必要的性能开销
        if(logger.isTraceEnabled()) {
            for (ColumnMapper fieldColumnMapper : listFields) {
                logger.trace("fieldColumnMapper {} ",fieldColumnMapper);
            }
        }
        for (ColumnMapper fieldColumnMapper : listFields) {
            if(!fieldColumnMapper.getColumnAnnotation().insertable()) {
                continue;
            }
            String columnName = fieldColumnMapper.getColumn();
            String fieldName = fieldColumnMapper.getField();
            String fieldType = fieldColumnMapper.getFieldType();
            Object fieldValue = BeanUtil.getValue(entity, fieldName);
            boolean isFieldValueNull = Objects.isNull(fieldValue);
            //自动生成字段 (如 ID, 创建时间等)
            if(fieldColumnMapper.isGenerated()) {//自动生成字段值
                	if(isFieldValueNull) {//空值
                		if(fieldColumnMapper.isIdColumn()){//id
                        generatedValue(sql , entity , fieldColumnMapper);
                    }else if(DateConverter.isDateType(fieldType)) {//日期类型
                        DateConverter.convert(entity, fieldColumnMapper,false);
                        sql.VALUES(columnName,"#{%s}".formatted(fieldName));
                    } 
                		fieldValue = BeanUtil.getValue(entity, fieldName);
                	}else {
                		sql.VALUES(columnName,"#{%s}".formatted(fieldName));
                	}
                	continue;
            }
            //逻辑删除字段 (仅在值为空时设置默认值)
            if(fieldColumnMapper.isLogicDelete()) {//逻辑删除字段默认值
                sql.VALUES(columnName,"'" + fieldColumnMapper.getSoftDelete().value() + "'");
                continue;
            }
            //字段值为空，且存默认值
            if(isFieldValueNull && fieldColumnMapper.getColumnDefault() != null) {
                //字段值为空，且标注默认值
                sql.VALUES(columnName,"" + fieldColumnMapper.getColumnDefault().value() + "");
                continue;
            }
            // 字段值不为空，正常插入
            if(!isFieldValueNull) {
                if(logger.isTraceEnabled()) {
                    logger.trace("Field {} , Type {} , Value {}",
                        String.format(ConstMetadata.LOG_FORMAT, fieldName), String.format(ConstMetadata.LOG_FORMAT, fieldType),fieldValue);
                }
                sql.VALUES(columnName,"#{%s}".formatted(fieldName));
                continue;
            }
        	    //skip null field value
            if(logger.isTraceEnabled()) {
                logger.trace("Field {} , Type {} , Value is null , skiped ",
                    String.format(ConstMetadata.LOG_FORMAT, fieldName), String.format(ConstMetadata.LOG_FORMAT, fieldType));
            } 
        }
        logger.trace("Insert SQL : \n{}" , sql);
        return sql.toString();
    }
    
    private void  generatedValue(SQL sql , T entity , ColumnMapper fieldColumnMapper) {
        //have @GeneratedValue and (value is null or eq "")
        GeneratedValue generatedValue = fieldColumnMapper.getGeneratedValue();
        if(generatedValue == null || generatedValue.strategy() == GenerationType.AUTO) {
            assignGeneratedIdentifier(entity, fieldColumnMapper, generatedValue);
            sql.VALUES(fieldColumnMapper.getColumn(),"#{%s}".formatted(fieldColumnMapper.getField()));
        }else if(generatedValue.strategy()==GenerationType.SEQUENCE){
            sql.VALUES(fieldColumnMapper.getColumn(),generatedValue.generator()+".nextval");
        }else if(generatedValue.strategy()==GenerationType.IDENTITY){
            //skip
        }else if(generatedValue.strategy()==GenerationType.TABLE){
            //skip
        }
    }
    
    /**
     * @param List<T> entity
     * @return insert sql script
     */
    public String insertBatch(List<T> listEntity) {
        //使用 CollectionUtils 简化判空，去除冗余的 size() > 0
        if(CollectionUtils.isEmpty(listEntity)) {
            throw new MybatisJpaException("insert List<T> can not been null ! ");
        }
        // 2. 安全获取 Class，避免 listEntity.get(0) 为 null 导致的警告和 NPE
        T firstEntity = listEntity.get(0);
        if (firstEntity == null) {
            throw new MybatisJpaException("The first element in the insert list cannot be null!");
        }
        Class<?> entityClass = firstEntity.getClass();
    	
        List<ColumnMapper> listFields = ColumnMetadata.buildColumnMapper(entityClass);
        
        SQL sql = new SQL().INSERT_INTO(TableMetadata.getTableName(entityClass));
        StringJoiner valueJoiner = new StringJoiner(",");
        for (ColumnMapper fieldColumnMapper : listFields) {
            if(fieldColumnMapper.getColumnAnnotation().insertable()) {
                	sql.INTO_COLUMNS(fieldColumnMapper.getColumn());
    	            valueJoiner.add("#{entity."+fieldColumnMapper.getField()+"}");
            }
            logger.trace("fieldColumnMapper {} ",fieldColumnMapper);
        }
        
        StringBuilder insertSql = new StringBuilder("");
        insertSql.append("<script>").append("\n")
		        .append(sql.toString()).append("\n")
		        .append(" VALUES ").append("\n")
		        .append(" <foreach collection =\"arg0\" item=\"entity\" separator =\",\">").append("\n")
		        .append("  (")
		        .append(valueJoiner.toString()) 
		        .append("  )").append("\n")
		        .append(" </foreach>").append("\n")
		        .append("</script>");
        logger.trace("Insert SQL : \n{}" , insertSql);
        
        for(T entity : listEntity) {
    	        for (ColumnMapper fieldColumnMapper : listFields) {
    	            //仅处理可插入的字段
    	            if(!fieldColumnMapper.getColumnAnnotation().insertable()) {
    	                continue;
    	            }
    	            String fieldName = fieldColumnMapper.getField();
    	            String fieldType = fieldColumnMapper.getFieldType();
    	            Object fieldValue = BeanUtil.getValue(entity, fieldName);
    	            boolean isFieldValueNull = Objects.isNull(fieldValue);
    	            if(fieldColumnMapper.isGenerated()) {//自动生成字段值
    	                	if(isFieldValueNull) {//空值
    	                		if(fieldColumnMapper.isIdColumn()){//id
    	                			batchGeneratedValue( entity , fieldColumnMapper);
	                    }else if(DateConverter.isDateType(fieldType)) {//日期类型
	                        DateConverter.convert(entity, fieldColumnMapper,false);
	                    } 
    	                	}
    	            }else if(fieldColumnMapper.isLogicDelete()) {//逻辑删除字段默认值
    	                BeanUtil.set(entity, fieldColumnMapper.getField(),fieldColumnMapper.getSoftDelete().value());
    	            }
    	        }
        }
        return insertSql.toString();        
    }
    
    private void  batchGeneratedValue( T entity , ColumnMapper fieldColumnMapper) {
        GeneratedValue generatedValue = fieldColumnMapper.getGeneratedValue();
        if(generatedValue == null || generatedValue.strategy() == GenerationType.AUTO) {
            assignGeneratedIdentifier(entity, fieldColumnMapper, generatedValue);
        }
    }

    private void assignGeneratedIdentifier(T entity, ColumnMapper fieldColumnMapper, GeneratedValue generatedValue) {
        String genValue = generateIdentifierValue(generatedValue);
        if(fieldColumnMapper.getFieldType().equalsIgnoreCase("String")) {
            BeanUtil.set(entity, fieldColumnMapper.getField(),genValue);
        }else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("Integer")) {
            BeanUtil.set(entity, fieldColumnMapper.getField(),Integer.valueOf(genValue));
        }else if(fieldColumnMapper.getFieldType().equalsIgnoreCase("Long")) {
            BeanUtil.set(entity, fieldColumnMapper.getField(),Long.valueOf(genValue));
        }
    }

    private String generateIdentifierValue(GeneratedValue generatedValue) {
        if(generatedValue == null) {
            return IdentifierGeneratorFactory.generate(IdentifierStrategy.DEFAULT);
        }
        if(IdentifierGeneratorFactory.exists(generatedValue.generator())) {
            return IdentifierGeneratorFactory.generate(generatedValue.generator());
        }
        return IdentifierGeneratorFactory.generate(IdentifierStrategy.DEFAULT);
    }
    
}
