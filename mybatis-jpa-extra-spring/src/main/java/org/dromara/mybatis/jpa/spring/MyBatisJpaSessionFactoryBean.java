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
 

package org.dromara.mybatis.jpa.spring;

import java.util.Collections;
import java.util.List;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.mybatis.jpa.crypto.EncryptFactory;
import org.dromara.mybatis.jpa.crypto.utils.ReciprocalUtils;
import org.dromara.mybatis.jpa.dialect.DialectMapper;
import org.dromara.mybatis.jpa.interceptor.FieldAutoFillSpringInterceptor;
import org.dromara.mybatis.jpa.interceptor.FieldDecryptInterceptor;
import org.dromara.mybatis.jpa.interceptor.FieldEncryptInterceptor;
import org.dromara.mybatis.jpa.interceptor.StatementHandlerInterceptor;
import org.dromara.mybatis.jpa.interceptor.TraceSqlIntercept;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * MyBatisJpaSessionFactoryBean
 */
public class MyBatisJpaSessionFactoryBean extends SqlSessionFactoryBean {
    protected static Logger  logger = LoggerFactory.getLogger(MyBatisJpaSessionFactoryBean.class);
    
    private int timeout                        = 30 ;
    
    private String dialect                     = DialectMapper.DEFAULT_DIALECT;
    
    private String cryptKey                    = ReciprocalUtils.DEFAULT_KEY;
    
    private List<Interceptor> interceptors     = Collections.emptyList();
    
    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
    
    public void setCryptKey(String cryptKey) {
        this.cryptKey = cryptKey;
    }

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {
        SqlSessionFactory factory = super.buildSqlSessionFactory();
        
        Configuration config = factory.getConfiguration();
        logger.debug("buildSqlSessionFactory : {}" , config);
        for (Interceptor interceptor : interceptors) {
            config.addInterceptor(interceptor);
        }
        
        //for @Encrypt
        MapperMetadata.setEncryptFactory(new EncryptFactory(this.cryptKey));
        
        //设置
        StatementHandlerInterceptor statementHandlerInterceptor =new StatementHandlerInterceptor();
        statementHandlerInterceptor.setDialectString(DialectMapper.getDialect(dialect));
        //select for page and findBy
        config.addInterceptor(statementHandlerInterceptor);
        //Encrypt
        config.addInterceptor(new FieldEncryptInterceptor());
        //Decrypt
        config.addInterceptor(new FieldDecryptInterceptor());
        //data AutoFill , insert and update
        config.addInterceptor(new FieldAutoFillSpringInterceptor());
        //Trace SQL and Execute Cost Time
        config.addInterceptor(new TraceSqlIntercept());
        //set Default Statement Timeout
        if(config.getDefaultStatementTimeout() == null || config.getDefaultStatementTimeout() == 0) {
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
