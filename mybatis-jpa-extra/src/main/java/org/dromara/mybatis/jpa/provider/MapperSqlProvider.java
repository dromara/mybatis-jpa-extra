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
package org.dromara.mybatis.jpa.provider;

import java.util.Map;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MapperSqlProvider <T extends JpaEntity>{
	
	private static final Logger logger 	= 	LoggerFactory.getLogger(MapperSqlProvider.class);
	
	public MapperSqlProvider() {
		logger.debug("constructor init .");
	}

	public String get(Map<String, Object>  parametersMap) {
		return new GetProvider().get(parametersMap);  
    }
	
	public String find(Map<String, Object>  parametersMap) throws Exception {
		return new FindProvider().find(parametersMap);  
    }
	
	public String findAll(Map<String, Object>  parametersMap) {  
		return new FindProvider().findAll(parametersMap);  
    }
	
	public String remove(Map<String, Object>  parametersMap) { 
        return new DeleteProvider().remove(parametersMap);  
    }  
	
	public String deleteBatch(Map<String, Object>  parametersMap) { 
		return new DeleteProvider().batchDelete(parametersMap);
    } 
	
	public String logicDelete(Map<String, Object>  parametersMap) { 
		return new LogicDeleteProvider().logicDelete(parametersMap);
    }
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(T entity) {
		return new InsertProvider().insert(entity);
	}

	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(T entity) {
		return new UpdateProvider().update(entity);
	}

	
	public String fetch(Map<String, Object>  parametersMap) {
		return new FetchProvider().fetch(parametersMap);
	}
	
	public String fetchByCondition(Map<String, Object>  parametersMap) {
		return new FetchProvider().fetchByCondition(parametersMap);
	}
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String fetchCount(T entity) {
		return new FetchCountProvider().executeCount(entity);
	}
	
	public String query(T entity) {
		return new QueryProvider().query(entity);
	}
	
	public String queryByCondition(T entity,Query query) {
		return new QueryProvider().queryByCondition(entity,query);
	}

}
