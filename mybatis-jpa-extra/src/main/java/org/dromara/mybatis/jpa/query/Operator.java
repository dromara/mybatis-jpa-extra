package org.dromara.mybatis.jpa.query;

public enum Operator {
	/**
	 * and
	 */
	and("and"),
	/**
	 * or
	 */
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
	/** not between查询 */
	notBetween("not between"),
	/** 模糊查询,两边模糊查询,like '%xx%' */
	like("like"),
	
	notLike("not like"),
	/** 左模糊查询,like '%xx' */
	likeLeft("like"),
	/** 右模糊查询,like 'xx%' */
	likeRight("like"),
	/**
	 * 空
	 */
	isNull("is null"),
	/**
	 * 不为空
	 */
	isNotNull("is not null"),
	/**
	 * 条件
	 */
	condition("condition"),
	/**
	 * 分组
	 */
	group("group"),
	/**
	 * 	排序
	 */
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