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
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dromara.mybatis.jpa.functions.IGetter;
import org.dromara.mybatis.jpa.util.LambdaUtil;

public class LambdaQuery <T> {

	List<Condition> conditions = new ArrayList<>();
	
	List<Condition> groupBy ;
	
	List<Condition> orderBy ;
	
	boolean softDelete = true;
	
	public LambdaQuery() {
		super();
	}

	public void joint() {
		if(CollectionUtils.isNotEmpty(conditions)) {
			Operator lastJoint = conditions.get(conditions.size() -1).getExpression();
			if(!lastJoint.equals(Operator.AND) && !lastJoint.equals(Operator.OR)){
				and();
			}
		}
	}
	
	public LambdaQuery <T> and() {
		conditions.add(new Condition(Operator.AND,"",null));
		return this;
	}
	
	public LambdaQuery <T> or() {
		conditions.add(new Condition(Operator.OR,"",null));
		return this;
	}
	
	public LambdaQuery <T> and(LambdaQuery<T> subQuery) {
		conditions.add(new Condition(Operator.AND,"",subQuery));
		return this;
	}
	
	public LambdaQuery <T> or(LambdaQuery<T> subQuery) {
		conditions.add(new Condition(Operator.OR,"",subQuery));
		return this;
	}
	
	/**
     * 添加等于条件<br>
     * 
     * <code>query.eq(User::getUser, "tom")</code>
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
	public LambdaQuery <T>eq(IGetter<T> getter , Object value) {
		conditions.add(new Condition(Operator.EQ,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加等于条件
     * <pre>
     *     query.eq(StringUtils.hasText(name), User::getUserName, name);
     *     等同于：
     *     if (StringUtils.hasText(name)) {
     *         query.eq(User::getUserName, name);
     *     }
     * </pre>
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>eq(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            eq(getter , value);
        }
        return  this;
    }

    /**
     * 添加不等于条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    
	public LambdaQuery <T>notEq(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.NOT_EQ, getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加不等于条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>notEq(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            notEq(getter , value);
        }
        return  this;
    }

    /**
     * 添加大于条件,>
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T> gt(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.GT,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加大于条件,>
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>gt(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            gt(getter , value);
        }
        return  this;
    }

    /**
     * 添加大于等于条件,>=
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>ge(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.GE,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加大于等于条件,>=
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>ge(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            ge(getter , value);
        }
        return  this;
    }

    /**
     * 添加小于条件,<
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>lt(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.LT,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加小于条件,<
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>lt(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            lt(getter , value);
        }
        return  this;
    }

    /**
     * 小于等于,<=
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>le(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.LE,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式小于等于条件,<=
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>le(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            le(getter , value);
        }
        return  this;
    }

    /**
     * 添加两边模糊查询条件，两边模糊匹配，即name like '%value%'
     *
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     * @see #likeLeft(getter , String) 左边模糊匹配
     * @see #likeRight(getter , String) 右边模糊匹配
     */
    public LambdaQuery <T>like(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.LIKE,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加两边模糊查询条件，两边模糊匹配，即name like '%value%'
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     * @see #likeLeft(boolean, getter , String) 左模糊
     * @see #likeRight(boolean, getter , String) 右模糊
     */
    public LambdaQuery <T>like(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            like(getter , value);
        }
        return  this;
    }

    /**
     * 添加左模糊查询条件，左边模糊匹配，即name like '%value'
     *
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public LambdaQuery <T>likeLeft(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.LIKE_LEFT,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加左模糊查询条件，左边模糊匹配，即name like '%value'
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public LambdaQuery <T>likeLeft(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            likeLeft(getter , value);
        }
        return  this;
    }

    /**
     * 添加右模糊查询条件，右边模糊匹配，即name like 'value%'。mysql推荐用这种
     *
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public LambdaQuery <T>likeRight(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.LIKE_RIGHT,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加右模糊查询条件，右边模糊匹配，即name like 'value%'。mysql推荐用这种
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public LambdaQuery <T>likeRight(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            likeRight(getter , value);
        }
        return  this;
    }

    /**
     * 添加IN条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>in(IGetter<T> getter , Collection<?> value) {
    	conditions.add(new Condition(Operator.IN,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加IN条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>in(boolean expression, IGetter<T> getter , Collection<?> value) {
        if (expression) {
            in(getter , value);
        }
        return  this;
    }

    /**
     * 添加IN条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>in(IGetter<T> getter , Object[] value) {
    	conditions.add(new Condition(Operator.IN,getColumnName(getter), value));
        return  this;
    }

    /**
     * 根据表达式添加IN条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>in(boolean expression, IGetter<T> getter , Object[] value) {
        if (expression) {
            in(getter , value);
        }
        return  this;
    }

    /**
     * 添加not in条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>notIn(IGetter<T> getter , Collection<?> value) {
    	conditions.add(new Condition(Operator.NOT_IN,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加not in条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>notIn(boolean expression, IGetter<T> getter , Collection<?> value) {
        if (expression) {
            notIn(getter , value);
        }
        return  this;
    }


    /**
     * 添加not in条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>notIn(IGetter<T> getter , Object[] value) {
    	conditions.add(new Condition(Operator.NOT_IN,getColumnName(getter ), value));
        return  this;
    }

    /**
     * 根据表达式添加not in条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public LambdaQuery <T>notIn(boolean expression, IGetter<T> getter , Object[] value) {
        if (expression) {
            notIn(getter , value);
        }
        return  this;
    }

    /**
     * 添加between条件
     *
     * @param IGetter 方法函数
     * @param startValue 起始值
     * @param endValue   结束值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(IGetter<T> getter , Object startValue, Object endValue) {
    	conditions.add(new Condition(Operator.BETWEEN,getColumnName(getter ), startValue, endValue));
        return  this;
    }

    /**
     * 添加between条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param startValue 起始值
     * @param endValue   结束值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(boolean expression, IGetter<T> getter , Object startValue, Object endValue) {
        if (expression) {
            between(getter , startValue, endValue);
        }
        return  this;
    }

    /**
     * 添加between条件
     * <pre>
     * {@literal
     * Object[] arr = new Object[]{1, 100};
     * query.between(arr);
     * }
     * </pre>
     *
     * @param values 存放起始值、结束值，只能存放2个值，values[0]表示开始值，value[1]表示结束值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(IGetter<T> getter , Object[] values) {
    	conditions.add(new Condition(Operator.BETWEEN,getColumnName(getter ), values[0],values[1]));
        return  this;
    }

    /**
     * 添加between条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param values     存放起始值、结束值，只能存放2个值，values[0]表示开始值，value[1]表示结束值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(boolean expression, IGetter<T> getter , Object[] values) {
        if (expression) {
            between(getter , values);
        }
        return  this;
    }

    /**
     * 添加between条件
     * <pre>
     * {@literal
     * query.between(Arrays.asList(1, 100));
     * }
     * </pre>
     *
     * @param values 存放起始值、结束值，只能存放2个值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(IGetter<T> getter , List<?> values) {
    	conditions.add(new Condition(Operator.BETWEEN,getColumnName(getter ), values.get(0),values.get(1)));
        return  this;
    }

    /**
     * 添加between条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param values     存放起始值、结束值，只能存放2个值
     * @return 返回Query对象
     */
    public LambdaQuery <T>between(boolean expression, IGetter<T> getter , List<?> values) {
        if (expression) {
            between(getter , values);
        }
        return  this;
    }

    


    /**
     * 添加字段不为null的条件
     *
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public LambdaQuery <T>notNull(IGetter<T> getter ) {
    	conditions.add(new Condition(Operator.IS_NOT_NULL,getColumnName(getter )," IS NOT NULL"));
        return  this;
    }

    /**
     * 根据表达式添加字段不为null的条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public LambdaQuery <T>notNull(boolean expression, IGetter<T> getter ) {
        if (expression) {
            notNull(getter );
        }
        return  this;
    }

    /**
     * 添加字段是null的条件
     *
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public LambdaQuery <T>isNull(IGetter<T> getter ) {
    	conditions.add(new Condition(Operator.IS_NULL,getColumnName(getter ) , " IS NULL"));
        return  this;
    }

    /**
     * 根据表达式添加字段是null的条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public LambdaQuery <T>isNull(boolean expression, IGetter<T> getter ) {
        if (expression) {
            isNull(getter );
        }
        return  this;
    }
    
	/**
	 * 查询条件
	 * @param conditionSql
	 * @return
	 */
	public LambdaQuery <T>condition(String conditionSql) {
		joint();
		conditions.add(new Condition(Operator.CONDITION,conditionSql,null));
		return  this;
	}
	
	public LambdaQuery <T>groupBy(IGetter<T> getter) {
		if(CollectionUtils.isEmpty(groupBy)) {
			this.groupBy = new ArrayList<>();
		}
		groupBy.add(new Condition(Operator.GROUP,getColumnName(getter),""));
		return  this;
	}
	
	public LambdaQuery <T>orderBy(IGetter<T> getter,String orderType) {
		if(CollectionUtils.isEmpty(orderBy)) {
			this.orderBy = new ArrayList<>();
		}
		orderBy.add(new Condition(Operator.ORDER,getColumnName(getter),orderType));
		return  this;
	}

	public List<Condition> getGroupBy() {
		return groupBy;
	}

	public List<Condition> getConditions() {
		return conditions;
	}
	
	public List<Condition> getOrderBy() {
		return orderBy;
	}
	
	public boolean isSoftDelete() {
		return softDelete;
	}

	public void setSoftDelete(boolean softDelete) {
		this.softDelete = softDelete;
	}

	/**
	 * 通过IGetter获取对应的ColumnName名称
	 * @param getter
	 * @return ColumnName
	 */
	public String getColumnName(IGetter <T> getter ) {
		return LambdaUtil.getColumnName(getter);
	}

	@Override
	public String toString() {
		return "Query [conditions=" + conditions + ", orderBy=" + orderBy + "]";
	}
}
