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
 

package org.apache.mybatis.jpa.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.entity.Students;
import org.apache.mybatis.jpa.util.JpaWebContext;
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

    @Autowired
    private ApplicationContext applicationContext;
 
    
    @Before
    public  void before() {
    	_logger.info("---------------- before");
    	JpaWebContext.applicationContext=applicationContext;

    }
    
    @Test
	public void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		studentsService.insert(student);
		
		Thread.sleep(1000);
		studentsService.remove(student.getId());
		
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Students student=studentsService.get("921d3377-937a-4578-b1e2-92fb23b5e512");
		
		System.out.println("Students "+student);
		 _logger.info("Students "+student);

	}
	
	
	@Test
	public void remove() throws Exception{
		
		_logger.info("remove...");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		studentsService.remove(student.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		studentsService.deleteBatch(idList);
	}

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		 Students student=new Students();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 List<Students> allListStudents = 
				 studentsService.queryPageResults(student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Test
	public void queryPageResultsByMapperId() throws Exception{

		_logger.info("queryPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 
		 List<Students> allListStudents = 
				 studentsService.queryPageResults("queryPageResults1",student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
		 
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
