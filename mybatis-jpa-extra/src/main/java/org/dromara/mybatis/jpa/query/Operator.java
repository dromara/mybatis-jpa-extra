package org.dromara.mybatis.jpa.query;

public enum Operator {
	
	and("and"),
	
	or("or"),
	
	/** 等于= */
	eq("="),
	/** 不等于&lt;&gt; */
	notEq("<>"),
	
	/** 大于&gt; */
	gt(">"),
	/** 大于等于&gt;= */
	ge(">="),
	
	/** 小于&lt; */
	lt("<"),
	/** 小于等于&lt;= */
	le("<="),
	
	/** in()查询  */
	in("in"),
	/** not in()查询 */
	notIn("not in"),
	
	/** between查询 */
	between("between"),
	notBetween("not between"),
	/** 模糊查询,两边模糊查询,like '%xx%' */
	like("like"),
	
	notLike("not like"),
	/** 左模糊查询,like '%xx' */
	likeLeft("like"),
	/** 右模糊查询,like 'xx%' */
	likeRight("like"),
	
	isNull("is null"),
	isNotNull("is not null"),
	
	condition("condition"),
	
	group("group"),
	
	order("order"),
	;
	
    
	private String operator;

	Operator(String operator) {
		this.operator = operator;
	}

	/**
	 * 返回操作符
	 * @return 返回操作符
	 */
	public String getOperator() {
		return operator;
	}

}