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
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder {
	private static final  Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	
	@SuppressWarnings("rawtypes")
	public static String build(Query query) {
		StringBuffer conditionString = new StringBuffer("");
		Operator lastExpression = Operator.and;
		for (Condition condition : query.getConditions()) {
			Operator expression = condition.getExpression();
			Object value = condition.getValue();
			//Column去除 ',空格,分号
			String column = condition.getColumn().replace("'", "").replace(" ", "").replace(";", "");
			condition.setColumn(column);
			if (expression.equals(Operator.and) || expression.equals(Operator.or)) {
				lastExpression = condition.getExpression();
				if (value instanceof Query subQuery) {
					conditionString.append(appendExpression(conditionString.toString(),lastExpression));
					conditionString.append(" ( ").append(build(subQuery)).append(" ) ");
				}

			}else if (expression.equals(Operator.condition)) {
				conditionString.append(column);
			} else {
				logger.trace("Expression {} column {} value class {}",lastExpression,column,value == null ? "" : value.getClass().getCanonicalName());
				
				conditionString.append(appendExpression(conditionString.toString(),lastExpression));
				
				if (expression.equals(Operator.like) || expression.equals(Operator.notLike)) {
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append("'%").append(value).append("%'");
	
				}else if (expression.equals(Operator.ignoreCase)) {
					conditionString.append("UPPER(").append(column).append(") ").append(Operator.eq.getOperator()).append(" ");
					conditionString.append("UPPER(").append(ConditionValue.valueOf(value)).append(")");
	
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
	
				} else if (expression.equals(Operator.between) || expression.equals(Operator.notBetween)) {
	
					conditionString.append(" ( ").append(column).append(" ").append(expression.getOperator()).append(" ");
					conditionString.append(ConditionValue.valueOf(value));
					conditionString.append(" and ");
					conditionString.append(ConditionValue.valueOf(condition.getValue2())).append(" ) ");
	
				} else if (expression.equals(Operator.isNull) || expression.equals(Operator.isNotNull)) {
	
					conditionString.append(column).append(" ").append(expression.getOperator());
	
				} else if (expression.equals(Operator.in) || expression.equals(Operator.notIn)) {
					conditionString.append(column).append(" ").append(expression.getOperator()).append(" ( ");
					if (value.getClass().isArray()) {
						Object[] objects = (Object[]) value;
						if(objects[0] instanceof Collection<?> cObjects) {
							logger.trace("objects[0] is Collection {}" , cObjects);
							conditionString.append(ConditionValue.valueOfCollection(cObjects));
						}else if(objects[0].getClass().isArray()) {
							objects = (Object[])objects[0];
							logger.trace("objects[0] is isArray {}" , objects);
							conditionString.append(ConditionValue.valueOfArray(objects));
						}else {
							logger.trace("not  isArray {}" , objects);
							conditionString.append(ConditionValue.valueOfArray(objects));
						}
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
		for (Condition condition : query.getOrderBy()) {
			if (orderBy.length() > 0) {
				orderBy.append(" , ");
			}
			orderBy.append(condition.getColumn()).append(" ").append(condition.getValue());
		}
		return orderBy.toString();
	}

}
