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
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.provider.MapperSqlProvider;
import org.dromara.mybatis.jpa.query.Query;

/**
 * IJpa Mapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaMapper<T> {
	
	@SelectProvider(type = MapperSqlProvider.class, method = "query")
	public List<T> query(T entity);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "filterByCondition")
	public List<T> filterByCondition(T entity,Query query);

	@SelectProvider(type = MapperSqlProvider.class, method = "findAll")
	public List<T> findAll(@Param (MapperMetadata.ENTITY_CLASS)Class<?> entityClass);

	
	@SelectProvider(type = MapperSqlProvider.class, method = "fetchCount")
	public Integer fetchCount(JpaPage page);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "fetch")
	public List<T> fetch(
					@Param (MapperMetadata.PAGE)JpaPage page,
					@Param (MapperMetadata.ENTITY) T entity);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "fetchByCondition")
	public List<T> fetchByCondition(
					@Param (MapperMetadata.PAGE) JpaPage page,
					@Param (MapperMetadata.CONDITION) Query query,
					@Param (MapperMetadata.ENTITY_CLASS)Class<?> entityClass);

	/**
	 *  query by id
	 * @param id
	 * @return one 
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "get")
	public T get(
					@Param (MapperMetadata.ENTITY_CLASS)Class<?> entityClass,
					@Param (MapperMetadata.PARAMETER_ID) String id,
					@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
	
	
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
	public Integer remove(	@Param (MapperMetadata.ENTITY_CLASS)			Class<?> entityClass,
							@Param (MapperMetadata.PARAMETER_ID) String id,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
		
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteBatch")
	public Integer deleteBatch(	
							@Param (MapperMetadata.ENTITY_CLASS)			Class<?> entityClass,
							@Param (MapperMetadata.PARAMETER_ID_LIST)	 	List<String> idList,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);	
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "logicDelete")
	public Integer logicDelete(	
							@Param (MapperMetadata.ENTITY_CLASS)			Class<?> 	entityClass,
							@Param (MapperMetadata.PARAMETER_ID_LIST) 		List<String> idList,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "find")
	public List<T> find(	@Param (MapperMetadata.ENTITY_CLASS)	Class<?> 	entityClass,
							@Param (MapperMetadata.QUERY_FILTER)	String 		filter,
							@Param (MapperMetadata.QUERY_ARGS) 		Object[] 	args, 
							@Param (MapperMetadata.QUERY_ARGTYPES) 	int[] 		argTypes);
	
	
	
}
