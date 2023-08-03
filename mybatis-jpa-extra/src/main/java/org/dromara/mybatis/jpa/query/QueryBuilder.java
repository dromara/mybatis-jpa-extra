package org.dromara.mybatis.jpa.query;

public class QueryBuilder {
	
	public static String build(Query query) {
		StringBuffer conditionString = new StringBuffer("");
		for (Condition condition : query.getConditions()) {
			condition.setColumn(condition.getColumn().replaceAll("'", "").replaceAll(" ", "").replace(";", ""));
			if (condition.getExpression().equals(Operator.and) 
					|| condition.getExpression().equals(Operator.or)) {

				conditionString.append(" ").append(condition.getExpression().getOperator()).append(" ");

				if (condition.getValue() != null && condition.getValue() instanceof Query) {
					conditionString.append(" ( ").append(build((Query) condition.getValue())).append(" ) ");
				}

			} else if (condition.getExpression().equals(Operator.like)
					|| condition.getExpression().equals(Operator.notLike)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append("'%").append(condition.getValue().toString()).append("%'");

			} else if (condition.getExpression().equals(Operator.likeLeft)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append("'%").append(condition.getValue().toString()).append("'");

			} else if (condition.getExpression().equals(Operator.likeRight)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append("'").append(condition.getValue().toString()).append("%'");

			} else if (condition.getExpression().equals(Operator.eq) 
					|| condition.getExpression().equals(Operator.notEq)

					|| condition.getExpression().equals(Operator.gt) 
					|| condition.getExpression().equals(Operator.ge)

					|| condition.getExpression().equals(Operator.lt) 
					|| condition.getExpression().equals(Operator.le)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append(getConditionValue(condition.getValue()));

			} else if (condition.getExpression().equals(Operator.between)
					|| condition.getExpression().equals(Operator.notBetween)) {

				conditionString
						.append(" ( ").append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append(getConditionValue(condition.getValue()));
				conditionString.append(" and ");
				conditionString.append(getConditionValue(condition.getValue2()))
								.append(" ) ");

			} else if (condition.getExpression().equals(Operator.isNull)
					|| condition.getExpression().equals(Operator.isNotNull)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator());

			} else if (condition.getExpression().equals(Operator.in)
					|| condition.getExpression().equals(Operator.notIn)) {
				if (condition.getValue().getClass().isArray()) {
					conditionString.append(condition.getColumn()).append(" ")
							.append(condition.getExpression().getOperator());
					conditionString.append(" ( ");
					StringBuffer conditionArray = new StringBuffer();
					Object[] objects = (Object[]) condition.getValue();
					for (Object object : objects) {
						if (conditionArray.length() > 0) {
							conditionArray.append(" , ");
						}
						conditionArray.append(getConditionValue(object));
					}
					conditionString.append(conditionArray);
					conditionString.append(" ) ");
				}
			} else if (condition.getExpression().equals(Operator.condition)) {
				conditionString.append(condition.getColumn().replace(";", ""));
			}
		}
		return conditionString.toString();
	}

	public static String buildGroupBy(Query query) {
		StringBuffer groupBy = new StringBuffer();
		for (Condition condition : query.getGroupBy()) {
			if (groupBy.length() > 0) {
				groupBy.append(" , ");
			}
			groupBy.append(condition.getColumn());
		}
		return groupBy.toString();
	}

	public static String buildOrderBy(Query query) {
		StringBuffer orderBy = new StringBuffer();
		for (Condition condition : query.getGroupBy()) {
			if (orderBy.length() > 0) {
				orderBy.append(" , ");
			}
			orderBy.append(condition.getColumn()).append(" ").append(condition.getValue());
		}
		return orderBy.toString();
	}

	private static String getConditionValue(Object value) {
		StringBuffer conditionString = new StringBuffer("");
		String valueType = value.getClass().getSimpleName().toLowerCase();
		if (valueType.equals("string") 
				|| valueType.equals("char")) {
			conditionString.append("'").append(String.valueOf(value).replaceAll("'", "")).append("'");
		} else if (valueType.equals("int") 
				|| valueType.equals("long") 
				|| valueType.equals("integer")
				|| valueType.equals("float") 
				|| valueType.equals("double")) {
			conditionString.append("").append(value).append("");
		} else {
			conditionString.append("'").append(String.valueOf(value).replaceAll("'", "")).append("'");
		}
		return conditionString.toString();
	}
}
