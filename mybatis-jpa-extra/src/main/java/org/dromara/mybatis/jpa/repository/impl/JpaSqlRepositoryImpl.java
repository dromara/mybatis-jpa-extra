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
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.repository.IJpaSqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JpaSqlRepositoryImpl
 */
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
		parameters.put(ConstMetadata.SQL_MAPPER_PARAMETER_SQL, sql);
		return parameters;
	}
	
	//follow is  for query paging
	
	/**
	 * 分页查询，JpaPage 和 entity
	 * @param page
	 * @param entity
	 * @return
	 */
	@Override
	public JpaPageResults<Map<String,Object>> fetch(String sql , JpaPage page , Map<String, Object> parameters) {
		try {
			page.build();
			parameters.put(ConstMetadata.SQL_MAPPER_PARAMETER_PAGE, page);
			List<Map<String,Object>> resultslist = this.selectList(sql, parameters);
			return buildPageResults(page , resultslist);
		}catch (Exception e) {
			logger.error("fetch Exception " , e);
		}
		return null;
	}
	
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
	

	protected JpaPageResults<Map<String,Object>> buildPageResults(JpaPage page , List<Map<String,Object>> resultslist) {
		//当前页记录数
		Integer records = JpaPageResults.parseRecords(resultslist);
		//总页数
		Integer totalCount = fetchCount(page, resultslist);
		return new JpaPageResults<>(page.getPageNumber(),page.getPageSize(),records,totalCount,resultslist);
	}
	
	/**
	 * 获取总页数
	 * @param page
	 * @param records
	 * @return
	 */
	protected Integer fetchCount(JpaPage page ,List<Map<String,Object>> resultslist) {
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
