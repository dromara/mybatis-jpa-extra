package org.dromara.mybatis.jpa.util;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.junit.jupiter.api.Test;

public class JpaSpringContextTest {

	@Test
	public void version(){
		System.out.println(MybatisJpaContext.version());
	}
}
