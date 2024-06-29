/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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
import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

//@SpringBootApplication(scanBasePackages = {"org.apache.mybatis.jpa"})
@SpringBootApplication
@MapperScan("org.dromara.mybatis.jpa.test.dao.persistence")
public class MybatisJpaApplication implements ApplicationRunner{ 
    private static final Logger _logger = LoggerFactory.getLogger(MybatisJpaApplication.class);
    
    @Autowired
    StudentsService studentsService;
    
    @Autowired
    private ApplicationContext applicationContext;
    /**
     * @param args args
     */
    public static void main(String[] args) {
    	new SpringApplicationBuilder(MybatisJpaApplication.class) 
         .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET 
         .bannerMode(Banner.Mode.OFF).run(args) ;
    	
    	//application.run(args);
    }
    
	@Override
	public void run(ApplicationArguments args) throws Exception {
		MybatisJpaContext.init(applicationContext);
		 
		_logger.info("queryPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 JpaPageResults<Students>  jpaPageResults=studentsService.fetchPageResults("fetchPageResults1",student);
		 for (Students s : jpaPageResults.getRows()) {
			 _logger.info("Students {}",s);
		 }
		
	}
	
}
