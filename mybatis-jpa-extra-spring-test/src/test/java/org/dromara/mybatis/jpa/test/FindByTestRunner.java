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
 

package org.dromara.mybatis.jpa.test;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindByTestRunner extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(FindByTestRunner.class);
    
    @Test
    void findByStdNo(){
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
        service.findByStdMajorAndStdClass("数学","2");
    }
    
    @Test
    void findByBetween(){
        _logger.info("find by Between");
        service.findByStdAgeBetween(20,35);
    }
    
    @Test
    void findByLessThan(){
        _logger.info("find by LessThan");
        service.findByStdAgeLessThan(40);
    }
    
    @Test
    void findByLessThanEqual(){
        _logger.info("find by LessThanEqual");
        service.findByStdAgeLessThanEqual(40);
    }
    
    @Test
    void findByAfter(){
        _logger.info("find by After");
        service.findByStdAgeAfter(40);
    }
    
    @Test
    void findByBefore(){
        _logger.info("find by Before");
        service.findByStdAgeBefore(40);
    }
    
    
    @Test
    void findByNull(){
        _logger.info("find by Null");
        service.findByImagesNull();
    }
    
    @Test
    void findByIsNull(){
        _logger.info("find by IsNull");
        service.findByImagesIsNull();
    }
    
    @Test
    void findByIsNotNull(){
        _logger.info("find by IsNotNull");
        service.findByImagesIsNotNull();
    }
    
    @Test
    void findByNotNull(){
        _logger.info("find by NotNull");
        service.findByImagesNotNull();
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
        service.findByStdGenderOrderByStdAge("F");
    }
    
    @Test
    void findByIsOrderBy(){
        _logger.info("find by OrderBy");
        service.findByStdGenderIsOrderByStdAge("F");
    }
    
    @Test
    void findByIn(){
        _logger.info("find by In");
        service.findByStdMajorIn(new String[]{"政治","化学"});
    }
    
    @Test
    void findByNotIn(){
        _logger.info("find by NotIn");
        service.findByStdMajorNotIn(List.of("数学","政治"));
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
        service.findByStdGenderIgnoreCase("F");
    }
    
}