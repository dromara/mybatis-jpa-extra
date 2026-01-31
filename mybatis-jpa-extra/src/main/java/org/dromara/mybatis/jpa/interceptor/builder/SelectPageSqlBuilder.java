/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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
 


package org.dromara.mybatis.jpa.interceptor.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.dromara.mybatis.jpa.constants.ConstMetadata;
import org.dromara.mybatis.jpa.dialect.Dialect;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageSqlCache;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectPageSqlBuilder {
    private static final Logger logger     =     LoggerFactory.getLogger(SelectPageSqlBuilder.class);
    
    public static SelectPageSql parse(BoundSql boundSql,Object parameterObject){
        SelectPageSql pageSql   = new SelectPageSql();
        //判断是否select语句及需要分页支持
        if (boundSql.getSql().toLowerCase().trim().startsWith("select")) {
            pageSql.setSelectTrack(true);
            pageSql.setPage(getJpaPageParameter(parameterObject));
            //分页标识
            if(pageSql.getPage() != null && pageSql.getPage().isPageable()){
                pageSql.setPageable(true);
            }
        }
        return pageSql;
    }
    
    public static String translate(StatementHandler statement,Dialect dialect,BoundSql boundSql,SelectPageSql pageSql) {
        String  selectSql = boundSql.getSql();
        String boundSqlRemoveBreakingWhitespace = removeBreakingWhitespace(selectSql);
        logger.trace("prepare  boundSql  ==> {}" , boundSqlRemoveBreakingWhitespace);
        if(statement instanceof SimpleStatementHandler){
            selectSql = dialect.getLimitString(selectSql, pageSql.getPage());
        }else if(statement instanceof PreparedStatementHandler){
        		MapperMetadata.PAGE_BOUNDSQL_CACHE.put(
                    pageSql.getPage().getPageSelectId(), 
                    new JpaPageSqlCache(selectSql,boundSql)
                    );
            selectSql = dialect.getLimitString(selectSql, pageSql.getPage());
        }
        logger.trace("prepare dialect boundSql : {}" , boundSqlRemoveBreakingWhitespace);
        return selectSql;
    }
    
    protected static JpaPage getJpaPageParameter(Object parameterObject) {
        JpaPage page = null;
        if((parameterObject instanceof JpaPage jpaPage)) {
            page = jpaPage;
        }else if(parameterObject instanceof ParamMap<?> paramMap) {
            if(paramMap.containsKey(ConstMetadata.PAGE)) {
                page = (JpaPage)paramMap.get(ConstMetadata.PAGE);
            }else {
                try {
                    for (Map.Entry<String,?> entry : paramMap.entrySet()){
                        if(entry.getValue() instanceof JpaPage jpaPage) {
                            page = jpaPage;
                            break;
                        }
                    }
                }catch(Exception e) {
                    logger.error("Exception",e);
                }
            }
        }else if(parameterObject instanceof HashMap<?,?> parameterMap){
            if(parameterMap.containsKey(ConstMetadata.SQL_MAPPER_PARAMETER_PAGE)) {
                page = (JpaPage)parameterMap.get(ConstMetadata.SQL_MAPPER_PARAMETER_PAGE);
                logger.trace("page : {} " , page);
            }
        }else {
            //is object not have JpaPage , do nothing
        }
        return page;
    }
    
    protected static String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
          builder.append(whitespaceStripper.nextToken());
          builder.append(" ");
        }
        return builder.toString();
    }
}
