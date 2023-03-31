package org.dromara.mybatis.jpa.query;

public class Condition {

	Operator expression;
	
	String column;
	
	Object value;
	
	Object value2;

	public Condition() {
		
	}

	public Condition(Operator expression, String column, Object value) {
		super();
		this.expression = expression;
		this.column = column;
		this.value = value;
	}
	
	
	public Condition(Operator expression, String column, Object value,Object value2) {
		super();
		this.expression = expression;
		this.column = column;
		this.value = value;
		this.value2 =value2;
	}
	
	public Operator getExpression() {
		return expression;
	}

	public void setExpression(Operator expression) {
		this.expression = expression;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}

	@Override
	public String toString() {
		return "Condition [expression=" + expression + ", column=" + column + ", value=" + value + ", value2=" + value2
				+ "]";
	}

}
