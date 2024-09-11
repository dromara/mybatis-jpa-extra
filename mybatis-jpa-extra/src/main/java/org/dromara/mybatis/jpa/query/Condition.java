/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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
		StringBuilder builder = new StringBuilder();
		builder.append("Condition [expression=");
		builder.append(expression);
		builder.append(", column=");
		builder.append(column);
		builder.append(", value=");
		builder.append(value);
		builder.append(", value2=");
		builder.append(value2);
		builder.append("]");
		return builder.toString();
	}

}
