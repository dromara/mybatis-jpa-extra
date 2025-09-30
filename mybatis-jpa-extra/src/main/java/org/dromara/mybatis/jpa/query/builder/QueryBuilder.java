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
 

package org.dromara.mybatis.jpa.query.builder;

import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.query.Condition;
import org.dromara.mybatis.jpa.query.ConditionValue;
import org.dromara.mybatis.jpa.query.Operator;
import org.dromara.mybatis.jpa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder {
	private static final  Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	
	public static String build(Query query) {
		StringBuilder conditionString = new StringBuilder("");
		Operator lastExpression = Operator.AND;
		for (Condition condition : query.getConditions()) {
			Operator expression = condition.getExpression();
			Object value = condition.getValue();
			String column = SafeValueHandler.safeColumn(condition.getColumn());
			condition.setColumn(column);
			if (expression.equals(Operator.AND) || expression.equals(Operator.OR)) {
				lastExpression = condition.getExpression();
				if (value instanceof Query subQuery) {
					String conditionSubString = build(subQuery);
					if(StringUtils.isNotBlank(conditionSubString)) {
						conditionString.append(appendExpression(conditionString.toString(),lastExpression));
						conditionString.append(" ( ").append(conditionSubString).append(" ) ");
					}
				}

			}else if (expression.equals(Operator.CONDITION)) {
				conditionString.append(column);
			} else {
				logger.trace("Expression {} column {} value class {}",lastExpression,column,value == null ? "" : value.getClass().getCanonicalName());
				
				conditionString.append(appendExpression(conditionString.toString(),lastExpression));
				
				if (expression.equals(Operator.LIKE) || expression.equals(Operator.NOT_LIKE)) {
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("%'");
	
				}else if (expression.equals(Operator.IGNORE_CASE)) {
					conditionString.append("UPPER(").append(column).append(") ").append(Operator.EQ.getOperator()).append(" ");
					conditionString.append("UPPER(").append(SafeValueHandler.valueOf(value)).append(")");
	
				} else if (expression.equals(Operator.LIKE_LEFT)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("'");
	
				} else if (expression.equals(Operator.LIKE_RIGHT)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'").append(SafeValueHandler.valueOf(value)).append("%'");
	
				} else if (expression.equals(Operator.EQ) || expression.equals(Operator.NOT_EQ)
						|| expression.equals(Operator.GT) || expression.equals(Operator.GE)
						|| expression.equals(Operator.LT) || expression.equals(Operator.LE)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(SafeValueHandler.valueOfType(value));
	
				} else if (expression.equals(Operator.BETWEEN) || expression.equals(Operator.NOT_BETWEEN)) {
	
					conditionString.append(" ( ").append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(SafeValueHandler.valueOfType(value));
					conditionString.append(" and ");
					conditionString.append(SafeValueHandler.valueOfType(condition.getValue2())).append(" ) ");
	
				} else if (expression.equals(Operator.IS_NULL) || expression.equals(Operator.IS_NOT_NULL)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator());
	
				} else if (expression.equals(Operator.IN) || expression.equals(Operator.NOT_IN)) {
					String inValues = ConditionValue.getCollectionValues(value);
					if(StringUtils.isNotBlank(inValues)) {
						conditionString.append(column).append(" ").append(expression.getOperator());
						conditionString.append(" ( ").append(inValues).append(" ) ");
					}
				} 
			}
		}
		logger.trace("conditionString {}" , conditionString);
		return conditionString.toString();
	}
	
	public static String appendExpression(String conditionString , Operator lastExpression) {
		return StringUtils.isBlank(conditionString) ? "" : " " + lastExpression + " ";
	}
	
	public static String buildGroupBy(Query query) {
		StringBuilder groupBy = new StringBuilder();
		for (Condition condition : query.getGroupBy()) {
			if (!groupBy.isEmpty()) {
				groupBy.append(" , ");
			}
			groupBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
		}
		return groupBy.toString();
	}

	public static String buildOrderBy(Query query) {
		StringBuilder orderBy = new StringBuilder();
		for (Condition condition : query.getOrderBy()) {
			if (!orderBy.isEmpty()) {
				orderBy.append(" , ");
			}
			orderBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
			orderBy.append(" ");
			orderBy.append(SafeValueHandler.valueOf(condition.getValue()));
		}
		return orderBy.toString();
	}

}
