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
 

package org.dromara.mybatis.jpa.test;

import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryTestRunner  extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(QueryTestRunner.class);
    
    @Test
    void query(){
        _logger.info("find...");
        service.query(new Students("10024"));
    }
    
    //WHERE (stdMajor = '政治' and STDAGE > 30 and stdMajor in ( '政治' , '数学' )  or  ( stdname = '周瑜' or stdname = '吕蒙' ) )
    @Test
    void queryByQuery(){
        _logger.info("query by query ...");
        service.query(
                new Query().eq("stdMajor", "政治").gt("STDAGE", 30).in("stdMajor", new Object[]{"政治","数学"})
                .or(new Query().eq("stdname", "周瑜").or().eq("stdname", "吕蒙")));
    }

}