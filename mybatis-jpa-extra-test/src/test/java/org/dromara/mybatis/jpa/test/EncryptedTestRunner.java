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

import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncryptedTestRunner  extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(EncryptedTestRunner.class);
    
    @Test
    void insert(){
        _logger.info("insert...");
        Students student=new Students();
        student.setStdNo("10024");
        student.setStdGender("M");
        student.setStdName("司马昭");
        student.setStdAge(20);
        student.setStdMajor("政治");
        student.setStdClass("4");
        student.setPassword("password");
        service.insert(student);
        
        _logger.info("insert id {}" , student.getId());
    }

    
    @Test
    void update(){
        _logger.info("get...");
        Students student=service.get("1007058902925180928");
         _logger.info("Students {}",student);
         
         _logger.info("update...");
         student.setImages(null);
         service.update(student);
         _logger.info("updateed.");
         
         student.setImages("ssss".getBytes());
         student.setPassword("12345");
         service.update(student);
         _logger.info("updateed2.");
    }
    
    @Test
    void updatePassword(){
        _logger.info("get...");
        Students student = service.get("1007058902925180928");
        _logger.info("Students {}", student);

        student.setPassword("12345");
        service.updatePassword(student);
        _logger.info("updatePassword.");
    }
    
    
}