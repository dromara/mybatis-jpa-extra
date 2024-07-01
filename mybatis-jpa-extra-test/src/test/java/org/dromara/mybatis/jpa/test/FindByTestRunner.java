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

import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindByTestRunner extends BaseTestRunner{
	private static final Logger _logger = LoggerFactory.getLogger(FindByTestRunner.class);
	
	@Test
	void findBy(){
		_logger.info("find by");

		List<Students>  students = service.findByStdNo("10024");
		for (Students s : students) {
			 _logger.info("Students {}" , s);
		 }
	}
	
	@Test
	void findBy2(){
		_logger.info("find by 2");

		List<Students>  students = service.findByStdMajorAndStdClass("数学","2");
		for (Students s : students) {
			 _logger.info("Students {}" , s);
		 }
	}
	

}