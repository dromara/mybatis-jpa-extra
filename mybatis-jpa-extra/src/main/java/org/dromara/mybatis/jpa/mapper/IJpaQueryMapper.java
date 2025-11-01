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
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.provider.MapperProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;

/**
 * IJpa QueryMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaQueryMapper<T> {
    
    /**
     *  query by id
     * @param id
     * @return one 
     */
    @SelectProvider(type = MapperProvider.class, method = "get")
    public T get(
                    @Param (ConstMetadata.ENTITY_CLASS)Class<?> entityClass,
                    @Param (ConstMetadata.PARAMETER_ID) String id,
                    @Param (ConstMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
    
    @SelectProvider(type = MapperProvider.class, method = "query")
    public List<T> query(T entity);
    
    @SelectProvider(type = MapperProvider.class, method = "queryByQuery")
    public List<T> queryByQuery(Class<?> entityClass,Query query);
    
    @SelectProvider(type = MapperProvider.class, method = "queryByLambdaQuery")
    public List<T> queryByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery);
    
    @SelectProvider(type = MapperProvider.class, method = "countByQuery")
    public long countByQuery(Class<?> entityClass,Query query);
    
    @SelectProvider(type = MapperProvider.class, method = "countByLambdaQuery")
    public long countByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery);
}
