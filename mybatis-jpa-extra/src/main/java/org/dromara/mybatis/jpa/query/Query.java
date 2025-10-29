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
import org.apache.commons.collections.CollectionUtils;

public class Query extends BaseQuery {
	
	public Query() {
		super();
	}

	public static Query builder(){
		return new Query();
	}

	public void joint() {
		if(CollectionUtils.isNotEmpty(conditions)) {
			Operator lastJoint = conditions.get(conditions.size() -1).getExpression();
			if(lastJoint.equals(Operator.AND)||lastJoint.equals(Operator.OR)){
				//
			}else {
				and();
			}
		}
	}
	
	public Query and() {
		conditions.add(new Condition(Operator.AND,"",null));
		return this;
	}
	
	public Query or() {
		conditions.add(new Condition(Operator.OR,"",null));
		return this;
	}
	
	public Query and(Query subQuery) {
		conditions.add(new Condition(Operator.AND,"",subQuery));
		return this;
	}
	
	public Query or(Query subQuery) {
		conditions.add(new Condition(Operator.OR,"",subQuery));
		return this;
	}
	
	/**
	 * 等于 =
	 * @param column
	 * @param value
	* @return Query
	 */
	public Query eq(String column, Object value) {
		eq( true , column , value);
		return this;
	}
	
	/**
	 * 等于 =
	 * @param column
	 * @param value
	* @return Query
	 */
	public Query ignoreCase(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.IGNORE_CASE,column,value));
		return this;
	}
	
	public Query eq(boolean expression , String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.EQ,column,value));
		}
		return this;
	}
	
	/**
	 * 不等于 <>
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query notEq(String column, Object value) {
		notEq( true , column , value);
		return this;
	}
	
	public Query notEq(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.NOT_EQ,column,value));
		}
		return this;
	}
	
	/**
	 * in(...)
	 * @param column
	 * @param value
	 * @return
	 */
	public Query in(String column, Object ... value) {
		in( true , column , value);
		return this;
	}
	
	public Query in(boolean expression ,String column, Object ... value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.IN,column,value));
		}
		return this;
	}
	
	/**
	 * not in(...)
	 * @param column
	 * @param value
	 * @return
	 */
	public Query notIn(String column, Object ... value) {
		notIn( true , column , value);
		return this;
	}
	
	public Query notIn(boolean expression ,String column, Object ... value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.NOT_IN,column,value));
		}
		return this;
	}
	
	/**
	 * 大于 >
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query gt(String column, Object value) {
		gt( true , column , value);
		return this;
	}
	
	public Query gt(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.GT,column,value));
		}
		return this;
	}
	
	/**
	 * 大于等于 >=
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query ge(String column, Object value) {
		ge( true , column , value);
		return this;
	}
	
	public Query ge(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.GE,column,value));
		}
		return this;
	}
	
	/**
	 * 小于 <
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query lt(String column, Object value) {
		lt(true,column,value);
		return this;
	}
	
	public Query lt(boolean expression , String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.LT,column,value));
		}
		return this;
	}
	
	/**
	 * 小于等于 <=
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query le(String column, Object value) {
		le(true,column,value);
		return this;
	}
	
	public Query le(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.LE,column,value));
		}
		return this;
	}
	
	/**
	 * column like '%value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query like(String column, Object value) {
		like(true,column,value);
		return this;
	}
	
	public Query like(boolean expression , String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.LIKE,column,value));
		}
		return this;
	}
	
	/**
	 * column not like '%value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query notLike(String column, Object value) {
		notLike(true,column,value);
		return this;
	}
	
	public Query notLike(boolean expression , String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.NOT_LIKE,column,value));
		}
		return this;
	}
	
	/**
	 * column like '%value'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query likeLeft(String column, Object value) {
		likeLeft(true,column,value);
		return this;
	}
	
	public Query likeLeft(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.LIKE_LEFT,column,value));
		}
		return this;
	}
	
	/**
	 * column like 'value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query likeRight(String column, Object value) {
		likeRight(true,column,value);
		return this;
	}
	
	public Query likeRight(boolean expression ,String column, Object value) {
		if(expression) {
			joint();
			conditions.add(new Condition(Operator.LIKE_RIGHT,column,value));
		}
		return this;
	}
	
	/**
	 * column is null
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query isNull(String column) {
		joint();
		conditions.add(new Condition(Operator.IS_NULL,column,null));
		return this;
	}
	
	/**
	 * column is not null
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query isNotNull(String column) {
		joint();
		conditions.add(new Condition(Operator.IS_NOT_NULL,column,null));
		return this;
	}
	
	
	/**
	 * column between value1 and value2
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query between(String column, Object value1, Object value2) {
		joint();
		conditions.add(new Condition(Operator.BETWEEN,column,value1,value2));
		return this;
	}
	
	/**
	 * column not between value1 and value2
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query notBetween(String column, Object value1, Object value2) {
		joint();
		conditions.add(new Condition(Operator.NOT_BETWEEN,column,value1,value2));
		return this;
	}
	
	/**
	 * 查询条件
	 * @param conditionSql
	 * @return
	 */
	public Query condition(String conditionSql) {
		joint();
		conditions.add(new Condition(Operator.CONDITION,conditionSql,null));
		return this;
	}
	
	public Query groupBy(String column) {
		if(CollectionUtils.isEmpty(groupBy)) {
			this.groupBy = new ArrayList<>();
		}
		groupBy.add(new Condition(Operator.GROUP,column,""));
		return this;
	}
	
	public Query orderBy(String column,String orderType) {
		if(CollectionUtils.isEmpty(orderBy)) {
			this.orderBy = new ArrayList<>();
		}
		orderBy.add(new Condition(Operator.ORDER,column,orderType));
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Query [conditions=");
		builder.append(conditions);
		builder.append(", groupBy=");
		builder.append(groupBy);
		builder.append(", orderBy=");
		builder.append(orderBy);
		builder.append("]");
		return builder.toString();
	}
	
	
}
