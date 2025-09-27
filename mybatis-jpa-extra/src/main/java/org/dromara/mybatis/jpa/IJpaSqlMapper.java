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
 

package org.dromara.mybatis.jpa;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.dromara.mybatis.jpa.provider.SqlMapperProvider;

/**
 * IJpa Sql Mapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaSqlMapper {

   /**
    * 查询数据返回
    *
    * @param sql   sql语句
    * @param parameters 参数
    * @return List<Map < String, Object>>
    */
	@SelectProvider(type = SqlMapperProvider.class, method = "selectList")
	List<Map<String, Object>> selectList(Map<String, Object> parameters);

   /**
    * 插入数据
    *
    * @param sql   sql语句
    * @param entity 参数
    * @return int
    */
	@InsertProvider(type = SqlMapperProvider.class, method = "insert")
	int insert(Map<String, Object> entity);

   /**
    * 更新数据
    *
    * @param sql   sql语句
    * @param entity 参数
    * @return int
    */
	@UpdateProvider(type = SqlMapperProvider.class, method = "update")
	int update(Map<String, Object> entity);

   /**
    * 删除数据
    *
    * @param sql   sql语句
    * @param parameters 参数
    * @return int
    */
	@DeleteProvider(type = SqlMapperProvider.class, method = "delete")
	int delete(Map<String, Object> parameters);			
}
