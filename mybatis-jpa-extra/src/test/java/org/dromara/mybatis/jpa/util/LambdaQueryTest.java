package org.dromara.mybatis.jpa.util;

import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.LambdaQueryBuilder;

public class LambdaQueryTest {

	public static void main(String[] args) {
		LambdaQuery<Stds> lambdaQuery = new LambdaQuery<>();
		
		lambdaQuery.between(Stds::getStdName, 1,20);
		
		System.out.println(lambdaQuery.getConditions());
		
		System.out.println(LambdaQueryBuilder.build(lambdaQuery));
		
	}

}
