/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.dromara.mybatis.jpa.provider.MapperSqlProvider;
import org.dromara.mybatis.jpa.query.Query;

/**
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaMapper<T> {
	
	@SelectProvider(type = MapperSqlProvider.class, method = "query")
	public List<T> query(T entity);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "filterByQuery")
	public List<T> filterByQuery(T entity,Query query);
	
	//follow function for Query
	public List<T> queryPageResults(T entity);

	@SelectProvider(type = MapperSqlProvider.class, method = "queryPageResultsCount")
	public Integer queryPageResultsCount(T entity);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "findAll")
	public List<T> findAll(@Param ("entityClass")Class<?> entityClass);

	/**
	 *  query by id
	 * @param id
	 * @return one 
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "get")
	public T get(
					@Param ("entityClass")Class<?> entityClass,
					@Param ("id") String id);
	
	
	//follow function for insert update and delete
	@InsertProvider(type = MapperSqlProvider.class, method = "insert")
	public Integer insert(T entity);
	
	@UpdateProvider(type = MapperSqlProvider.class, method = "update")
	public Integer update(T entity);


	/**
	 * delete by entity parameter
	 * @param entity
	 * @return
	 */
	public Integer delete(T entity);
	/**
	 * delete by id
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MapperSqlProvider.class, method = "remove")
	public Integer remove(	@Param ("entityClass")Class<?> entityClass,
							@Param ("id") String id);
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteBatch")
	public Integer deleteBatch(	
							@Param ("entityClass")Class<?> entityClass,
							@Param ("idList") List<String> idList);	
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "logicDelete")
	public Integer logicDelete(	
							@Param ("entityClass")	Class<?> 	entityClass,
							@Param ("idList") 		List<String> idList);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "find")
	public List<T> find(	@Param ("entityClass")	Class<?> 	entityClass,
							@Param ("filter")		String 		filter,
							@Param ("args") 		Object[] 	args, 
							@Param ("argTypes") 	int[] 		argTypes);
	
	
	
}