/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import java.util.Map;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderDelete;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderInsert;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderQuery;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapperSqlProvider <T extends JpaBaseDomain>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(MapperSqlProvider.class);
	
	public MapperSqlProvider() {
		_logger.debug("constructor init .");
	}

	public String get(Map<String, Object>  parametersMap) {
		return new SqlProviderQuery().get(parametersMap);  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		return new SqlProviderQuery().findAll(parametersMap);  
    }
	
	public String remove(Map<String, Object>  parametersMap) { 
        return new SqlProviderDelete().execute(parametersMap);  
    }  
	
	public String batchDelete(Map<String, Object>  parametersMap) { 
		return new SqlProviderDelete().batchDelete(parametersMap);
    } 
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		return new SqlProviderInsert().execute(entity);
	}

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		return new SqlProviderUpdate().execute(entity);
	}

	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String queryPageResultsCount(T entity) {
		return new SqlProviderQuery().executePageResultsCount(entity);
	}
	
	public String query(T entity) {
		return new SqlProviderQuery().execute(entity);
	}

}
