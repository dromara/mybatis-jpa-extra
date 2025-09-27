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
 
package org.dromara.mybatis.jpa.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromara.mybatis.jpa.IJpaSqlMapper;
import org.dromara.mybatis.jpa.metadata.MetadataConstants;
import org.dromara.mybatis.jpa.repository.IJpaSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JpaSqlRepositoryImpl implements IJpaSqlRepository {
	private static final  Logger logger = LoggerFactory.getLogger(JpaSqlRepositoryImpl.class);

	@Override
	public IJpaSqlMapper getMapper() {
		return null;
	}
	
	@Override
	public List<Map<String, Object>> selectList(String sql) {
		return getMapper().selectList(mapperValue(sql,null));
	}

	@Override
	public List<Map<String, Object>> selectList(String sql, Map<String, Object> parameters) {
		return getMapper().selectList(mapperValue(sql,parameters));
	}

	@Override
	public int insert(String sql) {
		return getMapper().insert(mapperValue(sql,null));
	}

	@Override
	public int insert(String sql, Map<String, Object> entity) {
		return getMapper().insert(mapperValue(sql,entity));
	}

	@Override
	public int update(String sql) {
		return getMapper().update(mapperValue(sql,null));
	}

	@Override
	public int update(String sql, Map<String, Object> entity) {
		return getMapper().update(mapperValue(sql,entity));
	}

	@Override
	public int delete(String sql) {
		return getMapper().delete(mapperValue(sql,null));
	}

	@Override
	public int delete(String sql, Map<String, Object> parameters) {
		return getMapper().delete(mapperValue(sql,parameters));
	}
	
	private Map<String, Object> mapperValue(String sql, Map<String, Object> parameters){
		if(parameters == null) {
			parameters = new HashMap<>();
		}
		logger.trace("sql {}",sql);
		logger.trace("parameters {}",parameters);
		parameters.put(MetadataConstants.IJPA_SQL_PARAMETER_SQL, sql);
		return parameters;
	}

}
