package org.apache.mybatis.jpa.persistence;

import javax.persistence.GeneratedValue;

public class FieldColumnMapper {
	//class
	private String fieldName;
	
	//class
	private String fieldType;
	
	//table
	private String columnName;
	
	private boolean idColumn	=	false;
	
	private GeneratedValue generatedValue;
	
	public FieldColumnMapper() {
		
	}
	
	public FieldColumnMapper(String fieldName, String fieldType, String columnName) {
		super();
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.columnName = columnName;
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

	@Override
	public String toString() {
		return "FieldColumnMapper [fieldName=" + fieldName + ", fieldType=" + fieldType + ", columnName=" + columnName
				+ ", idColumn=" + idColumn + ", generatedValue=" + generatedValue + "]";
	}

}
