/*
 * Copyright [2022] [MaxKey of copyright http://www.maxkey.top]
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

import java.util.List;

import org.apache.mybatis.jpa.persistence.JpaPageResults;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.entity.Students;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageResultsTestRunner {
	private static final Logger _logger = LoggerFactory.getLogger(PageResultsTestRunner.class);
	public static StudentsService service;

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		 Students student=new Students();
		 //student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 //student.setPageNumber(2);
		 student.calculate(21);
		 JpaPageResults<Students>  results = service.queryPageResults(student);
		 List<Students> rowsStudents = results.getRows();
		 long records =results.getRecords();//当前页记录数量
		 long totalPage =results.getTotalPage();//总页数
		 long total =results.getTotal();//总数据量
		 long page =results.getPage();//当前页
		 _logger.info("records {} , totalPage {} , total {} , page {} ",
				 records,totalPage,total,page);
		 for (Students s : rowsStudents) {
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
		 
		 JpaPageResults<Students>  results =
				 service.queryPageResults("queryPageResults1",student);
		 List<Students> rowsStudents = results.getRows();
		 long records =results.getRecords();//当前页记录数量
		 long totalPage =results.getTotalPage();//总页数
		 long total =results.getTotal();//总数据量
		 long page =results.getPage();//当前页
		 _logger.info("records {} , totalPage {} , total {} , page {} ",
				 records,totalPage,total,page);
		 for (Students s : rowsStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Before
	public void initSpringContext(){
		if(InitContext.context!=null) return;
		service = new InitContext().init();
	}
	

}