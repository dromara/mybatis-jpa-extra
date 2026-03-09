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
 

package org.dromara.mybatis.jpa.query;

import org.dromara.mybatis.jpa.query.builder.ConditionBuilder;
import org.dromara.mybatis.jpa.query.builder.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryTest {

    private static final Logger logger = LoggerFactory.getLogger(QueryTest.class);
    
    @Test
    void queryBuilder() {
        
        Query q = new Query().eq("cc", "ee").orderBy("aa", OrderBy.ASC);
        
        logger.debug("{}", QueryBuilder.build(q));
        
        logger.debug("{}", ConditionBuilder.buildOrderBy(q.getOrderBy()));
    }
}