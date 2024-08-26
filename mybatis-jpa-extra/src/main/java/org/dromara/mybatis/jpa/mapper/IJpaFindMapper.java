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
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.provider.MapperSqlProvider;

/**
 * IJpa IJpaFindMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaFindMapper<T> {
	
	@SelectProvider(type = MapperSqlProvider.class, method = "findAll")
	public List<T> findAll(@Param (MapperMetadata.ENTITY_CLASS)Class<?> entityClass);

	@SelectProvider(type = MapperSqlProvider.class, method = "findByIds")
	public List<T> findByIds(	
							@Param (MapperMetadata.ENTITY_CLASS)			Class<?> 	entityClass,
							@Param (MapperMetadata.PARAMETER_ID_LIST) 		List<String> idList,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);

	@SelectProvider(type = MapperSqlProvider.class, method = "find")
	public List<T> find(	@Param (MapperMetadata.ENTITY_CLASS)	Class<?> 	entityClass,
							@Param (MapperMetadata.QUERY_FILTER)	String 		filter,
							@Param (MapperMetadata.QUERY_ARGS) 		Object[] 	args, 
							@Param (MapperMetadata.QUERY_ARGTYPES) 	int[] 		argTypes);
	
}
