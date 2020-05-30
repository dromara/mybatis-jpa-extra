package org.apache.mybatis.jpa.test;

import org.apache.mybatis.jpa.id.SerialGenerator;
import org.apache.mybatis.jpa.util.MacAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SerialGeneratorTest {
	private static final Logger _logger = LoggerFactory.getLogger(SerialGeneratorTest.class);
	@Test
	public  void generator() {
		// TODO Auto-generated method stub
		SerialGenerator serialGenerator=new SerialGenerator();
		_logger.info(serialGenerator.generate(""));
		_logger.info(MacAddress.getAllHostMacAddress());
	}

}
