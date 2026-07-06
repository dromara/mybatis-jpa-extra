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

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.test.entity.StudentsInst;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchInstTestRunner  extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(FetchInstTestRunner.class);
    
    @Test
    void fetch(){
        _logger.info("fetch...");
         JpaPage page = new JpaPage(1);
         StudentsInst student = new StudentsInst();
         student.setStdGender("M");
         student.setInstId("1");
         
         JpaPageResults<StudentsInst>  results = serviceInst.fetch(page,student);
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }


}