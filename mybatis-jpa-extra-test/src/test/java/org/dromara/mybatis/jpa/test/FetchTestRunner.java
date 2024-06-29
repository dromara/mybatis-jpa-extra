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

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchTestRunner  extends BaseTestRunner{
	private static final Logger _logger = LoggerFactory.getLogger(FetchTestRunner.class);
	
	@Test
	void fetch(){
		_logger.info("fetch...");
		 JpaPage page = new JpaPage();
		 Students student = new Students();
		 student.setStdGender("M");
		 student.setStdAge(40);
		 page.setPageSize(20);
		 page.setPageable(true);
		 
		 JpaPageResults<Students>  results = service.fetch(page,student);
		 _logger.info("records {} , total {} , totalPage {} , page {} ",
				 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
		 for (Students s : results.getRows()) {
			 _logger.info("Students {}",s);
		 }
	}
	
	@Test
	void fetchByCondition(){
		_logger.info("fetchByCondition...");
		 JpaPage page = new JpaPage();
		 page.setPageSize(20);
		 page.setPageable(true);
		 
		 Query condition = new Query().eq("stdMajor", "政治").and().gt("STDAGE", 30);
		 
		 JpaPageResults<Students>  results = service.fetch(page,condition);
		 _logger.info("records {} , total {} , totalPage {} , page {} ",
				 results.getRecords(),results.getTotal(),results.getTotalPage(),results.getPage());
		 for (Students s : results.getRows()) {
			 _logger.info("Students {}",s);
		 }
	}

}