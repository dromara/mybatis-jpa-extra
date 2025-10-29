/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
 
package org.dromara.mybatis.jpa.provider;

import java.util.Map;

import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.provider.base.FetchCountProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
@SuppressWarnings({"rawtypes" })
public class SqlMapperProvider {	
	static final Logger logger 	= 	LoggerFactory.getLogger(SqlMapperProvider.class);
	
	public SqlMapperProvider() {
		logger.debug("constructor init .");
	}

	public String selectList(Map<String, Object>  parametersMap) {
		return parametersMap.get(ConstMetadata.SQL_MAPPER_PARAMETER_SQL).toString();
    }
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String insert(Map<String ,Object> entityMap) {
		return entityMap.get(ConstMetadata.SQL_MAPPER_PARAMETER_SQL).toString();
	}

	//update
	/**
	 * @param entity
	 * @return update sql String
	 */
	public String update(Map<String ,Object> entityMap) {
		return entityMap.get(ConstMetadata.SQL_MAPPER_PARAMETER_SQL).toString();
	}

	
	public String delete(Map<String ,Object> parametersMap) {
		return parametersMap.get(ConstMetadata.SQL_MAPPER_PARAMETER_SQL).toString();
	}
	
	/**
	 * @param entity
	 * @return insert sql String
	 */
	public String fetchCount(JpaPage entity) {
		return new FetchCountProvider().executeCount(entity);
	}
	
}
