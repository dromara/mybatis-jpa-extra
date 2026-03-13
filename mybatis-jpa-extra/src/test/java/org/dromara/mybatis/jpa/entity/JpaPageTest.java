package org.dromara.mybatis.jpa.entity;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JpaPageTest {
	private static final Logger logger = LoggerFactory.getLogger(JpaPageTest.class);
	
	@Test
	public void testPage() {
		JpaPage page = new JpaPage();
		page.setPageNumber(2);
		page.setPageSize(10);
		page.setSortKey("name");
		page.setSortOrder("asc");
			
		page.setPageable(true);
		page.calculate();
		page.buildOrderBy();
		logger.debug("JpaPage: {}", page);
	}
}
