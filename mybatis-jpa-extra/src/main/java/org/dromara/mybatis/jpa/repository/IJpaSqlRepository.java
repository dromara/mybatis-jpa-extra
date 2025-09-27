/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.repository;

import java.util.List;
import java.util.Map;

import org.dromara.mybatis.jpa.IJpaSqlMapper;

/**
 * ISqlRepository
 */
public interface IJpaSqlRepository {

	/**
	 * 
	 * @return IJpaSqlMapper
	 */
	public IJpaSqlMapper getMapper();
	
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
     * @param parameters 参数
     * @return List<Map < String, Object>>
     */
    List<Map<String, Object>> selectList(String sql, Map<String, Object> parameters);
    
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
     * @param entity 参数
     * @return int
     */
    int insert(String sql, Map<String, Object> entity);

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
     * @param entity 参数
     * @return int
     */
    int update(String sql, Map<String, Object> entity);

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
     * @param parameters 参数
     * @return int
     */
    int delete(String sql, Map<String, Object> parameters);
}
