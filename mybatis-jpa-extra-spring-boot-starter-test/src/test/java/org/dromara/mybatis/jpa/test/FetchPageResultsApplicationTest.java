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

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.StudentQueryDto;
import org.dromara.mybatis.jpa.test.entity.StudentVo;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;

@SpringBootTest(useMainMethod = UseMainMethod.ALWAYS)
public class FetchPageResultsApplicationTest {
    private static final Logger _logger = LoggerFactory.getLogger(FetchPageResultsApplicationTest.class);
    
    @Autowired
    StudentsService service;
    
    @Test
    void fetchPageResults(){
        
        _logger.info("fetchPageResults...");
         Students student=new Students();
         student.setPageNumber(2);
         student.setPageSize(10);
         JpaPageResults<Students>  results = service.fetchPageResults(student);

         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
    
    @Test
    void fetchPageResultsByMapperId(){
        _logger.info("fetchPageResults by mapperId...");
         Students student=new Students();
         student.setStdGender("M");
         student.setPageSize(10);
         student.setPageNumber(2);
         
         JpaPageResults<Students>  results =service.fetchPageResults("fetchPageResults1",student);
         
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
    
    @Test
    void fetchPageResultsByDto(){
        _logger.info("fetchPageResults by fetchPageResultsByDto...");
         StudentQueryDto student=new StudentQueryDto();
         student.setStdGender("M");
         student.setPageSize(10);
         student.setPageNumber(2);
         
         JpaPageResults<StudentVo> results = service.fetchPageResultsVo(student);
         
         _logger.info("records {} , total {} , totalPage {} , page {} ",
                 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
    }
}
