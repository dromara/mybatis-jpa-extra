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

package org.dromara.mybatis.jpa.query.builder;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.query.Condition;
import org.dromara.mybatis.jpa.query.ConditionValue;
import org.dromara.mybatis.jpa.query.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConditionBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ConditionBuilder.class);
            
    public static String build(StringBuilder conditionString,String column,Operator expression,Operator lastExpression,Object value,Object value2) {
        if (expression == null || StringUtils.isBlank(column)) {
            return conditionString.toString();
        }
        if (expression.equals(Operator.CONDITION)) {
            conditionString.append(column);
            return conditionString.toString();
        } 
        logger.trace("Expression {} column {} value class {}",lastExpression,column,value == null ? "" : value.getClass().getCanonicalName());
        conditionString.append(ConditionBuilder.appendExpression(conditionString.toString(),lastExpression));
        switch (expression) {
            case LIKE:
            case NOT_LIKE:
                conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
                conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("%'");
                break;
            case IGNORE_CASE:
                conditionString.append("UPPER(").append(column).append(") ").append(Operator.EQ.getOperator()).append(" ");
                conditionString.append("UPPER('").append(SafeValueHandler.valueOf(value)).append("')");
                break;
            case LIKE_LEFT:
                conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
                conditionString.append("'%").append(SafeValueHandler.valueOf(value)).append("'");
                break;
            case LIKE_RIGHT:
                conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
                conditionString.append("'").append(SafeValueHandler.valueOf(value)).append("%'");
                break;
            case EQ:   
            case NOT_EQ:   
            case GT:   
            case GE:   
            case LT:   
            case LE:   
                conditionString.append(column).append(" ").append(expression.getOperator()).append(" ");
                conditionString.append(SafeValueHandler.valueOfType(value));
                break;
            case BETWEEN:   
            case NOT_BETWEEN:  
                conditionString.append(" ( ").append(column).append(" ").append(expression.getOperator()).append(" ");
                conditionString.append(SafeValueHandler.valueOfType(value));
                conditionString.append(" and ");
                conditionString.append(SafeValueHandler.valueOfType(value2)).append(" ) ");
                break;
            case IS_NULL:
            case IS_NOT_NULL:
                conditionString.append(column).append(" ").append(expression.getOperator());
                break;
            case IN:
            case NOT_IN:
                if (value != null) {
                    String inValues  = ConditionValue.getCollectionValues(value);
                    if(StringUtils.isNotBlank(inValues)) {
                        conditionString.append(column).append(" ").append(expression.getOperator());
                        conditionString.append(" ( ").append(inValues).append(" ) ");
                    }
                }
                break;
        }
        return conditionString.toString();
    }
    
    public static String appendExpression(String conditionString , Operator lastExpression) {
        return StringUtils.isBlank(conditionString) ? "" : " " + lastExpression + " ";
    }

    
    public static void appendSubQuery(StringBuilder conditionString, String conditionSubString,Operator lastExpression) {
        if(StringUtils.isNotBlank(conditionSubString)) {
            conditionString.append(appendExpression(conditionString.toString(),lastExpression));
            conditionString.append(" ( ").append(conditionSubString).append(" ) ");
        }    
    }
    
    public static String buildGroupBy(List<Condition> conditions) {
        if(CollectionUtils.isEmpty(conditions)) {
            return "";
        }
        boolean isFirst = true;
        StringBuilder groupBy = new StringBuilder();
        for (Condition condition : conditions) {
            if (!isFirst) {
                groupBy.append(" , ");
            }
            groupBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
            isFirst = false;
        }
        return groupBy.toString();
    }

    public static String buildOrderBy(List<Condition> conditions) {
        if(CollectionUtils.isEmpty(conditions)) {
            return "";
        }
        boolean isFirst = true;
        StringBuilder orderBy = new StringBuilder();
        for (Condition condition : conditions) {
            if (!isFirst) {
                orderBy.append(" , ");
            }
            orderBy.append(SafeValueHandler.safeColumn(condition.getColumn()));
            orderBy.append(" ");
            orderBy.append(SafeValueHandler.valueOf(condition.getValue()));
            isFirst = false;
        }
        return orderBy.toString();
    }
    
}
