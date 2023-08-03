/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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

import java.util.concurrent.TimeUnit;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResultsSqlCache;
import org.dromara.mybatis.jpa.metadata.SqlSyntaxConstants;
import org.dromara.mybatis.jpa.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author Crystal.Sea
 *
 */
public class FetchCountProvider <T extends JpaEntity>{
	
	private static final Logger logger 	= 	LoggerFactory.getLogger(FetchCountProvider.class);
	
	//定义全局缓存
	public static final Cache<String, JpaPageResultsSqlCache> pageResultsBoundSqlCache = 
							Caffeine.newBuilder()
								.expireAfterWrite(300, TimeUnit.SECONDS)
								.build();
	/**
	 * @param entity
	 * @return executePageResultsCount sql String
	 */
	public String executeCount(JpaPage page) {
		//获取缓存数据
		JpaPageResultsSqlCache pageResultsSqlCache = getPageResultsCache(page.getPageResultSelectUUID());
		//多个空格 tab 替换成1个空格
		String selectSql = StringUtils.lineBreak2Blank(pageResultsSqlCache.getSql());
		
		BoundSql boundSql = (BoundSql)pageResultsSqlCache.getBoundSql();
		logger.trace("Count original SQL  :\n{}" , selectSql);
		
		StringBuffer sql = new StringBuffer(SqlSyntaxConstants.SELECT +" "+ SqlSyntaxConstants.Functions.COUNT_ONE +" countrows_ ");
		StringBuffer countSql = new StringBuffer();

		if(boundSql.getParameterMappings() == null ||boundSql.getParameterMappings().isEmpty()) {
			countSql.append(selectSql);
		}else {
			for (ParameterMapping parameterMapping:boundSql.getParameterMappings()) {
				countSql.append(selectSql.substring(0, selectSql.indexOf("?")));
				countSql.append("#{"+parameterMapping.getProperty()+"}");
				selectSql = selectSql.substring(selectSql.indexOf("?")+1);
			}
			countSql.append(selectSql);
		}
		String countSqlLowerCase = countSql.toString().toLowerCase();
		logger.trace("Count SQL LowerCase  :\n{}" , countSqlLowerCase);
		
		if(countSqlLowerCase.indexOf(SqlSyntaxConstants.DISTINCT + " ")>0 //去重
				||countSqlLowerCase.indexOf(" " + SqlSyntaxConstants.GROUPBY + " ")>0 //分组
				||countSqlLowerCase.indexOf(" " + SqlSyntaxConstants.HAVING + " ")>0 //聚合函数
				||(countSqlLowerCase.indexOf(" " + SqlSyntaxConstants.FROM + " ") 
						!= countSqlLowerCase.lastIndexOf(" " + SqlSyntaxConstants.FROM + " ")
				) //嵌套
				) {
			logger.trace("Count SQL Complex ");
			sql.append(SqlSyntaxConstants.FROM).append(" (").append(countSql).append(" ) count_table_");
		}else {
			int fromIndex = countSqlLowerCase.indexOf(" " + SqlSyntaxConstants.FROM + " ");
			int orderByIndex = countSqlLowerCase.indexOf(" " + SqlSyntaxConstants.ORDERBY + " ");
			logger.trace("Count SQL from Index {} , order by {}" ,fromIndex,orderByIndex);
			if(orderByIndex > -1) {
				sql.append(countSql.substring(fromIndex,orderByIndex));
			}else {
				sql.append(countSql.substring(fromIndex));
			}
		}
		logger.trace("Count SQL : \n{}" , sql);
		return sql.toString();
	}
	
	private JpaPageResultsSqlCache getPageResultsCache(String selectUUID) {
		JpaPageResultsSqlCache cache = pageResultsBoundSqlCache.getIfPresent(selectUUID);
		//删除缓存
		pageResultsBoundSqlCache.invalidate(selectUUID);
		return cache;
	}
	
}
