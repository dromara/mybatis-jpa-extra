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

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.provider.CrudMapperProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;

/**
 * IJpa IJpaCrudMapper
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaCrudMapper<T, ID extends Serializable> {
	
	//get
	/**
     *  query by id
     * @param id
     * @return one 
     */
    @SelectProvider(type = CrudMapperProvider.class, method = "get")
    public T get(
                    @Param (ConstMetadata.ENTITY_CLASS)            Class<?> entityClass,
                    @Param (ConstMetadata.PARAMETER_ID)            ID id,
                    @Param (ConstMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
    
    //follow function for delete
	/**
     * delete by id
     * @param id
     * @return
     */
    @DeleteProvider(type = CrudMapperProvider.class, method = "deleteById")
    public Integer deleteById(@Param (ConstMetadata.ENTITY_CLASS)          Class<?> entityClass,
                            @Param (ConstMetadata.PARAMETER_ID)            ID id,
                            @Param (ConstMetadata.PARAMETER_PARTITION_KEY) String partitionKey);
    
    /**
     * delete by idList
     * @param entityClass
     * @param idList
     * @param partitionKey
     * @return
     */
    @DeleteProvider(type = CrudMapperProvider.class, method = "deleteBatch")
    public Integer deleteBatch(    
                            @Param (ConstMetadata.ENTITY_CLASS)            Class<?> entityClass,
                            @Param (ConstMetadata.PARAMETER_ID_LIST)       List<ID> idList,
                            @Param (ConstMetadata.PARAMETER_PARTITION_KEY) String partitionKey);   
    
    /**
     * delete by Query
     * @param entityClass
     * @param query
     * @return
     */
    @DeleteProvider(type = CrudMapperProvider.class, method = "deleteByQuery")
    public Integer deleteByQuery(Class<?> entityClass , Query query);    
    
    /**
     * delete by LambdaQuery
     * @param entityClass
     * @param lambdaQuery
     * @return
     */
    @DeleteProvider(type = CrudMapperProvider.class, method = "deleteByLambdaQuery")
    public Integer deleteByLambdaQuery(Class<?> entityClass , LambdaQuery<T> lambdaQuery);    
    
    //follow function for insert or save
    @InsertProvider(type = CrudMapperProvider.class, method = "insert")
    public Integer insert(T entity);
    
    @InsertProvider(type = CrudMapperProvider.class, method = "insertBatch")
    public Integer insertBatch(List<T> listEntity);
    
    //update
    @UpdateProvider(type = CrudMapperProvider.class, method = "update")
    public Integer update(T entity);

    @UpdateProvider(type = CrudMapperProvider.class, method = "updateByQuery")
    public Integer updateByQuery(Class<?> entityClass , String setSql, Query query);    
    
    @UpdateProvider(type = CrudMapperProvider.class, method = "updateByLambdaQuery")
    public Integer updateByLambdaQuery(Class<?> entityClass , String setSql, LambdaQuery<T> lambdaQuery);            
    
    @UpdateProvider(type = CrudMapperProvider.class, method = "updateByUpdateWrapper")
    public Integer updateByUpdateWrapper(Class<?> entityClass , UpdateWrapper updateWrapper);    
    
    @UpdateProvider(type = CrudMapperProvider.class, method = "updateByLambdaUpdateWrapper")
    public Integer updateByLambdaUpdateWrapper(Class<?> entityClass ,LambdaUpdateWrapper<T> lambdaUpdateWrapper);    

    //query
    @SelectProvider(type = CrudMapperProvider.class, method = "query")
    public List<T> query(T entity);
    
    @SelectProvider(type = CrudMapperProvider.class, method = "queryByQuery")
    public List<T> queryByQuery(Class<?> entityClass,Query query);
    
    @SelectProvider(type = CrudMapperProvider.class, method = "queryByLambdaQuery")
    public List<T> queryByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery);
    
    //count
    @SelectProvider(type = CrudMapperProvider.class, method = "countByQuery")
    public long countByQuery(Class<?> entityClass,Query query);
    
    @SelectProvider(type = CrudMapperProvider.class, method = "countByLambdaQuery")
    public long countByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery);
    
}
