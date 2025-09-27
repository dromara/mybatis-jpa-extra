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

import org.apache.ibatis.annotations.UpdateProvider;
import org.dromara.mybatis.jpa.provider.MapperProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;

/**
 * IJpa IJpaUpdateMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaUpdateMapper<T> {

	@UpdateProvider(type = MapperProvider.class, method = "update")
	public Integer update(T entity);

	@UpdateProvider(type = MapperProvider.class, method = "updateByQuery")
	public Integer updateByQuery(Class<?> entityClass , String setSql, Query query);	
	
	@UpdateProvider(type = MapperProvider.class, method = "updateByLambdaQuery")
	public Integer updateByLambdaQuery(Class<?> entityClass , String setSql, LambdaQuery<T> lambdaQuery);		
	
	@UpdateProvider(type = MapperProvider.class, method = "updateByUpdateWrapper")
	public Integer updateByUpdateWrapper(Class<?> entityClass , UpdateWrapper updateWrapper);	
	
	@UpdateProvider(type = MapperProvider.class, method = "updateByLambdaUpdateWrapper")
	public Integer updateByLambdaUpdateWrapper(Class<?> entityClass ,LambdaUpdateWrapper<T> lambdaUpdateWrapper);	
	
}
