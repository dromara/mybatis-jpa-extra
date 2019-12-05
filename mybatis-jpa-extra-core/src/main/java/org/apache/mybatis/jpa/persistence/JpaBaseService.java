package org.apache.mybatis.jpa.persistence;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.mybatis.jpa.PageResultsSqlCache;
import org.apache.mybatis.jpa.util.BeanUtil;
import org.apache.mybatis.jpa.util.InstanceUtil;
import org.apache.mybatis.jpa.util.WebContext;
import org.ehcache.UserManagedCache;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.builders.UserManagedCacheBuilder;


/**
 * @author Crystal.Sea
 *
 * @param <T>
 */
public  class  JpaBaseService <T extends JpaBaseDomain> {
	
	final static Logger log = Logger.getLogger(JpaBaseService.class);
	
	//定义全局缓存
	public static UserManagedCache<String, PageResultsSqlCache> pageResultsBoundSqlCache = UserManagedCacheBuilder
											.newUserManagedCacheBuilder(String.class, PageResultsSqlCache.class)
											.withResourcePools(ResourcePoolsBuilder.heap(1000))
											//.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofSeconds(300)))
											.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(300)))
											.build(true);
	/**
	 * mapper class
	 */
	private String mapperClass = "";
	
	/**
	 * entity Class
	 */
	@SuppressWarnings("rawtypes")
	private Class entityClass;
	
	/**
	 * mapper 
	 */
	private IJpaBaseMapper<T> mapper = null;
	
	//TODO 
	public JpaBaseService() {}
	
	/**
	 * Load mapperClass by class type
	 * @param cls
	 */
	@SuppressWarnings("unchecked")
	public JpaBaseService(@SuppressWarnings("rawtypes") Class cls) {
		log.trace("class : " + cls.getSimpleName());
		mapperClass = cls.getSimpleName();
		Type[] pType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (pType != null && pType.length >= 1) {
			this.entityClass = (Class<T>) pType[0];
		} else {
			throw new RuntimeException("invalide initail, need generic type parameter!");
		}
		log.trace("class : " + entityClass.getSimpleName());
	}

	/**
	 *  Load mapperClass by class name
	 * @param mapperClass
	 */
	public JpaBaseService(String mapperClass) {
		log.trace("class : " + mapperClass);
		this.mapperClass = mapperClass;
	}

	//TODO get or set mapper
	/**
	 * Load Mapper from spring container by mapperClass as bean id
	 * @return IBaseMapper
	 */
	@SuppressWarnings( { "unchecked" })
	public IJpaBaseMapper<T> getMapper() {
		try {
			if(mapper == null) {
				String mapperClassBean=mapperClass.toLowerCase().charAt(0)+mapperClass.substring(1);
				log.info("mapperClass Bean is " +mapperClassBean);
				mapper = (IJpaBaseMapper<T>) WebContext.getBean(mapperClassBean);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return mapper;
	}

	public void setMapper(IJpaBaseMapper<T> mapper) {
		this.mapper = mapper;
	}

	//TODO follow function for Query
	
	/**
	 * query page list entity by entity 
	 * @param entity
	 * @return
	 */
	public JpaPageResults<T> queryPageResults(T entity) {
		entity.setPageResultSelectUUID(entity.generateId());
		entity.setStartRow(calculateStartRow(entity.getPageNumber() ,entity.getPageSize()));
		
		entity.setPageable(true);
		List<T> resultslist=getMapper().queryPageResults(entity);
		entity.setPageable(false);
		Integer totalPage=resultslist.size();
		
		Integer totalCount = 0;
		if(entity.getPageNumber()==1&&totalPage<entity.getPageSize()) {
			totalCount=totalPage;
		}else {
			totalCount=parseCount(getMapper().queryPageResultsCount(entity));
		}
		
		return new JpaPageResults<T>(entity.getPageNumber(),entity.getPageSize(),totalPage,totalCount,resultslist);
	}
	
	
	//TODO follow function for Query
	
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
		List<T> resultslist=null;
		try {
			resultslist = (List<T>)InstanceUtil.invokeMethod(getMapper(), mapperId, new Object[]{entity});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity.setPageable(false);
		Integer totalPage=resultslist.size();
		
		Integer totalCount = 0;
		if(entity.getPageNumber()==1&&totalPage<entity.getPageSize()) {
			totalCount=totalPage;
		}else {
			totalCount=parseCount(getMapper().queryPageResultsCount(entity));
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
		try {
			if(entity == null) {
				entity = (T) entityClass.newInstance();
			}
			Integer count=getMapper().queryPageResultsCount(entity);
			log.debug("queryCount count : "+count);
			return count;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
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
				return getMapper().query(entity);
			}
			return getMapper().query(entity);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<T> findAll() {
		try {

			return getMapper().findAll(this.entityClass);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *  query one entity by entity
	 * @param entity
	 * @return
	 */
	public T load(T entity) {
		try {
			List<T> entityList=getMapper().query(entity);
			return entityList!=null&&entityList.size()>0?entityList.get(0):null;
		} catch(Exception e) {
			e.printStackTrace();
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
			log.debug("entityClass  "+entityClass.toGenericString()+" , id "+id);
			return  getMapper().get(this.entityClass,id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//TODO follow function for insert update and delete
	/**
	 * insert new entity
	 * @param entity
	 * @return
	 */
	public boolean insert(T entity) {
		try {
			Integer count=getMapper().insert(entity);
			log.debug("insert count : "+count);
			
			return  count > 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * update entity
	 * @param entity
	 * @return
	 */
	public boolean update(T entity) {
		try {
			
			Integer count=getMapper().update(entity);
			log.debug("update count : "+count);
			
			return count > 0;
		} catch(Exception e) {
			e.printStackTrace();
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
			log.debug("delete count : "+count);
			
			return count > 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public boolean remove(String id){
		try {
			
			Integer count=getMapper().remove(this.entityClass,id);
			log.debug("remove count : "+count);
			
			return count > 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//TODO follow function for complex insert and delete
	/**
	 * batch insert entity
	 * @param listEntity
	 * @return
	 */
	public boolean batchInsert(List<T> listEntity){
		try {
			if(BeanUtil.isNotNull(listEntity)) {
				Integer count=0;
				for(T entity  : listEntity) {
					if(getMapper().insert(entity)>0) {
						count++;
					}
				}
				log.debug("batchInsert count : "+count);
				return count > 0;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * batch delete entity by ids
	 * @param ids
	 * @return
	 */
	public boolean batchDelete(List<String> idList) {
		try {
			Integer count=getMapper().batchDelete(this.entityClass,idList);
			log.debug("batchDelete count : "+count);
			
			return count > 0;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	//TODO follow is  for query grid paging
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
			retTotalCount=Integer.parseInt(totalCount.toString());
		}
		return retTotalCount;
	}
	
	/**
	 * calculate total Count
	 * @param entity
	 * @param totalCount
	 * @return
	 */
	public Integer calculateTotalPage(JpaBaseDomain entity,Integer totalCount){
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
