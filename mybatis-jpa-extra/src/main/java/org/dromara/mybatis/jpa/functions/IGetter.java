package org.dromara.mybatis.jpa.functions;

import java.io.Serializable;

/**
 * Entity::getter()函数
 *
 */
@FunctionalInterface
public interface IGetter<T> extends Serializable {
    /**
     * 返回属性值
     *
     * @param entity Entity
     * @return 属性值
     */
    Object get(T entity);
}