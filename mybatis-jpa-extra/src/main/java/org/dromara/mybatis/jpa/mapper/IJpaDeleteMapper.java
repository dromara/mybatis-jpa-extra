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
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Param;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.dromara.mybatis.jpa.provider.MapperSqlProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;

/**
 * IJpa IJpaDeleteMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaDeleteMapper<T> {
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteByQuery")
	public Integer deleteByQuery(Class<?> entityClass , Query query);	
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteByLambdaQuery")
	public Integer deleteByLambdaQuery(Class<?> entityClass , LambdaQuery<T> lambdaQuery);	
	
	/**
	 * delete by id
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteById")
	public Integer deleteById(	@Param (MapperMetadata.ENTITY_CLASS)			Class<?> entityClass,
							@Param (MapperMetadata.PARAMETER_ID) String id,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
		
	@DeleteProvider(type = MapperSqlProvider.class, method = "deleteBatch")
	public Integer deleteBatch(	
							@Param (MapperMetadata.ENTITY_CLASS)			Class<?> entityClass,
							@Param (MapperMetadata.PARAMETER_ID_LIST)	 	List<String> idList,
							@Param (MapperMetadata.PARAMETER_PARTITION_KEY) String partitionKey);	
		
}
