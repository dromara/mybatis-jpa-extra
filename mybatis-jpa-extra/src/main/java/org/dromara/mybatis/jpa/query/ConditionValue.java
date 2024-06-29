package org.dromara.mybatis.jpa.query;

public class ConditionValue {

	public static String valueOf(Object value) {
		StringBuffer conditionString = new StringBuffer("");
		String valueType = value.getClass().getSimpleName().toLowerCase();
		if (valueType.equals("string") 
				|| valueType.equals("char")) {
			conditionString.append("'").append(String.valueOf(value).replace("'", "")).append("'");
		} else if (valueType.equals("int") 
				|| valueType.equals("long") 
				|| valueType.equals("integer")
				|| valueType.equals("float") 
				|| valueType.equals("double")) {
			conditionString.append("").append(value).append("");
		} else {
			conditionString.append("'").append(String.valueOf(value).replace("'", "")).append("'");
		}
		return conditionString.toString();
	}
	
}