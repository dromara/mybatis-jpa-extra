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

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unchecked","rawtypes"})
public class LambdaQueryBuilder {
	private static final Logger logger = LoggerFactory.getLogger(LambdaQueryBuilder.class);
			
	public static String build(LambdaQuery lambdaQuery) {
		StringBuffer conditionString = new StringBuffer("");
		List<Condition> conditions = lambdaQuery.getConditions();
		Operator lastExpression = Operator.and;
		for (Condition condition : conditions) {
			Operator expression = condition.getExpression();
			Object value = condition.getValue();
			String column = SafeValueHandler.safeColumn(condition.getColumn());
			condition.setColumn(column);
			if (expression.equals(Operator.and) || expression.equals(Operator.or)) {
				lastExpression = condition.getExpression();
				if (value instanceof LambdaQuery lambdaQueryValue) {
					conditionString.append(appendExpression(conditionString.toString(),lastExpression));
					conditionString.append(" ( ").append(build(lambdaQueryValue)).append(" ) ");
				}
			}else if (expression.equals(Operator.condition)) {
				conditionString.append(column);
			} else {
				logger.trace("Expression {} column {} value class {}",lastExpression,column,value == null ? "" : value.getClass().getCanonicalName());
		
				conditionString.append(appendExpression(conditionString.toString(),lastExpression));
				
				if (expression.equals(Operator.like) || expression.equals(Operator.notLike)) {

					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("%'");
	
				} else if (expression.equals(Operator.likeLeft)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("'");
	
				} else if (expression.equals(Operator.likeRight)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'").append(SafeValueHandler.valueOf(value)).append("%'");
	
				} else if (expression.equals(Operator.eq) || expression.equals(Operator.notEq)
						|| expression.equals(Operator.gt) || expression.equals(Operator.ge)
						|| expression.equals(Operator.lt) || expression.equals(Operator.le)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(SafeValueHandler.valueOfType(value));
	
				} else if (expression.equals(Operator.between)|| expression.equals(Operator.notBetween)) {
	
					conditionString.append(" ( ").append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(SafeValueHandler.valueOfType(value));
					conditionString.append(" and ");
					conditionString.append(SafeValueHandler.valueOfType(condition.getValue2())).append(" ) ");
	
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
	
	public static String appendExpression(String conditionString , Operator lastExpression) {
		return StringUtils.isBlank(conditionString) ? "" : " " + lastExpression + " ";
	}

	public static String buildGroupBy(LambdaQuery lambdaQuery) {
		StringBuffer groupBy = new StringBuffer();
		List<Condition> conditions = lambdaQuery.getGroupBy();
		for (Condition condition : conditions) {
			if (groupBy.length() > 0) {
				groupBy.append(" , ");
			}
			groupBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
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
			orderBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
			orderBy.append(" ");
			orderBy.append(SafeValueHandler.valueOf(condition.getValue()));
		}
		return orderBy.toString();
	}

	
}
