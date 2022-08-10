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
 

/**
 * 
 */
package org.apache.mybatis.jpa.persistence;

import java.util.Map;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderDelete;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderFind;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderGet;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderInsert;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderQuery;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderPageResultsCount;
import org.apache.mybatis.jpa.persistence.provider.SqlProviderUpdate;
import org.apache.mybatis.jpa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapperSqlProvider <T extends JpaBaseEntity>{
	
	private static final Logger _logger 	= 	LoggerFactory.getLogger(MapperSqlProvider.class);
	
	public MapperSqlProvider() {
		_logger.debug("constructor init .");
	}

	public String get(Map<String, Object>  parametersMap) {
		return new SqlProviderGet().get(parametersMap);  
    }
	
	public String find(Map<String, Object>  parametersMap) throws Exception {
		return new SqlProviderFind().find(parametersMap);  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		return new SqlProviderFind().findAll(parametersMap);  
    }
	
	public String remove(Map<String, Object>  parametersMap) { 
        return new SqlProviderDelete().remove(parametersMap);  
    }  
	
	public String deleteBatch(Map<String, Object>  parametersMap) { 
		return new SqlProviderDelete().batchDelete(parametersMap);
    } 
	
	public String logicDelete(Map<String, Object>  parametersMap) { 
		return new SqlProviderDelete().logicDelete(parametersMap);
    }
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		return new SqlProviderInsert().insert(entity);
	}

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		return new SqlProviderUpdate().update(entity);
	}

	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String queryPageResultsCount(T entity) {
		return new SqlProviderPageResultsCount().executePageResultsCount(entity);
	}
	
	public String query(T entity,Query query) {
		return new SqlProviderQuery().query(entity,query);
	}

}
