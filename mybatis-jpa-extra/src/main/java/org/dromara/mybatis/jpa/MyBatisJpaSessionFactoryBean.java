/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 

package org.dromara.mybatis.jpa;

import java.util.Collections;
import java.util.List;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.mybatis.jpa.dialect.Dialect;
import org.dromara.mybatis.jpa.interceptor.StatementHandlerInterceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MyBatisJpaSessionFactoryBean
 */
public class MyBatisJpaSessionFactoryBean extends SqlSessionFactoryBean {
	protected static Logger  logger = LoggerFactory.getLogger(MyBatisJpaSessionFactoryBean.class);
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
		logger.debug("buildSqlSessionFactory : {}" , config.toString());
		for (Interceptor interceptor : interceptors) {
			config.addInterceptor(interceptor);
		}
		
		//设置
		StatementHandlerInterceptor statementHandlerInterceptor =new StatementHandlerInterceptor();
		statementHandlerInterceptor.setDialectString(Dialect.getDialect(dialect));
		config.addInterceptor(statementHandlerInterceptor);
		
		
		if(config.getDefaultStatementTimeout() == null 
				|| config.getDefaultStatementTimeout() == 0) {
			logger.debug("set StatementTimeout as default");
			config.setDefaultStatementTimeout(timeout);
		}
		logger.debug("DefaultStatementTimeout : {}" , config.getDefaultStatementTimeout());
		if(logger.isTraceEnabled()) {
			for(String mappedStatementName : config.getMappedStatementNames()) {
				logger.trace("MappedStatementName {} " ,mappedStatementName);
			}
		}
		return factory;
	}
	
	public SqlSessionFactory build() throws Exception {
		return buildSqlSessionFactory();
	}


	
}