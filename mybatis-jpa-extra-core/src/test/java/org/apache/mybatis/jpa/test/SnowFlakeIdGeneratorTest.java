package org.apache.mybatis.jpa.test;

import org.apache.mybatis.jpa.id.SnowFlakeIdGenerator;
import org.apache.mybatis.jpa.id.UUIDHexGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowFlakeIdGeneratorTest {
	private static final Logger _logger = LoggerFactory.getLogger(SnowFlakeIdGeneratorTest.class);
	@Test
	public  void generator() {
		// TODO Auto-generated method stub
		SnowFlakeIdGenerator uhg=new SnowFlakeIdGenerator();
		_logger.info(uhg.generate(""));
		_logger.info(uhg.generate(""));
	}

}
