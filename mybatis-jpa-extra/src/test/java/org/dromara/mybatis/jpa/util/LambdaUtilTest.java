package org.dromara.mybatis.jpa.util;

import org.junit.jupiter.api.Test;

public class LambdaUtilTest {

	@Test
	public  void getColumnName() {
		System.out.println(LambdaUtil.getColumnName(Stds::getStdNo));
		System.out.println(LambdaUtil.getColumnName(Stds::getDeleted));
	}

}
