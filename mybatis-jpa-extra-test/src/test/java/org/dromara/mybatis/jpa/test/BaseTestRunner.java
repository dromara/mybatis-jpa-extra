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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BaseTestRunner {
    private static final Logger _logger = LoggerFactory.getLogger(FetchPageResultsTestRunner.class);
    public static ApplicationContext context;
    
    public static StudentsService service;
    
    //Initialization ApplicationContext for Project
    public StudentsService init(){
        _logger.info("Init Spring Context...");
        SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime  = sdf_ymdhms.format(new Date());
        _logger.info("-- --Init Start at {}" , startTime);
        _logger.info("Application dir {}",System.getProperty("user.dir"));
        context = new ClassPathXmlApplicationContext(new String[] {"spring/applicationContext.xml"});

        MybatisJpaContext.init(context);
        service = (StudentsService)MybatisJpaContext.getBean(StudentsService.class);
        return service;
    }
    
    @BeforeAll
    public static void initSpringContext(){
        if(BaseTestRunner.context==null) {
            new BaseTestRunner().init();
        }
    }
}
