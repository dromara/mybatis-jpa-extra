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


package org.dromara.mybatis.jpa.test.disabled;

import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.dromara.mybatis.jpa.test.MybatisJpaApplication;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = MybatisJpaApplication.class)
@Disabled("true")
public class MybatisJpaApplicationTest{
    private static final Logger _logger = LoggerFactory.getLogger(MybatisJpaApplicationTest.class);

    @Autowired
    StudentsService studentsService;

    @Autowired
    org.apache.ibatis.session.SqlSessionFactory SqlSessionFactory;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    public  void before() {
    	_logger.info("---------------- before");
    	MybatisJpaContext.init(applicationContext);

    }

    @Test
	void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		studentsService.insert(student);

		studentsService.delete(student.getId());

	}

	@Test
	void get(){
		_logger.info("get...");
		Students student=studentsService.get("921d3377-937a-4578-b1e2-92fb23b5e512");

		 _logger.info("Students {}",student);

	}

	@Test
	void remove(){

		_logger.info("remove...");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		studentsService.delete(student.getId());

	}

	@Test
	void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		studentsService.deleteBatch(idList);
	}

	@Test
	void fetchPageResults(){

		_logger.info("fetchPageResults...");
		 Students student=new Students();
		 student.setStdGender("M");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 List<Students> allListStudents =
				 studentsService.fetchPageResults(student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students {}",s);
		 }
	}

	@Test
	void fetchPageResultsByMapperId(){

		_logger.info("fetchPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 student.setPageSize(10);
		 student.setPageNumber(2);

		 List<Students> allListStudents =
				 studentsService.fetchPageResults("fetchPageResults1",student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students {}",s);
		 }

	}


	 @Test
	 void findAll() {
			_logger.info("---------------- ALL");

			List<Students> allListStudents=studentsService.findAll();
			 for (Students s : allListStudents) {
				 _logger.info("Students {}",s);
			 }
	 }

}
