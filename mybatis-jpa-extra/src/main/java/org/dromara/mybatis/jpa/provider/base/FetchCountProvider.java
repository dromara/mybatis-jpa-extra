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
package org.dromara.mybatis.jpa.provider.base;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.dromara.mybatis.jpa.constants.ConstSqlSyntax;
import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageSqlCache;
import org.dromara.mybatis.jpa.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author Crystal.Sea
 *
 */
public class FetchCountProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(FetchCountProvider.class);
    
    /**
     * 定义全局缓存
     */
    public static final Cache<String, JpaPageSqlCache> PAGE_BOUNDSQL_CACHE = 
                            Caffeine.newBuilder()
                                .expireAfterWrite(300, TimeUnit.SECONDS)
                                .build();
    /**
     * @param entity
     * @return executePageResultsCount sql String
     */
    public String executeCount(JpaPage page) {
        StringBuilder sql = new StringBuilder("");
        //获取缓存数据
        JpaPageSqlCache pageSqlCache = getPageSqlCache(page.getPageSelectId());
        if(pageSqlCache != null) {
            //多个空格 tab 替换成1个空格
            String selectSql = StrUtils.lineBreakToBlank(pageSqlCache.getSql());
            
            BoundSql boundSql = pageSqlCache.getBoundSql();
            logger.trace("Count original SQL  :\n{}" , selectSql);
            
            sql.append(ConstSqlSyntax.SELECT).append(" ").append(ConstSqlSyntax.Functions.COUNT_ONE).append(" countrows_ ");
            StringBuilder countSql = new StringBuilder();
    
            if(boundSql.getParameterMappings() == null ||boundSql.getParameterMappings().isEmpty()) {
                countSql.append(selectSql);
            }else {
                for (ParameterMapping parameterMapping:boundSql.getParameterMappings()) {
                    countSql.append(selectSql.substring(0, selectSql.indexOf("?")));
                    countSql.append("#{").append(parameterMapping.getProperty()).append("}");
                    selectSql = selectSql.substring(selectSql.indexOf("?")+1);
                }
                countSql.append(selectSql);
            }
            String countSqlLowerCase = countSql.toString().toLowerCase().replace("\n", " ");
            logger.trace("Count SQL LowerCase  :\n{}" , countSqlLowerCase);
            
            /*
             * 判断 1,去重 2,分组 3,聚合函数
             */
            if(countSqlLowerCase.indexOf(ConstSqlSyntax.DISTINCT + " ")> -1 
                    ||countSqlLowerCase.indexOf(" " + ConstSqlSyntax.GROUP_BY + " ")> -1 
                    ||countSqlLowerCase.indexOf(" " + ConstSqlSyntax.HAVING + " ")> -1 
                    ||(countSqlLowerCase.indexOf(" " + ConstSqlSyntax.FROM + " ") 
                            != countSqlLowerCase.lastIndexOf(" " + ConstSqlSyntax.FROM + " ")
                    ) //嵌套
                    ) {
                logger.trace("Count SQL Complex ");
                sql.append(ConstSqlSyntax.FROM).append(" (").append(countSql).append(" ) count_table_");
            }else {
                int fromIndex = countSqlLowerCase.indexOf(" " + ConstSqlSyntax.FROM + " ");
                int orderByIndex = countSqlLowerCase.indexOf(" " + ConstSqlSyntax.ORDER_BY + " ");
                logger.trace("Count SQL from Index {} , order by {}" ,fromIndex,orderByIndex);
                if(orderByIndex > -1) {
                    sql.append(countSql.substring(fromIndex,orderByIndex));
                }else {
                    sql.append(countSql.substring(fromIndex));
                }
            }
            logger.trace("Count SQL : \n{}" , sql);
        }
        return sql.toString();
    }
    
    /**
     * 查询并删除缓存
     * @param selectId
     * @return
     */
    private JpaPageSqlCache getPageSqlCache(String selectId) {
        JpaPageSqlCache cache = PAGE_BOUNDSQL_CACHE.getIfPresent(selectId);
        PAGE_BOUNDSQL_CACHE.invalidate(selectId);
        return cache;
    }
    
}
