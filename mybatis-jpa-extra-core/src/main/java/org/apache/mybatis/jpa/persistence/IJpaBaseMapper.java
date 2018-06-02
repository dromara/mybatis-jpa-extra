package org.apache.mybatis.jpa.persistence;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * @author Crystal.sea
 * @param <T>
 */
public interface IJpaBaseMapper<T> {
	
	public List<T> select(T entity);
	
	public List<T> query(T entity);
	
	//TODO follow function for Query
	public List<T> queryPageResults(T entity);

	@SelectProvider(type = MapperSqlProvider.class, method = "queryPageResultsCount")
	public Integer queryPageResultsCount(T entity);
	
	@SelectProvider(type = MapperSqlProvider.class, method = "findAll")
	public List<T> findAll(@Param ("entityClass")Class<?> entityClass);

	/**
	 *  query by id
	 * @param id
	 * @return one 
	 */
	@SelectProvider(type = MapperSqlProvider.class, method = "get")
	public T get(@Param ("entityClass")Class<?> entityClass,@Param ("id") String id);
	
	/**
	 * query by entity
	 * @param entity
	 * @return one
	 */
	public T load(T entity);
	
	//TODO follow function for insert update and delete
	@InsertProvider(type = MapperSqlProvider.class, method = "insert")
	public Integer insert(T entity);
	
	@UpdateProvider(type = MapperSqlProvider.class, method = "update")
	public Integer update(T entity);


	/**
	 * delete by entity parameter
	 * @param entity
	 * @return
	 */
	public Integer delete(T entity);
	/**
	 * delete by id
	 * @param id
	 * @return
	 */
	@DeleteProvider(type = MapperSqlProvider.class, method = "remove")
	public Integer remove(@Param ("entityClass")Class<?> entityClass,@Param ("id") String id);
	
	@DeleteProvider(type = MapperSqlProvider.class, method = "batchDelete")
	public Integer batchDelete(@Param ("entityClass")Class<?> entityClass,@Param ("idList") List<String> idList);	
	
}
