package org.dromara.mybatis.jpa.id;

import org.junit.jupiter.api.Test;

public class UUIDGeneratorTest {

	@Test
	void generator(){
		UUIDGenerator uuidGenerator = new UUIDGenerator();
		System.out.println(uuidGenerator.generate(uuidGenerator));
	}
}
