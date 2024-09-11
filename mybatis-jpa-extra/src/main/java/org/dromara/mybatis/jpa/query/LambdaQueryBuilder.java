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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked","rawtypes"})
public class LambdaQueryBuilder {
	private static final Logger logger = LoggerFactory.getLogger(LambdaQueryBuilder.class);
			
	public static String build(LambdaQuery lambdaQuery) {
		StringBuffer conditionString = new StringBuffer("");
		List<Condition> conditions = lambdaQuery.getConditions();
		for (Condition condition : conditions) {
			Operator expression = condition.getExpression();
			Object value = condition.getValue();
			String column = condition.getColumn().replace("'", "").replace(" ", "").replace(";", "");
			//Column去除 ',空格,分号
			condition.setColumn(column);
			
			if (expression.equals(Operator.and) || expression.equals(Operator.or)) {

				conditionString.append(" ").append(expression.getOperator()).append(" ");

				if (value instanceof LambdaQuery lambdaQueryValue) {
					conditionString.append(" ( ").append(build(lambdaQueryValue)).append(" ) ");
				}

			}else if (expression.equals(Operator.condition)) {
				conditionString.append(column.replace(";", ""));
			} else {
				logger.trace("column {} value class {}",column,value == null ? "" : value.getClass().getCanonicalName());
				
				if (expression.equals(Operator.like) || expression.equals(Operator.notLike)) {

					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(value).append("%'");
	
				} else if (expression.equals(Operator.likeLeft)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(value).append("'");
	
				} else if (expression.equals(Operator.likeRight)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'").append(value).append("%'");
	
				} else if (expression.equals(Operator.eq) || expression.equals(Operator.notEq)
						|| expression.equals(Operator.gt) || expression.equals(Operator.ge)
						|| expression.equals(Operator.lt) || expression.equals(Operator.le)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(ConditionValue.valueOf(value));
	
				} else if (expression.equals(Operator.between)|| expression.equals(Operator.notBetween)) {
	
					conditionString.append(" ( ").append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(ConditionValue.valueOf(value));
					conditionString.append(" and ");
					conditionString.append(ConditionValue.valueOf(condition.getValue2())).append(" ) ");
	
				} else if (expression.equals(Operator.isNull) || expression.equals(Operator.isNotNull)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator());
	
				} else if (expression.equals(Operator.in) || expression.equals(Operator.notIn)) {
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ( ");
					if (value.getClass().isArray()) {
						conditionString.append(ConditionValue.valueOfArray((Object[]) value));
					}else if(value.getClass().getCanonicalName().equalsIgnoreCase("java.util.ArrayList")) {
						conditionString.append(ConditionValue.valueOfList((ArrayList) value));
					}else if(value.getClass().getCanonicalName().equalsIgnoreCase("java.util.LinkedList")) {
						conditionString.append(ConditionValue.valueOfList((LinkedList) value));
					}
					conditionString.append(" ) ");
				} 
			}
		}
		logger.trace("conditionString {}" , conditionString);
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
