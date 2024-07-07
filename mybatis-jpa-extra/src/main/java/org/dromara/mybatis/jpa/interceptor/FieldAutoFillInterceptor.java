/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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
 
package org.dromara.mybatis.jpa.interceptor;

import java.util.Objects;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.dromara.mybatis.jpa.handler.FieldAutoFillHandler;
import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class FieldAutoFillInterceptor  implements Interceptor {
	protected static Logger  logger = LoggerFactory.getLogger(FieldAutoFillInterceptor.class);
	
	boolean isAutoFill;
	FieldAutoFillHandler fieldAutoFillHandler;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(!isAutoFill) {
			fieldAutoFillHandler = MybatisJpaContext.getBean(FieldAutoFillHandler.class);
			logger.debug("get bean by fieldAutoFillHandler class");
		}
		if(fieldAutoFillHandler != null) {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Configuration configuration = mappedStatement.getConfiguration();
	        Object parameter = invocation.getArgs()[1];
	        MetaObject metaObject = configuration.newMetaObject(parameter);
	        if (Objects.equals(SqlCommandType.INSERT, mappedStatement.getSqlCommandType())) {
	        	fieldAutoFillHandler.insertFill(metaObject);
	        }else if (Objects.equals(SqlCommandType.UPDATE, mappedStatement.getSqlCommandType())) {
	        	fieldAutoFillHandler.updateFill(metaObject);
	        }
		}
		return invocation.proceed();
	}

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    	//super.setProperties(properties);
    }
}
