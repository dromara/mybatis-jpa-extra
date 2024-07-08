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
 

package org.dromara.mybatis.jpa.util;

import org.dromara.mybatis.jpa.id.impl.SnowFlakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowFlakeIdGeneratorTest {
	private static final Logger _logger = LoggerFactory.getLogger(SnowFlakeIdGeneratorTest.class);
	@Test
	 void generator() {
		SnowFlakeIdGenerator uhg=new SnowFlakeIdGenerator();
		_logger.info(uhg.generate(""));
		_logger.info(uhg.generate(""));
		StringBuffer conditionString =new StringBuffer("");
		_logger.info("length {}",conditionString.length());
	}

}