package org.apache.mybatis.jpa.test;

import java.util.List;

import org.apache.mybatis.jpa.id.SerialGenerator;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.domain.Students;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MybatisJpaApplication.class)
public class MybatisJpaApplicationTest{ 
    private static final Logger _logger = LoggerFactory.getLogger(MybatisJpaApplicationTest.class);
    
    @Autowired
    StudentsService studentsService;
    @Autowired
    org.apache.ibatis.session.SqlSessionFactory SqlSessionFactory;
   // @Autowired
    SerialGenerator serialGenerator;
    @Autowired
    private ApplicationContext applicationContext;
 
    
    @Before
    public  void before() {
    	_logger.info("---------------- before");
    	WebContext.applicationContext=applicationContext;

    }
    
	 @Test
	 public void findAll() {
			_logger.info("---------------- ALL");
			
			List<Students> allListStudents=studentsService.findAll();
			 for (Students s : allListStudents) {
				 _logger.info("Students "+s);
			 }
	 }

	
}
