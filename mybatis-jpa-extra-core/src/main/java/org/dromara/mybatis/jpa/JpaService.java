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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.entity.JpaPageResultsSqlCache;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.dromara.mybatis.jpa.util.BeanUtil;
import org.dromara.mybatis.jpa.util.InstanceUtil;
import org.dromara.mybatis.jpa.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


/**
 * JPA Service
 * @author Crystal.Sea
 *
 * @param <T>
 */
public  class  JpaService <T extends JpaEntity> {
	
	static final  Logger logger = LoggerFactory.getLogger(JpaService.class);
	
	@JsonIgnore
	//定义全局缓存
	public static final Cache<String, JpaPageResultsSqlCache> pageResultsBoundSqlCache = 
							Caffeine.newBuilder()
								.expireAfterWrite(300, TimeUnit.SECONDS)
								.build();
	/**
	 * mapper class
	 */
	@JsonIgnore
	private String mapperClass = "";
	
	/**
	 * entity Class
	 */
	@JsonIgnore
	@SuppressWarnings("rawtypes")
	private Class entityClass;
	
	/**
	 * mapper 
	 */
	@JsonIgnore
	private IJpaMapper<T> mapper = null;
	
	public JpaService() {}
	
	/**
	 * Load mapperClass by class type
	 * @param cls
	 */
	@SuppressWarnings("unchecked")
	public JpaService(@SuppressWarnings("rawtypes") Class cls) {
		logger.trace("class name : {}" , cls.getSimpleName());
		mapperClass = cls.getSimpleName();
		Type[] pType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (pType != null && pType.length >= 1) {
			this.entityClass = (Class<T>) pType[0];
		} else {
			logger.error("invalide initail, need generic type parameter! ");
			throw new RuntimeException("invalide initail, need generic type parameter!");
		}
		logger.trace("class : {}" , entityClass.getSimpleName());
	}

	/**
	 *  Load mapperClass by class name
	 * @param mapperClass
	 */
	public JpaService(String mapperClass) {
		logger.trace("class : {}" , mapperClass);
		this.mapperClass = mapperClass;
	}

	public void setMapper(IJpaMapper<T> mapper) {
		this.mapper = mapper;
	}
	
	//get or set mapper
	/**
	 * Load Mapper from spring container by mapperClass as bean id
	 * @return IBaseMapper
	 */
	@SuppressWarnings( { "unchecked" })
	public IJpaMapper<T> getMapper() {
		try {
			if(mapper == null) {
				String mapperClassBean = StringUtils.firstToLowerCase(mapperClass);
				logger.info("mapperClass Bean is {}" , mapperClassBean);
				mapper = (IJpaMapper<T>) MybatisJpaContext.getBean(mapperClassBean);
			}
		} catch(Exception e) {
			logger.error("getMapper Exception " , e);
		} finally {
			
		}
		return mapper;
	}

	//follow function for Query
	
	/**
	 * query page list entity by entity 
	 * @param entity
	 * @return
	 */
	public JpaPageResults<T> queryPageResults(T entity) {
		entity.setPageResultSelectUUID(entity.generateId());
		entity.setStartRow(calculateStartRow(entity.getPageNumber() ,entity.getPageSize()));
		
		entity.setPageable(true);
		List<T> resultslist = getMapper().queryPageResults(entity);
		entity.setPageable(false);
		Integer totalPage = resultslist.size();
		
		Integer totalCount = 0;
		if(entity.getPageNumber() == 1 && totalPage < entity.getPageSize()) {
			totalCount = totalPage;
		}else {
			totalCount = parseCount(getMapper().queryPageResultsCount(entity));
		}
		
		return new JpaPageResults<T>(entity.getPageNumber(),entity.getPageSize(),totalPage,totalCount,resultslist);
	}
	
	
	//follow function for Query
	/**
	 * query page list entity by entity 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JpaPageResults<T> queryPageResults(String mapperId,T entity) {
		entity.setPageResultSelectUUID(entity.generateId());
		entity.setStartRow(calculateStartRow(entity.getPageNumber() ,entity.getPageSize()));
		
		entity.setPageable(true);
		List<T> resultslist = null;
		try {
			resultslist = (List<T>)InstanceUtil.invokeMethod(getMapper(), mapperId, new Object[]{entity});
			entity.setPageable(false);
			Integer totalPage = resultslist.size();
			
			Integer totalCount = 0;
			if(entity.getPageNumber() == 1 && totalPage < entity.getPageSize()) {
				totalCount = totalPage;
			}else {
				totalCount = parseCount(getMapper().queryPageResultsCount(entity));
			}
			
			return new JpaPageResults<T>(entity.getPageNumber(),entity.getPageSize(),totalPage,totalCount,resultslist);
		} catch (Exception e) {
			logger.error("queryPageResults Exception " , e);
		}
		return null;
	}
	
	/**
	 * query Count by entity 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer queryPageResultsCount(T entity) {
		Integer count = 0;
		try {
			if(entity == null) {
				entity = (T) entityClass.getDeclaredConstructor().newInstance();
			}
			count = getMapper().queryPageResultsCount(entity);
			logger.debug("queryCount count : {}" , count);
		} catch(Exception e) {
			logger.error("queryPageResultsCount Exception " , e);
		}
		return count;
	}
	
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
			logger.error("query Exception " , e);
		}
		return null;
	}
	
	/**
	 *  query list entity by Query 
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Query query) {
		try {
			return getMapper().filterByQuery((T)entityClass.getDeclaredConstructor().newInstance(),query);
		} catch(Exception e) {
			logger.error("query Exception " , e);
		}
		return null;
	}
	
	/**
	 * findAll from table
	 * @return
	 */
	public List<T> findAll() {
		try {
			return getMapper().findAll(this.entityClass);
		} catch(Exception e) {
			logger.error("findAll Exception {}" , e);
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
		return null;
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
			return  (findList == null ||findList.size() == 0) ? null : findList.get(0);
		} catch(Exception e) {
			logger.error("findAll Exception " , e);
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
	 *  query one entity by entity
	 * @param entity
	 * @return
	 */
	public T load(T entity) {
		try {
			List<T> entityList = getMapper().query(entity);
			return ((entityList != null) && ( entityList.size() > 0 ))?entityList.get(0) : null;
		} catch(Exception e) {
			logger.error("load Exception " , e);
		}
		return null;
	}
	
	/**
	 * query one entity by entity id
	 * @param id
	 * @return
	 */
	public T get(String id) {
		try {
			logger.debug("entityClass  {} , primaryKey {}" , entityClass.toGenericString() , id);
			return  getMapper().get(this.entityClass,id,null);
		} catch(Exception e) {
			logger.error("get Exception " , e);
		}
		return null;
	}
	
	/**
	 * query one entity by entity id
	 * @param id
	 * @param partitionKey
	 * @return T
	 */
	public T get(String id,String partitionKey) {
		try {
			logger.debug("entityClass  {} , primaryKey {} , partitionKey {}" , entityClass.toGenericString() , id,partitionKey);
			return  getMapper().get(this.entityClass,id,partitionKey);
		} catch(Exception e) {
			logger.error("get Exception " , e);
		}
		return null;
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
	 * @param listEntity
	 * @return
	 */
	public boolean insertBatch(List<T> listEntity){
		try {
			if(BeanUtil.isNotNull(listEntity)) {
				Integer count = 0;
				for(T entity  : listEntity) {
					if(getMapper().insert(entity)>0) {
						count ++;
					}
				}
				logger.debug("Insert Batch count : {}" , count);
				return count > 0;
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
		T loadedEntity = load(entity);
		if(loadedEntity == null) {
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
			Integer count=getMapper().update(entity);
			logger.debug("update count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("update Exception " , e);
		}
		return false;
	}
	
	/**
	 * delete entity by entity
	 * @param entity
	 * @return
	 */
	public boolean delete(T entity) {
		try {
			Integer count = getMapper().delete(entity);
			logger.debug("delete count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("delete Exception " , e);
		}
		return false;
	}
	
	/**
	 * batch delete entity by id List
	 * @param idList
	 * @return boolean
	 */
	public boolean deleteBatch(List<String> idList) {
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
	public boolean deleteBatch(List<String> idList,String partitionKey) {
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
	 * batch delete entity by ids,split with ,
	 * @param ids
	 * @return boolean
	 */
	public boolean deleteBatch(String ids) {
		List<String> idList = StringUtils.string2List(ids, ",");
		return deleteBatch(idList);
	}
	
	/**
	 * batch delete entity by ids,split with ,
	 * @param ids
	 * @param partitionKey
	 * @return
	 */
	public boolean deleteBatch(String ids,String partitionKey) {
		List<String> idList = StringUtils.string2List(ids, ",");
		return deleteBatch(idList,partitionKey);
	}
	

	/**
	 * batch delete entity by ids , split with ,
	 * @param ids
	 * @param split
	 * @return
	 */
	public boolean deleteBatchSplit(String ids , String split) {
		List<String> idList = StringUtils.string2List(ids, StringUtils.isBlank(split)? "," : split );
		return deleteBatch(idList);
	}
	
	/**
	 * delete one entity by id
	 * @param id
	 * @return
	 */
	public boolean remove(String id){
		try {
			logger.debug("id {} " , id );
			Integer count=getMapper().remove(this.entityClass,id,null);
			logger.debug("remove count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("remove Exception " , e);
		}
		return false;
	}
	
	/**
	 * delete one entity by id
	 * @param id
	 * @param partitionKey
	 * @return
	 */
	public boolean remove(String id,String partitionKey){
		try {
			logger.debug("id {} , partitionKey {}" , id , partitionKey);
			Integer count = getMapper().remove(this.entityClass,id,partitionKey);
			logger.debug("remove count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("remove Exception " , e);
		}
		return false;
	}
	
	/**
	 * logicDelete entity by ids
	 * @param idList
	 * @return
	 */
	public boolean logicDelete(List<String> idList) {
		try {
			logger.trace("logicDelete idList {}" , idList);
			Integer count = getMapper().logicDelete(this.entityClass,idList,null);
			logger.debug("logicDelete count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("logicDelete Exception " , e);
		}
		return true;
	}
	
	/**
	 * logicDelete entity by ids
	 * @param idList
	 * @param partitionKey
	 * @return
	 */
	public boolean logicDelete(List<String> idList,String partitionKey) {
		try {
			logger.trace("logicDelete idList {} , partitionKey {}" , idList , partitionKey);
			Integer count = getMapper().logicDelete(this.entityClass,idList,partitionKey);
			logger.debug("logicDelete count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			logger.error("logicDelete Exception " , e);
		}
		return true;
	}
	
	/**
	 * logicDelete entity by ids
	 * @param ids string
	 * @return
	 */
	public boolean logicDelete(String ids) {
		List<String> idList = StringUtils.string2List(ids, ",");
		return logicDelete(idList);
	}
	
	/**
	 * logicDelete entity by ids
	 * @param ids string
	 * @param split
	 * @return
	 */
	public boolean logicDeleteSplit(String ids , String split) {
		List<String> idList = StringUtils.string2List(ids, StringUtils.isBlank(split)? "," : split );
		return logicDelete(idList);
	}
	
	
	//follow is  for query grid paging
	/**
	 * parse Object Count to Integer
	 * @param totalCount
	 * @return
	 */
	public Integer parseCount(Object totalCount){
		Integer retTotalCount=0;
		if(totalCount == null) {
			return retTotalCount;
		}else{
			retTotalCount = Integer.parseInt(totalCount.toString());
		}
		return retTotalCount;
	}
	
	/**
	 * calculate total Count
	 * @param entity
	 * @param totalCount
	 * @return
	 */
	public Integer calculateTotalPage(JpaEntity entity,Integer totalCount){
		return (totalCount + entity.getPageSize() - 1) / entity.getPageSize();
	}
	
	/**
	 * calculate StartRow
	 * @param page
	 * @param pageResults
	 * @return
	 */
	public Integer calculateStartRow(Integer page,Integer pageSize){
		return (page - 1) * pageSize;
	}
}
