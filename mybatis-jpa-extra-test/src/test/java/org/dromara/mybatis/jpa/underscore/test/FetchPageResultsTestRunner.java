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
 

package org.dromara.mybatis.jpa.underscore.test;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.test.entity.Scores;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchPageResultsTestRunner  extends BaseUnderscoreTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(FetchPageResultsTestRunner.class);

    @Test
    void fetchPageResults(){
        
        _logger.info("fetchPageResults...");
        Scores score=new Scores();
        score.setPageNumber(2);
        score.setPageSize(10);
        JpaPageResults<Scores>  results = service.fetchPageResults(score);

        _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
    
    @Test
    void fetchPageResultsByMapperId(){
        _logger.info("fetchPageResults by mapperId...");
        Scores score=new Scores();
        score.setCourseId("1000");
        score.setPageSize(10);
        score.setPageNumber(2);
         
         JpaPageResults<Scores>  results =service.fetchPageResults("fetchPageResults1",score);
         
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }

}