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
 

package org.dromara.mybatis.jpa.test;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchPageResultsTestRunner {
	private static final Logger _logger = LoggerFactory.getLogger(FetchPageResultsTestRunner.class);
	public static StudentsService service;

	@Test
	public void fetchPageResults() throws Exception{
		
		_logger.info("fetchPageResults...");
		 Students student=new Students();
		 student.setPageSize(10);
		 student.calculate(21);
		 JpaPageResults<Students>  results = service.fetchPageResults(student);
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
	public void fetch() throws Exception{
		_logger.info("fetch...");
		 JpaPage page = new JpaPage();
		 Students student = new Students();
		 student.setStdGender("M");
		 student.setStdAge(40);
		 page.setPageSize(20);
		 page.setPageable(true);
		 
		 JpaPageResults<Students>  results = service.fetch(page,student);
		 List<Students> rowsStudents = results.getRows();
		 _logger.info("records {} , totalPage {} , total {} , page {} ",
				 results.getRecords(),results.getTotalPage(),results.getTotal(),results.getPage());
		 for (Students s : rowsStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Test
	public void fetchByCondition() throws Exception{
		_logger.info("fetchByCondition...");
		 JpaPage page = new JpaPage();
		 Query condition = new Query().eq("stdMajor", "政治").and().gt("STDAGE", 30);
		 page.setPageSize(20);
		 page.setPageable(true);
		 
		 JpaPageResults<Students>  results = service.fetch(page,condition);
		 List<Students> rowsStudents = results.getRows();
		 _logger.info("records {} , totalPage {} , total {} , page {} ",
				 results.getRecords(),results.getTotalPage(),results.getTotal(),results.getPage());
		 for (Students s : rowsStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Test
	public void fetchPageResultsByMapperId() throws Exception{

		_logger.info("fetchPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 
		 JpaPageResults<Students>  results =
				 service.fetchPageResults("fetchPageResults1",student);
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
	
	@BeforeAll
	public static void initSpringContext(){
		if(InitContext.context!=null) return;
		service = new InitContext().init();
	}

}