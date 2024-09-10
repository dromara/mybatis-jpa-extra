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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder {
	private static final  Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	
	@SuppressWarnings("rawtypes")
	public static String build(Query query) {
		StringBuffer conditionString = new StringBuffer("");
		for (Condition condition : query.getConditions()) {
			condition.setColumn(condition.getColumn().replace("'", "").replace(" ", "").replace(";", ""));
			if (condition.getExpression().equals(Operator.and) 
					|| condition.getExpression().equals(Operator.or)) {

				conditionString.append(" ").append(condition.getExpression().getOperator()).append(" ");

				if (condition.getValue() instanceof Query subQuery) {
					conditionString.append(" ( ").append(build(subQuery)).append(" ) ");
				}

			} else if (condition.getExpression().equals(Operator.like)
					|| condition.getExpression().equals(Operator.notLike)) {

				conditionString.append(condition.getColumn()).append(" ")
						.append(condition.getExpression().getOperator()).append(" ");
				conditionString.append("'%").append(condition.getValue().toString()).append("%'");

			}else if (condition.getExpression().equals(Operator.ignoreCase)) {
				conditionString.append("UPPER(").append(condition.getColumn()).append(") ")
							   .append(Operator.eq.getOperator()).append(" ");
				conditionString.append("UPPER(").append(ConditionValue.valueOf(condition.getValue())).append(")");

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
					conditionString.append(condition.getColumn()).append(" ").append(condition.getExpression().getOperator());
					conditionString.append(" ( ");
					Object[] objects = (Object[]) condition.getValue();
					if(objects[0] instanceof Collection<?> cObjects) {
						logger.debug("objects[0] is Collection {}" , cObjects);
						conditionString.append(buildCollection(cObjects));
					}else if(objects[0].getClass().isArray()) {
						objects = (Object[])objects[0];
						logger.debug("objects[0] is isArray {}" , objects);
						conditionString.append(buildArray(objects));
					}else {
						logger.debug("not  isArray {}" , objects);
						conditionString.append(buildArray(objects));
					}
					conditionString.append(" ) ");
				}else if(condition.getValue().getClass().getCanonicalName().equalsIgnoreCase("java.util.ArrayList")) {
					conditionString.append(condition.getColumn()).append(" ").append(condition.getExpression().getOperator());
					conditionString.append(" ( ");
					StringBuffer conditionArray = new StringBuffer();
					ArrayList objects = (ArrayList) condition.getValue();
					for (Object object : objects) {
						if (conditionArray.length() > 0) {
							conditionArray.append(" , ");
						}
						conditionArray.append(ConditionValue.valueOf(object));
					}
					conditionString.append(conditionArray);
					conditionString.append(" ) ");
				}else if(condition.getValue().getClass().getCanonicalName().equalsIgnoreCase("java.util.LinkedList")) {
					conditionString.append(condition.getColumn()).append(" ").append(condition.getExpression().getOperator());
					conditionString.append(" ( ");
					StringBuffer conditionArray = new StringBuffer();
					LinkedList objects = (LinkedList) condition.getValue();
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

	private static String buildArray(Object[] objects) {
		StringBuffer conditionArray = new StringBuffer();
		for (int i = 0 ; i< objects.length ; i++) {
			if (conditionArray.length() > 0) {
				conditionArray.append(" , ");
			}
			conditionArray.append(ConditionValue.valueOf(objects[i]));
		}
		return conditionArray.toString();
	}
	
	private static String buildCollection(Collection<?> cObjects) {
		StringBuffer conditionArray = new StringBuffer();
        for (Object element : cObjects) {//for循环读取集合
        	if (conditionArray.length() > 0) {
				conditionArray.append(" , ");
			}
        	conditionArray.append(ConditionValue.valueOf(element));
        	logger.debug("{}",element);
        }
		return conditionArray.toString();
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
