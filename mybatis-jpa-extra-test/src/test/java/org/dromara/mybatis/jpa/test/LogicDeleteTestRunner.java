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

import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogicDeleteTestRunner {
	private static final Logger _logger = LoggerFactory.getLogger(LogicDeleteTestRunner.class);
	public static StudentsService service;

	@Test
	void logicDelete() throws Exception{
		_logger.info("batchDeleteByIds...");
		service.logicDelete("2");
		service.logicDelete("2,639178432667713536");
	}
	
	@Test
	void logicBatchDelete() throws Exception{
		_logger.info("logicDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.logicDelete(idList);
	}
	
	@Test
	void logicDeleteSplit() throws Exception{
		_logger.info("batchDeleteByIds...");
		service.logicDeleteSplit("2,639178432667713536",",");
	}
	
	@BeforeAll
	public static void initSpringContext(){
		if(InitContext.context!=null) return;
		service = new InitContext().init();
	}
}