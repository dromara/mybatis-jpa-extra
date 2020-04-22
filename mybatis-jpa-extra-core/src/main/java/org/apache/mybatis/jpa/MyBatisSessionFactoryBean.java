package org.apache.mybatis.jpa;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.mybatis.jpa.dialect.Dialect;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyBatisSessionFactoryBean extends SqlSessionFactoryBean {
	protected Logger _logger = LoggerFactory.getLogger(MyBatisSessionFactoryBean.class);
	private List<Interceptor> interceptors = Collections.emptyList();
	
	private int timeout = 30 ;
	private String dialect = Dialect.DEFAULT_DIALECT;
	
	public void setInterceptors(List<Interceptor> interceptors) {
		this.interceptors = interceptors;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
		SqlSessionFactory factory = super.buildSqlSessionFactory();
		
		
		Configuration config = factory.getConfiguration();
		_logger.debug("buildSqlSessionFactory : "+config.toString());
		for (Interceptor interceptor : interceptors) {
			config.addInterceptor(interceptor);
		}
		
		StatementHandlerInterceptor statementHandlerInterceptor =new StatementHandlerInterceptor();
		statementHandlerInterceptor.setDialectString(Dialect.getDialect(dialect));
		config.addInterceptor(statementHandlerInterceptor);
		
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