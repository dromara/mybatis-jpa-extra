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
	AND("and"),
	/**
	 * or
	 */
	OR("or"),
	
	/** 等于= */
	EQ("="),
	/** 不等于&lt;&gt; */
	NOT_EQ("<>"),
	
	/** 大于&gt; */
	GT(">"),
	/** 大于等于&gt;= */
	GE(">="),
	
	/** 小于&lt; */
	LT("<"),
	/** 小于等于&lt;= */
	LE("<="),
	
	/** in()查询  */
	IN("in"),
	/** not in()查询 */
	NOT_IN("not in"),
	
	/** between查询 */
	BETWEEN("between"),
	/** not between查询 */
	NOT_BETWEEN("not between"),
	/** 模糊查询,两边模糊查询,like '%xx%' */
	LIKE("like"),
	
	NOT_LIKE("not like"),
	/** 左模糊查询,like '%xx' */
	LIKE_LEFT("like"),
	/** 右模糊查询,like 'xx%' */
	LIKE_RIGHT("like"),
	/**
	 * 空
	 */
	IS_NULL("is null"),
	/**
	 * 不为空
	 */
	IS_NOT_NULL("is not null"),
	/**
	 * 条件
	 */
	CONDITION("condition"),
	/**
	 * 分组
	 */
	GROUP("group"),
	/**
	 * 	排序
	 */
	ORDER("order"),
	/**
	 * 忽略大小写
	 */
	IGNORE_CASE("ignoreCase"),
	;
	
    /**
     * 符号
     */
	private String notation;

	Operator(String notation) {
		this.notation = notation;
	}

	/**
	 * 返回操作符
	 * @return 返回操作符
	 */
	public String getOperator() {
		return notation;
	}

}