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

import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurdTestRunner  extends BaseTestRunner{
	private static final Logger _logger = LoggerFactory.getLogger(CurdTestRunner.class);
	
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
		service.insert(student);
		
		Thread.sleep(1000);
		_logger.info("insert id " + student.getId());
	}
	
	@Test
	void merge() throws Exception{
		_logger.info("merge...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		service.merge(student);
		
		Thread.sleep(1000);
		_logger.info("insert id " + student.getId());
		
	}

	@Test
	void get() throws Exception{
		_logger.info("get...");
		Students student=service.get("317d5eda-927c-4871-a916-472a8062df23");
		System.out.println("Students "+student);
		 _logger.info("Students "+student);
	}
	
	@Test
	public void update() throws Exception{
		_logger.info("get...");
		Students student=service.get("317d5eda-927c-4871-a916-472a8062df23");
		System.out.println("Students "+student);
		 _logger.info("Students "+student);
		 
		 _logger.info("update...");
		 student.setImages(null);
		 service.update(student);
		 _logger.info("updateed.");
		 
		 student.setImages("ssss".getBytes());
		 service.update(student);
		 _logger.info("updateed2.");
	}
	
	@Test
	void updateByQuery() throws Exception{
		_logger.info("updateByQuery...");
		service.update("id = '5'",Query.builder().eq("id", "2"));
	}
	
	@Test
	void remove() throws Exception{
		_logger.info("remove...");
		service.remove("921d3377-937a-4578-b1e2-92fb23b5e512");
	}
	
	@Test
	void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.deleteBatch(idList);
	}
	
	@Test
	void deleteByQuery() throws Exception{
		_logger.info("deleteByQuery...");
		service.delete(Query.builder().eq("id", "2"));
	}
	
}