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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindByTestRunner extends BaseUnderscoreTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(FindByTestRunner.class);
    
    @Test
    void findBy(){
        _logger.info("find by");
        service.findByStdNo("10024");
    }
    
    @Test
    void findByIs(){
        _logger.info("find by Is");
        service.findByStdNoIs("10024");
    }
    
    @Test
    void findByEquals(){
        _logger.info("find by Equals");
        service.findByStdNoEquals("10024");
    }
    
    @Test
    void findByNot(){
        _logger.info("find by Not");
        service.findByStdNoNot("10024");
    }
    
    @Test
    void findByAnd(){
        _logger.info("find by 2");
        service.findByStdNoAndCourseId("1000","2");
    }
    
    @Test
    void findByBetween(){
        _logger.info("find by Between");
        service.findByGradeBetween(60,80);
    }
    
    @Test
    void findByLessThan(){
        _logger.info("find by LessThan");
        service.findByGradeLessThan(60);
    }
    
    @Test
    void findByLessThanEqual(){
        _logger.info("find by LessThanEqual");
        service.findByGradeLessThanEqual(60);
    }
    
    @Test
    void findByLike(){
        _logger.info("find by Like");
        service.findByStdNameLike("孙");
    }
    
    @Test
    void findByNotLike(){
        _logger.info("find by NotLike");
        service.findByStdNameNotLike("孙");
    }
    
    @Test
    void findByStartingWith(){
        _logger.info("find by StartingWith");
        service.findByStdNameStartingWith("孙");
    }
    
    @Test
    void findByEndingWith(){
        _logger.info("find by EndingWith");
        service.findByStdNameEndingWith("孙");
    }
    
    @Test
    void findByContaining(){
        _logger.info("find by Containing");
        service.findByStdNameContaining("孙");
    }
    
    @Test
    void findByOrderBy(){
        _logger.info("find by OrderBy");
        service.findByCourseNameOrderByGrade("F");
    }
    
    @Test
    void findByIsOrderBy(){
        _logger.info("find by OrderBy");
        service.findByCourseNameIsOrderByGrade("F");
    }
    
    @Test
    void findByIn(){
        _logger.info("find by In");
        service.findByCourseNameIn(new String[]{"政治","化学"});
    }
    
    @Test
    void findByNotIn(){
        _logger.info("find by NotIn");
        service.findByCourseNameNotIn(List.of("数学","政治"));
    }
    
    @Test
    void findByTrue(){
        _logger.info("find by True");
        service.findByDeletedTrue();
    }
    
    @Test
    void findByFalse(){
        _logger.info("find by False");
        service.findByDeletedFalse();
    }
    
    @Test
    void findByIgnoreCase(){
        _logger.info("find by IgnoreCase");
        service.findByCourseNameIgnoreCase("F");
    }
    
}