package org.dromara.mybatis.jpa.query;

import java.util.List;

@SuppressWarnings({"unchecked","rawtypes"})
public class LambdaQueryBuilder {
	public static String build(LambdaQuery lambdaQuery) {
		StringBuffer conditionString = new StringBuffer("");
		List<Condition> conditions = lambdaQuery.getConditions();
		for (Condition condition : conditions) {
			condition.setColumn(condition.getColumn().replace("'", "").replace(" ", "").replace(";", ""));
			if (condition.getExpression().equals(Operator.and) 
					|| condition.getExpression().equals(Operator.or)) {

				conditionString.append(" ").append(condition.getExpression().getOperator()).append(" ");

				if (condition.getValue() instanceof LambdaQuery lambdaQueryValue) {
					conditionString.append(" ( ").append(build(lambdaQueryValue)).append(" ) ");
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
				conditionString.append(ConditionValue.valueOf(condition.getValue()));

			} else if (condition.getExpression().equals(Operator.between)
					|| condition.getExpression().equals(Operator.notBetween)) {

				conditionString
						.append(" ( ").append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append(ConditionValue.valueOf(condition.getValue()));
				conditionString.append(" and ");
				conditionString.append(ConditionValue.valueOf(condition.getValue2()))
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
						conditionArray.append(ConditionValue.valueOf(object));
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

	public static String buildGroupBy(LambdaQuery lambdaQuery) {
		StringBuffer groupBy = new StringBuffer();
		List<Condition> conditions = lambdaQuery.getGroupBy();
		for (Condition condition : conditions) {
			if (groupBy.length() > 0) {
				groupBy.append(" , ");
			}
			groupBy.append(condition.getColumn());
		}
		return groupBy.toString();
	}

	
	public static String buildOrderBy(LambdaQuery lambdaQuery) {
		StringBuffer orderBy = new StringBuffer();
		
		List<Condition> conditions = lambdaQuery.getGroupBy();
		for (Condition condition : conditions) {
			if (orderBy.length() > 0) {
				orderBy.append(" , ");
			}
			orderBy.append(condition.getColumn()).append(" ").append(condition.getValue());
		}
		return orderBy.toString();
	}

	
}
