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

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.dromara.mybatis.jpa.handler.FieldAutoFillHandler;
import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class FieldAutoFillSpringInterceptor  extends FieldAutoFillInterceptor {
    protected static Logger  logger = LoggerFactory.getLogger(FieldAutoFillSpringInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(fieldAutoFillHandler == null) {
            try {
                fieldAutoFillHandler = MybatisJpaContext.getBean(FieldAutoFillHandler.class);
                logger.debug("get bean by fieldAutoFillHandler class");
            }catch(Exception e){
                logger.error("get bean by fieldAutoFillHandler Exception",e);
            }
        }
        
        this.autoFill(invocation);
        
        return invocation.proceed();
    }

}
