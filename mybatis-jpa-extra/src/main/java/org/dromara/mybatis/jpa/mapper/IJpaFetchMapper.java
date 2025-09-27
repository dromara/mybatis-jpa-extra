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
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.metadata.MetadataConstants;
import org.dromara.mybatis.jpa.provider.MapperProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;

/**
 * IJpa IJpaFetchMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaFetchMapper<T>{
	
	@SelectProvider(type = MapperProvider.class, method = "fetchCount")
	public Integer fetchCount(JpaPage page);
	
	@SelectProvider(type = MapperProvider.class, method = "fetch")
	public List<T> fetch(
					@Param (MetadataConstants.PAGE)JpaPage page,
					@Param (MetadataConstants.ENTITY) T entity);
	
	@SelectProvider(type = MapperProvider.class, method = "fetchByQuery")
	public List<T> fetchByQuery(
					@Param (MetadataConstants.PAGE) JpaPage page,
					@Param (MetadataConstants.CONDITION) Query query,
					@Param (MetadataConstants.ENTITY_CLASS)Class<?> entityClass);
	
	@SelectProvider(type = MapperProvider.class, method = "fetchByLambdaQuery")
	public List<T> fetchByLambdaQuery(
					@Param (MetadataConstants.PAGE) JpaPage page,
					@Param (MetadataConstants.CONDITION) LambdaQuery<T> lambdaQuery,
					@Param (MetadataConstants.ENTITY_CLASS)Class<?> entityClass);
	

	//
	public List<T> fetchPageResults(T entity);
	
	public List<T> fetchPageResults(@Param (MetadataConstants.PAGE) JpaPage page ,@Param (MetadataConstants.ENTITY) T entity);
		
}
