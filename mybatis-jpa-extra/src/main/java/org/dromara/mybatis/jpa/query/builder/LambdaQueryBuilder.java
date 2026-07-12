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
 

package org.dromara.mybatis.jpa.query.builder;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.dromara.mybatis.jpa.handler.SafeValueHandler;
import org.dromara.mybatis.jpa.query.Condition;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes"})
public class LambdaQueryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(LambdaQueryBuilder.class);
    
    private static final int MAX_NESTED_DEPTH = 3;
    
    public static String build(LambdaQuery lambdaQuery) {
        return build(lambdaQuery, 0);
    }
    
    private static String build(LambdaQuery lambdaQuery, int currentDepth) {
        if (lambdaQuery == null || CollectionUtils.isEmpty(lambdaQuery.getConditions())) {
            return "";
        }
        if (currentDepth > MAX_NESTED_DEPTH) {
            throw new IllegalStateException("LambdaQuery nested depth exceeds the maximum limit of " + MAX_NESTED_DEPTH);
        }
        StringBuilder conditionString = new StringBuilder("");
        List<Condition> conditions = lambdaQuery.getConditions();
        Operator lastExpression = Operator.AND;
        for (Condition condition : conditions) {
            Operator expression = condition.getExpression();
            Object value = condition.getValue();
            String column = SafeValueHandler.safeColumn(condition.getColumn());
            condition.setColumn(column);
            if (expression.equals(Operator.AND) || expression.equals(Operator.OR)) {
                lastExpression = condition.getExpression();
                if (value instanceof LambdaQuery subQuery) {
                    ConditionBuilder.appendSubQuery(conditionString,build(subQuery,currentDepth + 1),lastExpression);
                }
            }else {
                ConditionBuilder.build(conditionString, column, expression, lastExpression, value, value);
            }
        }
        logger.trace("conditionString {}" , conditionString);
        return conditionString.toString();
    }
    
}
