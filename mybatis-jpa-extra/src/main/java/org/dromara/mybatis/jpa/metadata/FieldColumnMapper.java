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
 

package org.dromara.mybatis.jpa.metadata;

import java.lang.reflect.Field;

import org.dromara.mybatis.jpa.annotations.ColumnDefault;
import org.dromara.mybatis.jpa.annotations.Encrypted;
import org.dromara.mybatis.jpa.annotations.PartitionKey;
import org.dromara.mybatis.jpa.annotations.SoftDelete;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Temporal;

public class FieldColumnMapper {
	
	private Field 			field;
	/**
	 * class field name
	 */
	private String 			fieldName;
	
	/**
	 * class field type
	 */
	private String 			fieldType;
	
	/**
	 * table column name
	 */
	private String 			columnName;
	
	private boolean 		idColumn	=	false;
	
	private boolean 		generated	=	false;
	
	private boolean 		logicDelete	=	false;
	
	private boolean         encrypted	=	false;
	
	private GeneratedValue 	generatedValue;
	
	private	Column 			columnAnnotation;
	
	private Temporal 		temporalAnnotation;
	
	private ColumnDefault   columnDefault;
	
	private PartitionKey    partitionKey;
	
	private SoftDelete     	softDelete;
	
	private Encrypted     	encryptedAnnotation;
	
	public FieldColumnMapper() {
		
	}
	
	public FieldColumnMapper(Field field,String fieldName, String fieldType, String columnName) {
		super();
		this.field = field;
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.columnName = columnName;
	}
	
	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public boolean isIdColumn() {
		return idColumn;
	}

	public void setIdColumn(boolean idColumn) {
		this.idColumn = idColumn;
	}

	public GeneratedValue getGeneratedValue() {
		return generatedValue;
	}

	public void setGeneratedValue(GeneratedValue generatedValue) {
		this.generatedValue = generatedValue;
	}

	public Column getColumnAnnotation() {
		return columnAnnotation;
	}

	public void setColumnAnnotation(Column columnAnnotation) {
		this.columnAnnotation = columnAnnotation;
	}
	
	public Temporal getTemporalAnnotation() {
		return temporalAnnotation;
	}

	public void setTemporalAnnotation(Temporal temporalAnnotation) {
		this.temporalAnnotation = temporalAnnotation;
	}
	
	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}
	
	public ColumnDefault getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(ColumnDefault columnDefault) {
		this.columnDefault = columnDefault;
	}
	
	public PartitionKey getPartitionKey() {
		return partitionKey;
	}

	public void setPartitionKey(PartitionKey partitionKey) {
		this.partitionKey = partitionKey;
	}

	public boolean isLogicDelete() {
		return logicDelete;
	}

	public void setLogicDelete(boolean logicDelete) {
		this.logicDelete = logicDelete;
	}

	public SoftDelete getSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(SoftDelete softDelete) {
		this.softDelete = softDelete;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public Encrypted getEncryptedAnnotation() {
		return encryptedAnnotation;
	}

	public void setEncryptedAnnotation(Encrypted encryptedAnnotation) {
		this.encryptedAnnotation = encryptedAnnotation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FieldColumnMapper [fieldName=");
		builder.append(fieldName);
		builder.append(", fieldType=");
		builder.append(fieldType);
		builder.append(", columnName=");
		builder.append(columnName);
		builder.append(", idColumn=");
		builder.append(idColumn);
		builder.append(", generated=");
		builder.append(generated);
		builder.append(", logicDelete=");
		builder.append(logicDelete);
		builder.append(", encrypted=");
		builder.append(encrypted);
		builder.append(", generatedValue=");
		builder.append(generatedValue);
		builder.append(", columnAnnotation=");
		builder.append(columnAnnotation);
		builder.append(", temporalAnnotation=");
		builder.append(temporalAnnotation);
		builder.append(", columnDefault=");
		builder.append(columnDefault);
		builder.append(", partitionKey=");
		builder.append(partitionKey);
		builder.append(", softDelete=");
		builder.append(softDelete);
		builder.append(", encryptedAnnotation=");
		builder.append(encryptedAnnotation);
		builder.append("]");
		return builder.toString();
	}

}
