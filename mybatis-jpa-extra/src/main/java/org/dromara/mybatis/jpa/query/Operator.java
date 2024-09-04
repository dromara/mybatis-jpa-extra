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

/**
 * 操作符
 */
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
	/**
	 * 忽略大小写
	 */
	ignoreCase("ignoreCase"),
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