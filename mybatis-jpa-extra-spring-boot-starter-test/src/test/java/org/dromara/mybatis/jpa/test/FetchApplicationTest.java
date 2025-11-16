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


package org.dromara.mybatis.jpa.test;

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;

@SpringBootTest(useMainMethod = UseMainMethod.ALWAYS)
public class FetchApplicationTest {
    private static final Logger _logger = LoggerFactory.getLogger(FetchApplicationTest.class);
    
    @Autowired
    StudentsService service;

    @Test
    void fetch(){
        _logger.info("fetch...");
         JpaPage page = new JpaPage();
         Students student = new Students();
         student.setStdGender("M");
         student.setStdAge(40);
         page.setPageSize(20);
         page.setPageable(true);
         
         JpaPageResults<Students>  results = service.fetch(page,student);
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
    
    @Test
    void fetchByQuery(){
        _logger.info("fetch By Query...");
         JpaPage page = new JpaPage();
         page.setPageSize(20);
         page.setPageable(true);
         
         Query condition = new Query().isNotNull("stdMajor");//.eq("stdMajor", "政治").and().gt("STDAGE", 30);
         
         JpaPageResults<Students>  results = service.fetch(page,condition);
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
    
    @Test
    void fetchByLambdaQuery(){
        _logger.info("fetch By LambdaQuery...");
         JpaPage page = new JpaPage();
         page.setPageSize(20);
         page.setPageable(true);
         LambdaQuery<Students> lambdaQuery =new LambdaQuery<>();
         //lambdaQuery.eq(Students::getStdMajor, "政治").and().gt(Students::getStdAge, Integer.valueOf(30));
         JpaPageResults<Students>  results = service.fetch(page,lambdaQuery);
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
}