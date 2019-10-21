package org.apache.mybatis.jpa;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyBatisSessionFactoryBean extends SqlSessionFactoryBean {
	protected Logger _logger = LoggerFactory.getLogger(MyBatisSessionFactoryBean.class);
	private List<Interceptor> interceptors = Collections.emptyList();
	
	private int timeout = 30 ;
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
  
	protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
		SqlSessionFactory factory = super.buildSqlSessionFactory();
		
		
		Configuration config = factory.getConfiguration();
		_logger.debug("buildSqlSessionFactory : "+config.toString());
		for (Interceptor interceptor : interceptors) {
			config.addInterceptor(interceptor);
		}

		_logger.debug("DefaultStatementTimeout : "+ config.getDefaultStatementTimeout());
		config.setDefaultStatementTimeout(timeout);
		_logger.debug("after change ,DefaultStatementTimeout : "+ config.getDefaultStatementTimeout());
		_logger.debug(""+config.getMappedStatementNames());
		return factory;
	}
	
	public SqlSessionFactory build() throws Exception {
		return buildSqlSessionFactory();
	}
}