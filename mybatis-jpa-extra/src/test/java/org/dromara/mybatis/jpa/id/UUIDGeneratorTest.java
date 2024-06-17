package org.dromara.mybatis.jpa.id;

import org.junit.jupiter.api.Test;

public class UUIDGeneratorTest {

	@Test
	public void generator() throws Exception{
		UUIDGenerator uuidGenerator = new UUIDGenerator();
		System.out.println(uuidGenerator.generate(uuidGenerator));
	}
}
