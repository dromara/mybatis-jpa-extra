package org.dromara.mybatis.jpa.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dromara.mybatis.jpa.functions.IGetter;
import org.dromara.mybatis.jpa.util.LambdaUtil;

@SuppressWarnings({"unchecked"})
public class LambdaQuery <T> {

	List<Condition> conditions = new ArrayList<>();
	
	List<Condition> groupBy ;
	
	List<Condition> orderBy ;

	public void joint() {
		if(CollectionUtils.isNotEmpty(conditions)) {
			Operator lastJoint = conditions.get(conditions.size() -1).getExpression();
			if(!lastJoint.equals(Operator.and) && !lastJoint.equals(Operator.or)){
				and();
			}
		}
	}
	
	public T and() {
		conditions.add(new Condition(Operator.and,"",null));
		return (T) this;
	}
	
	public T or() {
		conditions.add(new Condition(Operator.or,"",null));
		return (T) this;
	}
	
	public T and(LambdaQuery<T> subQuery) {
		conditions.add(new Condition(Operator.and,"",subQuery));
		return (T) this;
	}
	
	public T or(LambdaQuery<T> subQuery) {
		conditions.add(new Condition(Operator.or,"",subQuery));
		return (T) this;
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
	public T eq(IGetter<T> getter , Object value) {
		conditions.add(new Condition(Operator.eq,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
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
    public T eq(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            eq(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加不等于条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    
	public T notEq(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.notEq, LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加不等于条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T notEq(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            notEq(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加大于条件,>
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T gt(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.gt,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加大于条件,>
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T gt(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            gt(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加大于等于条件,>=
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T ge(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.ge,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加大于等于条件,>=
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T ge(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            ge(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加小于条件,<
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T lt(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.lt,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加小于条件,<
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T lt(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            lt(getter , value);
        }
        return (T) this;
    }

    /**
     * 小于等于,<=
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T le(IGetter<T> getter , Object value) {
    	conditions.add(new Condition(Operator.le,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式小于等于条件,<=
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T le(boolean expression, IGetter<T> getter , Object value) {
        if (expression) {
            le(getter , value);
        }
        return (T) this;
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
    public T like(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.like,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
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
    public T like(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            like(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加左模糊查询条件，左边模糊匹配，即name like '%value'
     *
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public T likeLeft(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.likeLeft,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加左模糊查询条件，左边模糊匹配，即name like '%value'
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public T likeLeft(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            likeLeft(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加右模糊查询条件，右边模糊匹配，即name like 'value%'。mysql推荐用这种
     *
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public T likeRight(IGetter<T> getter , String value) {
    	conditions.add(new Condition(Operator.likeRight,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加右模糊查询条件，右边模糊匹配，即name like 'value%'。mysql推荐用这种
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值,不需要加%
     * @return 返回Query对象
     */
    public T likeRight(boolean expression, IGetter<T> getter , String value) {
        if (expression) {
            likeRight(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加IN条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T in(IGetter<T> getter , Collection<?> value) {
    	conditions.add(new Condition(Operator.in,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加IN条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T in(boolean expression, IGetter<T> getter , Collection<?> value) {
        if (expression) {
            in(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加IN条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T in(IGetter<T> getter , Object[] value) {
    	conditions.add(new Condition(Operator.in,LambdaUtil.getColumnName(getter), value));
        return (T) this;
    }

    /**
     * 根据表达式添加IN条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T in(boolean expression, IGetter<T> getter , Object[] value) {
        if (expression) {
            in(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加not in条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T notIn(IGetter<T> getter , Collection<?> value) {
    	conditions.add(new Condition(Operator.notIn,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加not in条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T notIn(boolean expression, IGetter<T> getter , Collection<?> value) {
        if (expression) {
            notIn(getter , value);
        }
        return (T) this;
    }


    /**
     * 添加not in条件
     *
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T notIn(IGetter<T> getter , Object[] value) {
    	conditions.add(new Condition(Operator.notIn,LambdaUtil.getColumnName(getter ), value));
        return (T) this;
    }

    /**
     * 根据表达式添加not in条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param value      值
     * @return 返回Query对象
     */
    public T notIn(boolean expression, IGetter<T> getter , Object[] value) {
        if (expression) {
            notIn(getter , value);
        }
        return (T) this;
    }

    /**
     * 添加between条件
     *
     * @param IGetter 方法函数
     * @param startValue 起始值
     * @param endValue   结束值
     * @return 返回Query对象
     */
    public T between(IGetter<T> getter , Object startValue, Object endValue) {
    	conditions.add(new Condition(Operator.between,LambdaUtil.getColumnName(getter ), startValue, endValue));
        return (T) this;
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
    public T between(boolean expression, IGetter<T> getter , Object startValue, Object endValue) {
        if (expression) {
            between(getter , startValue, endValue);
        }
        return (T) this;
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
    public T between(IGetter<T> getter , Object[] values) {
    	conditions.add(new Condition(Operator.between,LambdaUtil.getColumnName(getter ), values[0],values[1]));
        return (T) this;
    }

    /**
     * 添加between条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param values     存放起始值、结束值，只能存放2个值，values[0]表示开始值，value[1]表示结束值
     * @return 返回Query对象
     */
    public T between(boolean expression, IGetter<T> getter , Object[] values) {
        if (expression) {
            between(getter , values);
        }
        return (T) this;
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
    public T between(IGetter<T> getter , List<?> values) {
    	conditions.add(new Condition(Operator.between,LambdaUtil.getColumnName(getter ), values.get(0),values.get(1)));
        return (T) this;
    }

    /**
     * 添加between条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @param values     存放起始值、结束值，只能存放2个值
     * @return 返回Query对象
     */
    public T between(boolean expression, IGetter<T> getter , List<?> values) {
        if (expression) {
            between(getter , values);
        }
        return (T) this;
    }

    


    /**
     * 添加字段不为null的条件
     *
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public T notNull(IGetter<T> getter ) {
    	conditions.add(new Condition(Operator.isNotNull,LambdaUtil.getColumnName(getter )," IS NOT NULL"));
        return (T) this;
    }

    /**
     * 根据表达式添加字段不为null的条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public T notNull(boolean expression, IGetter<T> getter ) {
        if (expression) {
            notNull(getter );
        }
        return (T) this;
    }

    /**
     * 添加字段是null的条件
     *
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public T isNull(IGetter<T> getter ) {
    	conditions.add(new Condition(Operator.isNull,LambdaUtil.getColumnName(getter ) , " IS NULL"));
        return (T) this;
    }

    /**
     * 根据表达式添加字段是null的条件
     *
     * @param expression 表达式，当为true时添加条件
     * @param IGetter 方法函数
     * @return 返回Query对象
     */
    public T isNull(boolean expression, IGetter<T> getter ) {
        if (expression) {
            isNull(getter );
        }
        return (T) this;
    }
    
	/**
	 * 查询条件
	 * @param conditionSql
	 * @return
	 */
	public T condition(String conditionSql) {
		joint();
		conditions.add(new Condition(Operator.condition,conditionSql,null));
		return (T) this;
	}
	
	public T groupBy(IGetter<T> getter) {
		if(CollectionUtils.isNotEmpty(groupBy)) {
			this.groupBy = new ArrayList<>();
		}
		groupBy.add(new Condition(Operator.group,LambdaUtil.getColumnName(getter),""));
		return (T) this;
	}
	
	public T orderBy(IGetter<T> getter,String orderType) {
		if(CollectionUtils.isNotEmpty(orderBy)) {
			this.orderBy = new ArrayList<>();
		}
		orderBy.add(new Condition(Operator.order,LambdaUtil.getColumnName(getter),orderType));
		return (T) this;
	}

	public List<Condition> getGroupBy() {
		return groupBy;
	}

	public List<Condition> getConditions() {
		return conditions;
	}
	
	@Override
	public String toString() {
		return "Query [conditions=" + conditions + ", orderBy=" + orderBy + "]";
	}
}
