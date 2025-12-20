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
 

package org.dromara.mybatis.jpa.repository;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;


/**
 * JPA Base Repository
 * @author Crystal.Sea
 *
 * @param <T>
 */
public  interface  IJpaRepository <T> {
    
    //follow function for fetch page
    /**
     * 分页查询，JpaPage 和 entity
     * @param page
     * @param entity
     * @return
     */
    public JpaPageResults<T> fetch(JpaPage page , T entity);
    
    /**
     * 分页查询，支持Query
     * @param page
     * @param query
     * @return
     */
    public JpaPageResults<T> fetch(JpaPage page ,Query query);
    
    /**
     * 分页查询，支持Lambda Query
     * @param page
     * @param lambdaQuery
     * @return
     */
    public JpaPageResults<T> fetch(JpaPage page ,LambdaQuery<T> lambdaQuery); 
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    public JpaPageResults<T> fetchPageResults(T entity);
    
    public JpaPageResults<T> fetchPageResults(JpaPage page , T entity) ;
    
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    public JpaPageResults<T> fetchPageResults(String mapperId,T entity);
    
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    public JpaPageResults<T> fetchPageResults(String mapperId,JpaPage page ,T entity);

    /**
     * select with filter and args
     * 
     * <pre>
     * find(" StdNo = ? or StdNo = ?",new Object[]{"10024","10004"},new int[]{Types.VARCHAR,Types.INTEGER})
     * </pre>
     * @param filter
     * @param args
     * @param argTypes
     * @return List<T>
     * 
     */
    public List<T> find(String filter , Object[] args , int[] argTypes) ;
    
    /**
     * select with filter 
     * <pre>
     * find(" StdNo = '10024')
     * </pre>
     * @param filter
     * @return List<T>
     */
    public List<T> find(String filter) ;
    
    /**
     * select with filter and args
     * @param filter
     * @param args
     * @param argTypes
     * @return T
     */
    public T findOne(String filter , Object[] args , int[] argTypes) ;
    
    /**
     * select with filter
     * <pre>
     * find(" StdNo = '10024')
     * </pre>
     * @param filter
     * @return T
     */
    public T findOne(String filter) ;
    
    /**
     * find one entity by id 
     * @param id
     * @return
     */
    public T findById(String id);
    /**
     * find entity by id List
     * @param idList
     * @return List<T>
     */
    public List<T> findByIds(List<String> idList) ;
    
    /**
     * find entity by id List
     * @param idList
     * @param partitionKey
     * @return List<T>
     */
    public List<T> findByIds(List<String> idList,String partitionKey) ;
    
    /**
     * findAll data
     * @return
     */
    public List<T> findAll() ;
    
    /**
     * get one entity by entity id
     * @param id
     * @return
     */
    public T get(String id) ;
    
    /**
     * get one entity by entity id
     * @param id
     * @param partitionKey
     * @return T
     */
    public T get(String id,String partitionKey) ;
    
    /**
     *  get one entity by entity 
     * @param entity
     * @return
     */
    public T get(T entity) ;
    
    /**
     *  get entity by Query 
     * @param entity
     * @return
     */
    public T get(Query query) ;
    
    /**
     *  get  entity by LambdaQuery 
     * @param entity
     * @return
     */
    public T get(LambdaQuery<T> lambdaQuery) ;
    
    //follow function for query
    /**
     *  query list entity by entity 
     * @param entity
     * @return
     */
    public List<T> query(T entity) ;
    
    
    /**
     *  query list entity by Query 
     * @param entity
     * @return
     */
    public List<T> query(Query query) ;
    
    /**
     *  query list entity by LambdaQuery 
     * @param entity
     * @return
     */
    public List<T> query(LambdaQuery<T> lambdaQuery) ;
    
    /**
     *  count by Query 
     * @param entity
     * @return
     */
    public long count(Query query) ;
    
    /**
     *  count by LambdaQuery 
     * @param entity
     * @return
     */
    public long count(LambdaQuery<T> lambdaQuery) ;
    
    /**
     * exists by query
     * @param query
     * @return boolean
     */
    public boolean exists(Query query) ;
    
    /**
     * exists by lambdaQuery
     * @param lambdaQuery
     * @return boolean
     */
    public boolean exists(LambdaQuery<T> lambdaQuery) ;

    //follow function for insert update and delete
    /**
     * insert new entity
     * @param entity
     * @return
     */
    public boolean insert(T entity) ;
    
    /**
     * insert entity with batch
     * @param listEntity
     * @return
     */
    public boolean insertBatch(List<T> listEntity);
    
    /**
     * JPA persist ,  save
     * @param entity
     * @return boolean
     */
    public boolean persist(T entity) ;
    
    /**
     * JPA merge , save or update
     * @param entity
     * @return boolean
     */
    public boolean merge(T entity) ;
    
    /**
     * update entity
     * @param entity
     * @return
     */
    public boolean update(T entity) ;
    
    /**
     *  update entity by Query 
     * @param setSql
     * @param query
     * @return
     */
    public boolean update(String setSql , Query query) ;
    
    /**
     *  update entity by Query 
     * @param setSql
     * @param query
     * @return
     */
    public boolean update(String setSql , LambdaQuery <T> lambdaQuery) ;
    
    /**
     * update by updateWrapper
     * @param updateWrapper
     * @return
     */
    public boolean update(UpdateWrapper updateWrapper) ;
    
    /**
     * update by lambdaUpdateWrapper
     * @param lambdaUpdateWrapper
     * @return
     */
    public boolean update(LambdaUpdateWrapper <T> lambdaUpdateWrapper) ;
    
    /**
     * delete entity by Query
     * @param Query
     * @return
     */
    public boolean delete(Query query) ;
    
    /**
     * delete entity by LambdaQuery
     * @param Query
     * @return
     */
    public boolean delete(LambdaQuery<T> lambdaQuery) ;
    
    /**
     * batch delete entity by id List
     * @param idList
     * @return boolean
     */
    public boolean deleteBatch(List<String> idList) ;
    
    /**
     * batch delete entity by id List
     * @param idList
     * @param partitionKey
     * @return boolean
     */
    public boolean deleteBatch(List<String> idList,String partitionKey) ;
    
    /**
     * delete one entity by id
     * @param id
     * @return
     */
    public boolean delete(String id);
    
    /**
     * delete one entity by id
     * @param id
     * @param partitionKey
     * @return
     */
    public boolean delete(String id,String partitionKey);
    
    /**
     * logicDelete entity by ids
     * @param idList
     * @return
     */
    public boolean softDelete(List<String> idList) ;
    
    /**
     * logicDelete entity by ids
     * @param idList
     * @param partitionKey
     * @return
     */
    public boolean softDelete(List<String> idList,String partitionKey) ;
    
    /**
     * logicDelete entity by id
     * @param id string
     * @return
     */
    public boolean softDelete(String id) ;
    
    /**
     * logic Delete entity by Query
     * @param Query
     * @return
     */
    public boolean softDelete(Query query) ;
    
    /**
     * logic Delete entity by Query
     * @param Query
     * @return
     */
    public boolean softDelete(LambdaQuery <T> lambdaQuery) ;

}
