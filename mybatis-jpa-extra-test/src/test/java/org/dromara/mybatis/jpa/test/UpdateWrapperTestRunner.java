/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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
import java.util.Arrays;
import java.util.List;

import org.dromara.mybatis.jpa.test.entity.Students;
import org.dromara.mybatis.jpa.update.LambdaUpdateWrapper;
import org.dromara.mybatis.jpa.update.UpdateWrapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateWrapperTestRunner  extends BaseTestRunner{
	private static final Logger _logger = LoggerFactory.getLogger(UpdateWrapperTestRunner.class);

	@Test
	void updateByLambdaUpdateWrapper(){
		_logger.info("update by LambdaUpdateWrapper ...");
		List<String> majorList = new ArrayList<>(Arrays.asList("政治","化学"));
		LambdaUpdateWrapper<Students> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(Students::getStdMajor, "政治").and().gt(Students::getStdAge, Integer.valueOf(30)).and().in(Students::getStdMajor, majorList)
		.or(new LambdaUpdateWrapper<Students>().eq(Students::getStdName, "周瑜").or().eq(Students::getStdName, "吕蒙"));
		updateWrapper.set(Students::getStdMajor, "历史");
		
		service.update(updateWrapper);
		
	}
	
	@Test
	void updateByUpdateWrapper(){
		_logger.info("update by UpdateWrapper ...");
		UpdateWrapper updateWrapper = new UpdateWrapper();
		updateWrapper.eq("StdName", "周瑜").or().eq("StdName", "吕蒙");
		updateWrapper.set("StdMajor", "历史");
		
		service.update(updateWrapper);
	}
	

}