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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.mapping.BoundSql;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.TableMetadata;
import org.dromara.mybatis.jpa.metadata.findby.FindByKeywords;
import org.dromara.mybatis.jpa.metadata.findby.FindByMapper;
import org.dromara.mybatis.jpa.metadata.findby.FindByMetadata;
import org.dromara.mybatis.jpa.metadata.findby.FindByKeywords.KEY;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.query.builder.ConditionBuilder;
import org.dromara.mybatis.jpa.query.builder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindBySqlBuilder {
    private static final Logger logger     =     LoggerFactory.getLogger(FindBySqlBuilder.class);

    public static boolean isFindBy(String mappedStatementId,BoundSql boundSql) {
        return StringUtils.isBlank(boundSql.getSql()) && !FindByMetadata.containsKey(mappedStatementId);
    }
    
    public static void parse(String mappedStatementId,BoundSql boundSql){
        if(isFindBy(mappedStatementId , boundSql)){
            FindByMapper findByMapper = new FindByMapper(mappedStatementId);
            findByMapper.parseFindBy();
            FindByMetadata.put(mappedStatementId, findByMapper);
        }
    }
    
    public static String translate(FindByMapper findByMapper,Object parameterObject) {
        findByMapper.parseEntityClass();
        List<ColumnMapper> entityFields = ColumnMetadata.buildColumnMapper(findByMapper.getEntityClass());
        Query q = Query.builder();
        String fieldNameStart = findByMapper.getRemovedFindByName();
        int argIndex = 0;
        for(ColumnMapper fcm: entityFields) {
            String fieldName = fcm.getField();
            String columnName = fcm.getColumn();
            String findByKeyword = "";
            String capitalizeFieldName = StringUtils.capitalize(fieldName);
            if(fieldNameStart.startsWith(capitalizeFieldName)) {
                logger.trace("FieldName : {} , capitalize {}" , fieldName,capitalizeFieldName);
                if(fieldNameStart.length() >= fieldName.length()) {
                    fieldNameStart = fieldNameStart.substring(fieldName.length());
                    findByKeyword = FindByKeywords.startKeyword(fieldNameStart);
                    if(StringUtils.isNotBlank(findByKeyword) && !KEY.ORDER_BY.equals(findByKeyword)) {
                        fieldNameStart = fieldNameStart.substring(findByKeyword.length());
                    }
                    logger.trace("FindBy fieldNameStart : {} " , fieldNameStart);
                    if(StringUtils.isBlank(findByKeyword)){
                        findByKeyword = KEY.EQUALS;
                    }
                    logger.trace("FindBy Keyword : {} " , findByKeyword);
                }

                if(KEY.BETWEEN.equals(findByKeyword)) {
                    appendParameter(q,findByKeyword,columnName,((ParamMap<?>)parameterObject).get("arg0"),((ParamMap<?>)parameterObject).get("arg1"));
                    break;
                }else if(parameterObject instanceof ParamMap<?> paramMap) {
                    
                    Object parameterValue = paramMap.get("arg"+(argIndex++ ));
                    logger.trace("FindBy getCanonicalName : {} " , parameterValue.getClass().getCanonicalName());
                    if(KEY.AND.equals(findByKeyword)) {
                        appendParameter(q,KEY.EQUALS,columnName,parameterValue,null);
                        appendParameter(q,KEY.AND,columnName,parameterValue,null);
                    }else if(KEY.OR.equals(findByKeyword)) {
                        appendParameter(q,KEY.EQUALS,columnName,parameterValue,null);
                        appendParameter(q,KEY.OR,columnName,parameterValue,null);
                    }else {
                        appendParameter(q,findByKeyword,columnName,parameterValue,null);
                    }
                }else {
                    if(KEY.ORDER_BY.equals(findByKeyword)) {
                        appendParameter(q,KEY.EQUALS,columnName,parameterObject,null);
                    }else {
                        appendParameter(q,findByKeyword,columnName,parameterObject,null);
                    }
                }
                
                if(fieldNameStart.length() < fieldName.length() || StringUtils.isBlank(fieldNameStart)) {
                    logger.trace("FindBy break");
                    break;
                }
            }else {
                findByKeyword = FindByKeywords.startKeyword(fieldNameStart);
                if(StringUtils.isNotBlank(findByKeyword) && KEY.ORDER_BY.equals(findByKeyword)) {
                    logger.trace("FindBy Keyword : {} " , findByKeyword);
                    fieldNameStart = fieldNameStart.substring(findByKeyword.length());
                    logger.trace("FindBy order by columnName : {} " , fieldNameStart);
                    String orderBy = "asc";
                    if(fieldNameStart.endsWith("Desc")) {
                        orderBy = "desc";
                    }
                    columnName = getColumnNameFromEntityFields(entityFields,fieldNameStart);
                    appendParameter(q,KEY.ORDER_BY,columnName,orderBy,null);
                    break;
                }
            }
        }
        logger.trace("Query : {}" , q);
        SQL selectSql = TableMetadata.buildSelect(findByMapper.getEntityClass(),findByMapper.isDistinct());
        selectSql.WHERE("( " + QueryBuilder.build(q) + " )");

        ColumnMapper logicColumnMapper = ColumnMetadata.getLogicColumn(findByMapper.getEntityClass());
        if(logicColumnMapper != null && logicColumnMapper.isLogicDelete()) {
            selectSql.WHERE(" %s = '%s'"
                    .formatted(
                            logicColumnMapper.getColumn(),
                            logicColumnMapper.getSoftDelete().value())
                    );
        }
        
        if (q.getOrderBy() != null) {
            selectSql.ORDER_BY(ConditionBuilder.buildOrderBy(q.getOrderBy()));
        }
        logger.trace("selectSql : \n{}" , selectSql);
        return selectSql.toString();
    }
    
    public static String getColumnNameFromEntityFields(List<ColumnMapper> entityFields , String fieldNameStart) {
        String columnName = "";
        for(ColumnMapper fcm: entityFields) {
            String fieldName = fcm.getField();
            if(fieldNameStart.startsWith(StringUtils.capitalize(fieldName))) {
                columnName = fcm.getColumn();
                break;
            }
        }
        return columnName;
    }
    
    protected static void  appendParameter(Query q,String operator,String columnName, Object value, Object value1) {
        if(KEY.AND.equals(operator)) {
            q.and();
        }else if(KEY.OR.equals(operator)) {
            q.or();
        }else if(KEY.IS.equals(operator) || KEY.EQUALS.equals(operator)) {
            q.eq(columnName, value);
        }else if(KEY.BETWEEN.equals(operator)) {
            q.between(columnName, value, value1);
        }else if(KEY.LESS_THAN.equals(operator) || KEY.BEFORE.equals(operator)) {
            q.lt(columnName, value);
        }else if(KEY.LESS_THAN_EQUAL.equals(operator)) {
            q.le(columnName, value);
        }else if(KEY.GREATER_THAN.equals(operator) || KEY.AFTER.equals(operator)) {
            q.gt(columnName, value);
        }else if(KEY.GREATER_THAN_EQUAL.equals(operator)) {
            q.ge(columnName, value);
        }else if(KEY.IS_NULL.equals(operator) || KEY.NULL.equals(operator)) {
            q.isNull(columnName);
        }else if(KEY.IS_NOT_NULL.equals(operator) || KEY.NOT_NULL.equals(operator)) {
            q.isNotNull(columnName);
        }else if(KEY.LIKE.equals(operator)) {
            q.like(columnName, value);
        }else if(KEY.NOT_LIKE.equals(operator)) {
            q.notLike(columnName, value);
        }else if(KEY.STARTING_WITH.equals(operator)) {
            q.likeRight(columnName, value);
        }else if(KEY.ENDING_WITH.equals(operator)) {
            q.likeLeft(columnName, value);
        }else if(KEY.CONTAINING.equals(operator)) {
            q.like(columnName, value);
        }else if(KEY.ORDER_BY.equals(operator)) {
            q.orderBy(columnName,value.toString());
        }else if(KEY.NOT.equals(operator)) {
            q.notEq(columnName, value);
        }else if(KEY.IN.equals(operator)) {
            q.in(columnName, value);
        }else if(KEY.NOT_IN.equals(operator)) {
            q.notIn(columnName, value);
        }else if(KEY.TRUE.equals(operator)) {
            q.eq(columnName, true);
        }else if(KEY.FALSE.equals(operator)) {
            q.eq(columnName, false);
        }else if(KEY.IGNORE_CASE.equals(operator)) {
            q.ignoreCase(columnName, value);
        }
    }
}
