/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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

package org.dromara.mybatis.jpa.persistence.provider;

import org.apache.ibatis.jdbc.SQL;
import org.dromara.mybatis.jpa.persistence.FieldColumnMapper;
import org.dromara.mybatis.jpa.persistence.JpaBaseEntity;
import org.dromara.mybatis.jpa.persistence.MapperMetadata;
import org.dromara.mybatis.jpa.query.Condition;
import org.dromara.mybatis.jpa.query.Operator;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class QueryProvider<T extends JpaBaseEntity> {

	private static final Logger _logger = LoggerFactory.getLogger(QueryProvider.class);

	public String filterByQuery(T entity, Query query) {
		_logger.trace("Query \n" + query);
		SQL sql = MapperMetadata.buildSelect(entity.getClass()).WHERE(buildQuery(query));
		
		if (query.getGroupBy() != null) {
			sql.GROUP_BY(buildQueryGroupBy(query));
		}
		if (query.getOrderBy() != null) {
			sql.ORDER_BY(buildQueryOrderBy(query));
		}
		_logger.trace("filter By Query SQL \n" + sql.toString());
		return sql.toString();
	}

	public String buildQuery(Query query) {
		StringBuffer conditionString = new StringBuffer("");
		for (Condition condition : query.getConditions()) {
			condition.setColumn(condition.getColumn().replaceAll("'", "").replaceAll(" ", "").replace(";", ""));
			if (condition.getExpression().equals(Operator.and) 
					|| condition.getExpression().equals(Operator.or)) {

				conditionString.append(" ").append(condition.getExpression().getOperator()).append(" ");

				if (condition.getValue() != null && condition.getValue() instanceof Query) {
					conditionString.append(" ( ").append(buildQuery((Query) condition.getValue())).append(" ) ");
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

	String buildQueryGroupBy(Query query) {
		StringBuffer groupBy = new StringBuffer();
		for (Condition condition : query.getGroupBy()) {
			if (groupBy.length() > 0) {
				groupBy.append(" , ");
			}
			groupBy.append(condition.getColumn());
		}
		return groupBy.toString();
	}

	String buildQueryOrderBy(Query query) {
		StringBuffer orderBy = new StringBuffer();
		for (Condition condition : query.getGroupBy()) {
			if (orderBy.length() > 0) {
				orderBy.append(" , ");
			}
			orderBy.append(condition.getColumn()).append(" ").append(condition.getValue());
		}
		return orderBy.toString();
	}

	public String getConditionValue(Object value) {
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

	public String query(T entity) {
		SQL sql = MapperMetadata.buildSelect(entity.getClass());

		for (FieldColumnMapper fieldColumnMapper : MapperMetadata.fieldsMap.get(entity.getClass().getSimpleName())) {
			String fieldValue = BeanUtil.getValue(entity, fieldColumnMapper.getFieldName());
			String fieldType = fieldColumnMapper.getFieldType().toLowerCase();

			_logger.trace("ColumnName {} , FieldType {} , value {}", fieldColumnMapper.getColumnName(), fieldType,
					fieldValue);

			if (fieldValue == null 
					|| (fieldType.equals("string") && fieldValue.equals(""))
					|| (fieldType.startsWith("byte") && fieldValue == null)
					|| (fieldType.equals("int") && fieldValue.equals("0"))
					|| (fieldType.equals("long") && fieldValue.equals("0"))
					|| (fieldType.equals("integer") && fieldValue.equals("0"))
					|| (fieldType.equals("float") && fieldValue.equals("0.0"))
					|| (fieldType.equals("double") && fieldValue.equals("0.0"))) {
				// skip null field value
			} else {
				sql.WHERE(fieldColumnMapper.getColumnName() + " = #{" + fieldColumnMapper.getFieldName() + "}");
			}
		}
		_logger.trace("filter By Entity SQL \n" + sql.toString());
		return sql.toString();
	}

}
