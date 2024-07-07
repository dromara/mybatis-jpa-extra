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

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.dromara.mybatis.jpa.meta.FieldColumnMapper;
import org.dromara.mybatis.jpa.meta.FieldMetadata;
import org.dromara.mybatis.jpa.meta.MapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({ @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class FieldEncryptInterceptor  implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(FieldEncryptInterceptor.class);
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
        //实体对象
        Object entity = invocation.getArgs()[1];
        FieldMetadata.buildColumnList(entity.getClass());
        List <FieldColumnMapper> listFieldColumn = FieldMetadata.getFieldsMap(entity.getClass());

        for (FieldColumnMapper encryptField : listFieldColumn) {
        	if(encryptField.isEncrypted()) {
        		logger.debug("FieldName {} is need {} Encrypted ",encryptField.getFieldName(),encryptField.getEncryptedAnnotation().algorithm());
	            encryptField.getField().setAccessible(true);
	            String plainValue = (String) encryptField.getField().get(entity);
	            String cipherValue = decrypt(plainValue,encryptField.getEncryptedAnnotation().algorithm());
	            encryptField.getField().set(entity, cipherValue);
        	}
        }
        return invocation.proceed();
	}
    
	private String decrypt(String plainValue,String algorithm) throws SQLException {
    	return MapperMetadata.getEncryptFactory().getEncryptor(algorithm.toLowerCase()).encrypt(plainValue);  
    }
	 
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
