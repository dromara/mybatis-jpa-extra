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
 

package org.dromara.mybatis.jpa.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.dromara.mybatis.jpa.metadata.MetadataConstants;
import org.dromara.mybatis.jpa.provider.MapperSqlProvider;

/**
 * IJpa IJpaFindMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaFindMapper<T> {
	
	/**
	 * 查询所有数据
	 * @param entityClass
	 * @return
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "findAll")
	public List<T> findAll(@Param (MetadataConstants.ENTITY_CLASS)Class<?> entityClass);

	/**
	 * 根据ids列表查询
	 * @param entityClass
	 * @param idList
	 * @param partitionKey
	 * @return
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "findByIds")
	public List<T> findByIds(	
							@Param (MetadataConstants.ENTITY_CLASS)			Class<?> 	entityClass,
							@Param (MetadataConstants.PARAMETER_ID_LIST) 		List<String> idList,
							@Param (MetadataConstants.PARAMETER_PARTITION_KEY) String partitionKey);

	/**
	 * 根据给定的过滤条件，参数，参数类型查询
	 * @param entityClass
	 * @param filter
	 * @param args
	 * @param argTypes
	 * @return
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "find")
	public List<T> find(	@Param (MetadataConstants.ENTITY_CLASS)	Class<?> 	entityClass,
							@Param (MetadataConstants.QUERY_FILTER)	String 		filter,
							@Param (MetadataConstants.QUERY_ARGS) 		Object[] 	args, 
							@Param (MetadataConstants.QUERY_ARGTYPES) 	int[] 		argTypes);
	
}
