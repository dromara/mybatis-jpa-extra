package org.dromara.mybatis.jpa.query;

import java.util.ArrayList;

public class Query {

	public final static class OrderType {

		public final static String ASC 	= "asc";
		
		public final static String DESC = "desc";
	}
	
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	
	ArrayList<Condition> groupBy ;
	
	ArrayList<Condition> orderBy ;
	
	
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}

	public ArrayList<Condition> getOrderBy() {
		return orderBy;
	}

	public void joint() {
		if(conditions.size() >= 1) {
			Operator lastJoint = conditions.get(conditions.size() -1).getExpression();
			if(lastJoint.equals(Operator.and)
					||lastJoint.equals(Operator.or)) {
			}else {
				and();
			}
		}
	}
	
	public Query and() {
		conditions.add(new Condition(Operator.and,"",null));
		return this;
	}
	
	public Query or() {
		conditions.add(new Condition(Operator.or,"",null));
		return this;
	}
	
	public Query and(Query subQuery) {
		conditions.add(new Condition(Operator.and,"",subQuery));
		return this;
	}
	
	public Query or(Query subQuery) {
		conditions.add(new Condition(Operator.or,"",subQuery));
		return this;
	}
	
	/**
	 * 等于 =
	 * @param column
	 * @param value
	* @return Query
	 */
	public Query eq(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.eq,column,value));
		return this;
	}
	
	
	/**
	 * 不等于 <>
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query notEq(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.notEq,column,value));
		return this;
	}
	
	/**
	 * in(...)
	 * @param column
	 * @param value
	 * @return
	 */
	public Query in(String column, Object ... value) {
		joint();
		conditions.add(new Condition(Operator.in,column,value));
		return this;
	}
	
	/**
	 * not in(...)
	 * @param column
	 * @param value
	 * @return
	 */
	public Query notIn(String column, Object ... value) {
		joint();
		conditions.add(new Condition(Operator.notIn,column,value));
		return this;
	}
	
	/**
	 * 大于 >
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query gt(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.gt,column,value));
		return this;
	}
	
	/**
	 * 大于等于 >=
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query ge(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.ge,column,value));
		return this;
	}
	
	/**
	 * 小于 <
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query lt(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.lt,column,value));
		return this;
	}
	
	/**
	 * 小于等于 <=
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query le(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.le,column,value));
		return this;
	}
	
	/**
	 * column like '%value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query like(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.like,column,value));
		return this;
	}
	
	/**
	 * column not like '%value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query notLike(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.notLike,column,value));
		return this;
	}
	
	
	/**
	 * column like '%value'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query likeLeft(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.likeLeft,column,value));
		return this;
	}
	
	/**
	 * column like 'value%'
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query likeRight(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.likeRight,column,value));
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
		conditions.add(new Condition(Operator.isNull,column,null));
		return this;
	}
	
	/**
	 * column is not null
	 * @param column
	 * @param value
	 * @return Query
	 */
	public Query isNotNull(String column, Object value) {
		joint();
		conditions.add(new Condition(Operator.isNotNull,column,null));
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
		conditions.add(new Condition(Operator.between,column,value1,value2));
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
		conditions.add(new Condition(Operator.notBetween,column,value1,value2));
		return this;
	}
	
	/**
	 * 查询条件
	 * @param conditionSql
	 * @return
	 */
	public Query condition(String conditionSql) {
		joint();
		conditions.add(new Condition(Operator.condition,conditionSql,null));
		return this;
	}
	
	public Query groupBy(String column) {
		if(groupBy == null) {
			this.groupBy = new ArrayList<Condition>();
		}
		groupBy.add(new Condition(Operator.group,column,""));
		return this;
	}
	
	public Query orderBy(String column,String orderType) {
		if(orderBy == null) {
			this.orderBy = new ArrayList<Condition>();
		}
		orderBy.add(new Condition(Operator.order,column,orderType));
		return this;
	}

	public ArrayList<Condition> getGroupBy() {
		return groupBy;
	}

	@Override
	public String toString() {
		return "Query [conditions=" + conditions + ", orderBy=" + orderBy + "]";
	}
	
	
}
