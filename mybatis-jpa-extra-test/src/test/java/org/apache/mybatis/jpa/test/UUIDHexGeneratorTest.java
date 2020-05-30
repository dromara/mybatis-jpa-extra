package org.apache.mybatis.jpa.test;

import org.apache.mybatis.jpa.id.UUIDHexGenerator;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UUIDHexGeneratorTest {
	private static final Logger _logger = LoggerFactory.getLogger(UUIDHexGeneratorTest.class);
	@Test
	public  void generator() {
		// TODO Auto-generated method stub
		UUIDHexGenerator uhg=new UUIDHexGenerator();
		_logger.info(uhg.generate(""));
		_logger.info(uhg.generate(""));
	}

}
