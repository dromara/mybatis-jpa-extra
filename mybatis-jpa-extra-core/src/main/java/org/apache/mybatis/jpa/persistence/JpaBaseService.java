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
 

package org.apache.mybatis.jpa.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.mybatis.jpa.PageResultsSqlCache;
import org.apache.mybatis.jpa.query.Query;
import org.apache.mybatis.jpa.util.BeanUtil;
import org.apache.mybatis.jpa.util.InstanceUtil;
import org.apache.mybatis.jpa.util.StringUtils;
import org.apache.mybatis.jpa.util.JpaWebContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


/**
 * @author Crystal.Sea
 *
 * @param <T>
 */
public  class  JpaBaseService <T extends JpaBaseEntity> {
	
	final static Logger _logger = LoggerFactory.getLogger(JpaBaseService.class);
	
	@JsonIgnore
	//定义全局缓存
	public static final Cache<String, PageResultsSqlCache> pageResultsBoundSqlCache = 
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
	private IJpaBaseMapper<T> mapper = null;
	
	public JpaBaseService() {}
	
	/**
	 * Load mapperClass by class type
	 * @param cls
	 */
	@SuppressWarnings("unchecked")
	public JpaBaseService(@SuppressWarnings("rawtypes") Class cls) {
		_logger.trace("class : {}" , cls.getSimpleName());
		mapperClass = cls.getSimpleName();
		Type[] pType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (pType != null && pType.length >= 1) {
			this.entityClass = (Class<T>) pType[0];
		} else {
			_logger.error("invalide initail, need generic type parameter! ");
			throw new RuntimeException("invalide initail, need generic type parameter!");
		}
		_logger.trace("class : {}" , entityClass.getSimpleName());
	}

	/**
	 *  Load mapperClass by class name
	 * @param mapperClass
	 */
	public JpaBaseService(String mapperClass) {
		_logger.trace("class : {}" , mapperClass);
		this.mapperClass = mapperClass;
	}

	public void setMapper(IJpaBaseMapper<T> mapper) {
		this.mapper = mapper;
	}
	
	//get or set mapper
	/**
	 * Load Mapper from spring container by mapperClass as bean id
	 * @return IBaseMapper
	 */
	@SuppressWarnings( { "unchecked" })
	public IJpaBaseMapper<T> getMapper() {
		try {
			if(mapper == null) {
				String mapperClassBean = mapperClass.toLowerCase().charAt(0) + mapperClass.substring(1);
				_logger.info("mapperClass Bean is {}" , mapperClassBean);
				mapper = (IJpaBaseMapper<T>) JpaWebContext.getBean(mapperClassBean);
			}
		} catch(Exception e) {
			_logger.error("getMapper Exception " , e);
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
			totalCount=totalPage;
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
		} catch (Exception e) {
			_logger.error("queryPageResults Exception " , e);
		}
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
				entity = (T) entityClass.newInstance();
			}
			count = getMapper().queryPageResultsCount(entity);
			_logger.debug("queryCount count : {}" , count);
		} catch(Exception e) {
			_logger.error("queryPageResultsCount Exception " , e);
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
				entity = (T) entityClass.newInstance();
			}
			return getMapper().query(entity);
		} catch(Exception e) {
			_logger.error("query Exception " , e);
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
			return getMapper().filterByQuery((T)entityClass.newInstance(),query);
		} catch(Exception e) {
			_logger.error("query Exception " , e);
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
			_logger.error("findAll Exception " , e);
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
			_logger.error("findAll Exception " , e);
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
			_logger.error("findAll Exception " , e);
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
			_logger.error("load Exception " , e);
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
			_logger.debug("entityClass  {} , primaryKey {}" , entityClass.toGenericString() , id);
			return  getMapper().get(this.entityClass,id);
		} catch(Exception e) {
			_logger.error("get Exception " , e);
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
			_logger.debug("insert count : {}" , count);
			return  count > 0;
		} catch(Exception e) {
			_logger.error("insert Exception " , e);
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
				_logger.debug("Insert Batch count : {}" , count);
				return count > 0;
			}
		} catch(Exception e) {
			_logger.error("Insert Batch Exception " , e);
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
			_logger.debug("update count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			_logger.error("update Exception " , e);
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
			Integer count=getMapper().delete(entity);
			_logger.debug("delete count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			_logger.error("delete Exception " , e);
		}
		return false;
	}
	
	/**
	 * batch delete entity by id List
	 * @param ids
	 * @return
	 */
	public boolean deleteBatch(List<String> idList) {
		try {
			_logger.trace("deleteBatch {}" , idList);
			Integer count = getMapper().deleteBatch(this.entityClass,idList);
			_logger.debug("deleteBatch count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			_logger.error("deleteBatch Exception " , e);
		}
		return false;
	}
	
	/**
	 * batch delete entity by ids,split with ,
	 * @param ids
	 * @return
	 */
	public boolean deleteBatch(String ids) {
		List<String> idList = StringUtils.string2List(ids, ",");
		return deleteBatch(idList);
	}

	public boolean deleteBatch(String ids , String split) {
		List<String> idList = StringUtils.string2List(ids, split);
		return deleteBatch(idList);
	}
	
	
	public boolean remove(String id){
		try {
			Integer count=getMapper().remove(this.entityClass,id);
			_logger.debug("remove count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			_logger.error("remove Exception " , e);
		}
		return false;
	}
	
	public boolean logicDelete(List<String> idList) {
		try {
			_logger.trace("logicDelete {}" , idList);
			Integer count = getMapper().logicDelete(this.entityClass,idList);
			_logger.debug("logicDelete count : {}" , count);
			return count > 0;
		} catch(Exception e) {
			_logger.error("logicDelete Exception " , e);
		}
		return true;
	}
	
	public boolean logicDelete(String ids) {
		List<String> idList = StringUtils.string2List(ids, ",");
		return logicDelete(idList);
	}
	
	public boolean logicDelete(String ids , String split) {
		List<String> idList = StringUtils.string2List(ids, split);
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
	public Integer calculateTotalPage(JpaBaseEntity entity,Integer totalCount){
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
