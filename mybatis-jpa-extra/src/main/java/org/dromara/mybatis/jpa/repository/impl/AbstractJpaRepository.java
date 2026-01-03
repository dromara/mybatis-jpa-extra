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
 

package org.dromara.mybatis.jpa.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.repository.IJpaRepository;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;
import org.dromara.mybatis.jpa.util.InstanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Abstract JPA Repository
 * @author Crystal.Sea
 *
 * @param <T>
 */
public abstract class  AbstractJpaRepository <M extends IJpaMapper<T, ID>, T extends JpaEntity, ID extends Serializable > implements IJpaRepository<T, ID>{
    private static final  Logger logger = LoggerFactory.getLogger(AbstractJpaRepository.class);
    
    /**
     * entity Class
     */
    @SuppressWarnings("rawtypes")
    private Class entityClass;
    
    private M mapper;

    public M getMapper() {
        return mapper;
    }
    
    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public AbstractJpaRepository() {
        init();
    }
    
    public AbstractJpaRepository(M mapper) {
        init();
        this.mapper = mapper;
    }
    
    @SuppressWarnings({"unchecked","rawtypes"})
    protected void init() {
        Class mapperClass = null;
        Type[] pType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        if (pType != null && pType.length >= 2) {
            mapperClass=(Class<T>) pType[0];
            this.entityClass = (Class<T>) pType[1];
            if(logger.isTraceEnabled()) {
                logger.trace("Mapper {} , Entity {}" , String.format(ConstMetadata.LOG_FORMAT, mapperClass.getSimpleName()),entityClass.getSimpleName());
            }
        } else {
            logger.error("invalide initail, need generic type parameter! ");
        }
    }

    //follow function for fetch page
    /**
     * 分页查询，JpaPage 和 entity
     * @param page
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public JpaPageResults<T> fetch(JpaPage page , T entity) {
        try {
            page.build();
            List<T> resultslist = getMapper().fetch(page, entity);
            return (JpaPageResults<T>) buildPageResults(page , resultslist);
        }catch (Exception e) {
            logger.error("fetch Exception " , e);
        }
        return null;
    }
    
    /**
     * 分页查询，支持Query
     * @param page
     * @param query
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public JpaPageResults<T> fetch(JpaPage page ,Query query) {
        try {
            page.build();
            List<T> resultslist = getMapper().fetchByQuery(page, query , this.entityClass);
            return (JpaPageResults<T>) buildPageResults(page , resultslist);
        }catch (Exception e) {
            logger.error("fetch by Query Exception " , e);
        }
        return null;
    }
    
    /**
     * 分页查询，支持Lambda Query
     * @param page
     * @param lambdaQuery
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public JpaPageResults<T> fetch(JpaPage page ,LambdaQuery<T> lambdaQuery) {
        try {
            page.build();
            List<T> resultslist = getMapper().fetchByLambdaQuery(page, lambdaQuery , this.entityClass);
            return (JpaPageResults<T>) buildPageResults(page , resultslist);
        }catch (Exception e) {
            logger.error("fetch by LambdaQuery Exception " , e);
        }
        return null;
    }
    
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public JpaPageResults<T> fetchPageResults(T entity) {
        try {
            entity.build();
            List<T> resultslist = getMapper().fetchPageResults(entity);
            return (JpaPageResults<T>) buildPageResults(entity , resultslist);
        }catch (Exception e) {
            logger.error("fetchPageResults Exception " , e);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public JpaPageResults<T> fetchPageResults(JpaPage page , T entity) {
        try {
            entity.build();
            List<T> resultslist = getMapper().fetchPageResults(page , entity);
            return (JpaPageResults<T>) buildPageResults(entity , resultslist);
        }catch (Exception e) {
            logger.error("fetchPageResults page Exception " , e);
        }
        return null;
    }
    
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    public JpaPageResults<T> fetchPageResults(String mapperId,T entity) {
        return fetchPageResults(mapperId , null , entity);
    }
    
    /**
     * query page list entity by entity 
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public JpaPageResults<T> fetchPageResults(String mapperId,JpaPage page ,T entity) {
        try {
            entity.build();
            List<T> resultslist = (List<T>)InstanceUtil.invokeMethod(getMapper(), mapperId, 
                    page == null ? new Object[]{entity} : new Object[]{page , entity});
            return (JpaPageResults<T>) buildPageResults(entity , resultslist);
        }catch (NoSuchMethodException e) {
            logger.error("Mapper no fetchPageResults Method Exception " , e);
        }catch (Exception e) {
            logger.error("fetchPageResults Exception " , e);
        }
        return null;
    }

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
    public List<T> find(String filter , Object[] args , int[] argTypes) {
        try {
            return getMapper().find(this.entityClass,filter ,args , argTypes);
        } catch(Exception e) {
            logger.error("findAll Exception " , e);
        }
        return Collections.emptyList();
    }
    
    /**
     * select with filter 
     * <pre>
     * find(" StdNo = '10024')
     * </pre>
     * @param filter
     * @return List<T>
     */
    public List<T> find(String filter) {
        return find(filter ,null , null);
    }
    
    /**
     * select with filter and args
     * @param filter
     * @param args
     * @param argTypes
     * @return T
     */
    public T findOne(String filter , Object[] args , int[] argTypes) {
        try {
            List<T> findList = find(filter ,args , argTypes);
            return  CollectionUtils.isEmpty(findList) ? null : findList.get(0);
        } catch(Exception e) {
            logger.error("findOne filter Exception " , e);
        }
        return null;
    }
    
    /**
     * select with filter
     * <pre>
     * find(" StdNo = '10024')
     * </pre>
     * @param filter
     * @return T
     */
    public T findOne(String filter) {
        return findOne( filter ,null , null);
    }
    
    /**
     * find one entity by entity id
     * @param id
     * @return
     */
    public T findById(ID id) {
        return this.get(id);
    }
    
    /**
     * find entity by id List
     * @param idList
     * @return List<T>
     */
    public List<T> findByIds(List<ID> idList) {
        try {
            logger.trace("findByIds {}" , idList);
            List<T> findList = getMapper().findByIds(this.entityClass,idList,null);
            logger.trace("findByIds count : {}" , findList.size());
            return findList;
        } catch(Exception e) {
            logger.error("findByIds Exception " , e);
        }
        return Collections.emptyList();
    }
    
    /**
     * find entity by id List
     * @param idList
     * @param partitionKey
     * @return List<T>
     */
    public List<T> findByIds(List<ID> idList,String partitionKey) {
        try {
            logger.trace("findByIds {} , partitionKey {}" , idList , partitionKey);
            List<T> findList = getMapper().findByIds(this.entityClass , idList , partitionKey);
            logger.debug("findByIds count : {}" , findList.size());
            return findList;
        } catch(Exception e) {
            logger.error("findByIds Exception " , e);
        }
        return Collections.emptyList();
    }
    
    /**
     * findAll data
     * @return
     */
    public List<T> findAll() {
        try {
            return getMapper().findAll(this.entityClass);
        } catch(Exception e) {
            logger.error("findAll Exception" , e);
        }
        return Collections.emptyList();
    }
    
    /**
     * get one entity by entity id
     * @param id
     * @return
     */
    public T get(ID id) {
        try {
            logger.debug("entityClass  {} , primaryKey {}" , entityClass , id);
            return  getMapper().get(this.entityClass,id,null);
        } catch(Exception e) {
            logger.error("get Exception " , e);
        }
        return null;
    }
    
    /**
     * get one entity by entity id
     * @param id
     * @param partitionKey
     * @return T
     */
    public T get(ID id,String partitionKey) {
        try {
            logger.debug("entityClass  {} , primaryKey {} , partitionKey {}" , entityClass , id,partitionKey);
            return  getMapper().get(this.entityClass,id,partitionKey);
        } catch(Exception e) {
            logger.error("get Exception " , e);
        }
        return null;
    }
    
    /**
     *  get one entity by entity 
     * @param entity
     * @return
     */
    public T get(T entity) {
        try {
            List<T> queryList = query(entity);
            return CollectionUtils.isEmpty(queryList) ? null : queryList.get(0);
        } catch(Exception e) {
            logger.error("get by entity Exception " , e);
        }
        return null;
    }
    
    /**
     *  get entity by Query 
     * @param entity
     * @return
     */
    public T get(Query query) {
        try {
            List<T> queryList = query(query);
            return CollectionUtils.isEmpty(queryList) ? null : queryList.get(0);
        } catch(Exception e) {
            logger.error("get by Query Exception " , e);
        }
        return null;
    }
    
    /**
     *  get  entity by LambdaQuery 
     * @param entity
     * @return
     */
    public T get(LambdaQuery<T> lambdaQuery) {
        try {
            List<T> queryList = query(lambdaQuery);
            return CollectionUtils.isEmpty(queryList) ? null : queryList.get(0);
        } catch(Exception e) {
            logger.error("get by LambdaQuery Exception " , e);
        }
        return null;
    }
    
    //follow function for query
    /**
     *  query list entity by entity 
     * @param entity
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<T> query(T entity) {
        try {
            if(entity == null) {
                entity = (T) entityClass.getDeclaredConstructor().newInstance();
            }
            return getMapper().query(entity);
        } catch(Exception e) {
            logger.error("query by entity Exception " , e);
        }
        return Collections.emptyList();
    }
    
    
    /**
     *  query list entity by Query 
     * @param entity
     * @return
     */
    public List<T> query(Query query) {
        try {
            return getMapper().queryByQuery(entityClass,query);
        } catch(Exception e) {
            logger.error("query by Query Exception " , e);
        }
        return Collections.emptyList();
    }
    
    /**
     *  query list entity by LambdaQuery 
     * @param entity
     * @return
     */
    public List<T> query(LambdaQuery<T> lambdaQuery) {
        try {
            return getMapper().queryByLambdaQuery(entityClass,lambdaQuery);
        } catch(Exception e) {
            logger.error("query by LambdaQuery Exception " , e);
        }
        return Collections.emptyList();
    }
    
    /**
     *  count by Query 
     * @param entity
     * @return
     */
    public long count(Query query) {
        try {
            return getMapper().countByQuery(entityClass,query);
        } catch(Exception e) {
            logger.error("count by Query Exception " , e);
        }
        return 0;
    }
    
    /**
     *  count by LambdaQuery 
     * @param entity
     * @return
     */
    public long count(LambdaQuery<T> lambdaQuery) {
        try {
            return getMapper().countByLambdaQuery(entityClass,lambdaQuery);
        } catch(Exception e) {
            logger.error("count by LambdaQuery Exception " , e);
        }
        return 0;
    }
    
    /**
     * exists by query
     */
    @Override
    public boolean exists(Query query) {
        return count(query) > 0;
    }
    
    /**
     * exists by lambdaQuery
     */
    @Override
    public boolean exists(LambdaQuery<T> lambdaQuery) {
        return count(lambdaQuery) > 0;
    }

    //follow function for insert update and delete
    /**
     * insert new entity
     * @param entity
     * @return
     */
    public boolean insert(T entity) {
        try {
            Integer count = getMapper().insert(entity);
            logger.debug("insert count : {}" , count);
            return  count > 0;
        } catch(Exception e) {
            logger.error("insert Exception " , e);
        }
        return false;
    }
    
    /**
     * insert entity with batch
     * @param List<T> listEntity
     * @return
     */
    public boolean insertBatch(List<T> listEntity){
        try {
            if(CollectionUtils.isNotEmpty(listEntity)) {
            	    Integer count = getMapper().insertBatch(listEntity) ;
                logger.debug("Insert Batch count : {}" , count);
            }
        } catch(Exception e) {
            logger.error("Insert Batch Exception " , e);
        }
        return false;
    }
    
    /**
     * JPA persist ,  save
     * @param entity
     * @return boolean
     */
    public boolean persist(T entity) {
        return insert(entity);
    }
    
    /**
     * JPA merge , save or update
     * @param entity
     * @return boolean
     */
    public boolean merge(T entity) {
        List<T> resultList = query(entity);
        if(CollectionUtils.isEmpty(resultList)) {
            return insert(entity);
        }else {
            return update(entity);
        }
    }
    
    /**
     * update entity
     * @param entity
     * @return
     */
    public boolean update(T entity) {
        try {
            Integer count = getMapper().update(entity);
            logger.debug("update count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("update Exception " , e);
        }
        return false;
    }
    
    /**
     *  update entity by Query 
     * @param setSql
     * @param query
     * @return
     */
    public boolean update(String setSql , Query query) {
        try {
            Integer count =  getMapper().updateByQuery(entityClass,setSql,query);
            logger.debug("update by Query count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("update by Query Exception " , e);
        }
        return false;
    }
    
    /**
     *  update entity by Query 
     * @param setSql
     * @param query
     * @return
     */
    public boolean update(String setSql , LambdaQuery <T> lambdaQuery) {
        try {
            Integer count =  getMapper().updateByLambdaQuery(entityClass,setSql,lambdaQuery);
            logger.debug("update by LambdaQuery count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("update by LambdaQuery Exception " , e);
        }
        return false;
    }
    
    /**
     *  update by UpdateWrapper
     * @param UpdateWrapper
     */
    @Override
    public boolean update(UpdateWrapper updateWrapper) {
        try {
            Integer count =  getMapper().updateByUpdateWrapper(entityClass,updateWrapper);
            logger.debug("update by UpdateWrapper count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("update by UpdateWrapper Exception " , e);
        }
        return false;
    }

    /**
     *  update by LambdaUpdateWrapper
     * @param LambdaUpdateWrapper
     */
    @Override
    public boolean update(LambdaUpdateWrapper<T> lambdaUpdateWrapper) {
        try {
            Integer count =  getMapper().updateByLambdaUpdateWrapper(entityClass,lambdaUpdateWrapper);
            logger.debug("update by LambdaUpdateWrapper count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("update by LambdaUpdateWrapper Exception " , e);
        }
        return false;
    }
    
    /**
     * delete entity by Query
     * @param Query
     * @return
     */
    public boolean delete(Query query) {
        try {
            Integer count = getMapper().deleteByQuery(entityClass , query);
            logger.debug("delete by query count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("delete by query Exception " , e);
        }
        return false;
    }
    
    /**
     * delete entity by LambdaQuery
     * @param Query
     * @return
     */
    public boolean delete(LambdaQuery<T> lambdaQuery) {
        try {
            Integer count = getMapper().deleteByLambdaQuery(entityClass , lambdaQuery);
            logger.debug("delete by LambdaQuery count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("delete by LambdaQuery Exception " , e);
        }
        return false;
    }
    
    /**
     * batch delete entity by id List
     * @param idList
     * @return boolean
     */
    public boolean deleteBatch(List<ID> idList) {
        try {
            logger.trace("deleteBatch {}" , idList);
            Integer count = getMapper().deleteBatch(this.entityClass,idList,null);
            logger.debug("deleteBatch count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("deleteBatch Exception " , e);
        }
        return false;
    }
    
    /**
     * batch delete entity by id List
     * @param idList
     * @param partitionKey
     * @return boolean
     */
    public boolean deleteBatch(List<ID> idList,String partitionKey) {
        try {
            logger.trace("deleteBatch {} , partitionKey {}" , idList , partitionKey);
            Integer count = getMapper().deleteBatch(this.entityClass , idList , partitionKey);
            logger.debug("deleteBatch count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("deleteBatch Exception " , e);
        }
        return false;
    }
    
    /**
     * delete entity by id
     * @param id
     * @return boolean
     */
    public boolean deleteById(ID id){
        return this.delete(id);
    }
    
    /**
     * delete one entity by id
     * @param id
     * @return
     */
    public boolean delete(ID id){
        try {
            logger.debug("id {} " , id );
            Integer count=getMapper().deleteById(this.entityClass,id,null);
            logger.debug("delete by id count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("delete by id Exception " , e);
        }
        return false;
    }
    
    /**
     * delete one entity by id
     * @param id
     * @param partitionKey
     * @return
     */
    public boolean delete(ID id,String partitionKey){
        try {
            logger.debug("id {} , partitionKey {}" , id , partitionKey);
            Integer count = getMapper().deleteById(this.entityClass,id,partitionKey);
            logger.debug("delete by id and partitionKey count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("delete by id and partitionKey Exception " , e);
        }
        return false;
    }
    
    /**
     * logicDelete entity by ids
     * @param idList
     * @return
     */
    public boolean softDelete(List<ID> idList) {
        try {
            logger.trace("softDelete idList {}" , idList);
            Integer count = getMapper().softDelete(this.entityClass,idList,null);
            logger.debug("softDelete count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete by idList Exception " , e);
        }
        return true;
    }
    
    /**
     * logicDelete entity by ids
     * @param idList
     * @param partitionKey
     * @return
     */
    public boolean softDelete(List<ID> idList,String partitionKey) {
        try {
            logger.trace("softDelete idList {} , partitionKey {}" , idList , partitionKey);
            Integer count = getMapper().softDelete(this.entityClass,idList,partitionKey);
            logger.debug("softDelete idList and partitionKey count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete idList and partitionKey Exception " , e);
        }
        return true;
    }
    
    /**
     * logicDelete entity by id
     * @param id string
     * @return
     */
    public boolean softDelete(ID id) {
        try {
            logger.trace("softDelete id {}" , id );
            Integer count = getMapper().softDeleteById(this.entityClass,id,null);
            logger.debug("softDelete id count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete id Exception " , e);
        }
        return true;
    }
    
    /**
     * logicDelete entity by id
     * @param id string
     * @return
     */
    public boolean softDelete(ID id,String partitionKey) {
        try {
            logger.trace("softDelete id {} , partitionKey {}" , id , partitionKey);
            Integer count = getMapper().softDeleteById(this.entityClass,id,partitionKey);
            logger.debug("softDelete id and partitionKey count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete id and partitionKey Exception " , e);
        }
        return true;
    }
    
    /**
     * logic Delete entity by Query
     * @param Query
     * @return
     */
    public boolean softDelete(Query query) {
        try {
            Integer count = getMapper().softDeleteByQuery(entityClass , query);
            logger.debug("softDelete by Query count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete by Query Exception " , e);
        }
        return false;
    }
    
    /**
     * logic Delete entity by Query
     * @param Query
     * @return
     */
    public boolean softDelete(LambdaQuery <T> lambdaQuery) {
        try {
            Integer count = getMapper().softDeleteByLambdaQuery(entityClass , lambdaQuery);
            logger.debug("softDelete by LambdaQuery count : {}" , count);
            return count > 0;
        } catch(Exception e) {
            logger.error("softDelete by LambdaQuery Exception " , e);
        }
        return false;
    }
    
    //follow is  for query paging
    /**
     * query Count by entity 
     * @param entity
     * @return
     */
    protected Integer fetchCount(JpaPage page) {
        Integer count = 0;
        try {
            count = getMapper().fetchCount(page);
            logger.debug("fetchCount count : {}" , count);
        } catch(Exception e) {
            logger.error("fetchCount Exception " , e);
        }
        return count;
    }

    protected JpaPageResults<?> buildPageResults(JpaPage page , List<?> resultslist) {
        //当前页记录数
        Integer records = JpaPageResults.parseRecords(resultslist);
        //总页数
        Integer totalCount = fetchCount(page, resultslist);
        //构建返回对象
        return new JpaPageResults<>(page.getPageNumber(),page.getPageSize(),records,totalCount,resultslist);
    }
    
    /**
     * 获取总页数
     * @param page
     * @param records
     * @return
     */
    protected Integer fetchCount(JpaPage page ,List<?> resultslist) {
        Integer totalCount = 0;
        page.setPageable(false);
        Integer records = JpaPageResults.parseRecords(resultslist);
        if(page.getPageNumber() == 1 && records < page.getPageSize()) {
            totalCount = records;
        }else {
            totalCount = JpaPageResults.parseCount(getMapper().fetchCount(page));
        }
        return totalCount;
    }
}
