package org.dromara.mybatis.jpa;

import java.util.List;
import java.util.Map;

public interface SqlRepository {

	 /**
     * 查询数据返回
     *
     * @param sql sql语句
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> selectList(String sql);

    /**
     * 查询数据返回
     *
     * @param sql   sql语句
     * @param value 参数
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> selectList(String sql, Object value);
    
    /**
     * 插入数据
     *
     * @param sql sql语句
     * @return int
     */
    int insert(String sql);

    /**
     * 插入数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int insert(String sql, Object value);

    /**
     * 更新数据
     *
     * @param sql sql语句
     * @return int
     */
    int update(String sql);

    /**
     * 更新数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int update(String sql, Object value);

    /**
     * 删除数据
     *
     * @param sql sql语句
     * @return int
     */
    int delete(String sql);

    /**
     * 删除数据
     *
     * @param sql   sql语句
     * @param value 参数
     * @return int
     */
    int delete(String sql, Object value);
}
