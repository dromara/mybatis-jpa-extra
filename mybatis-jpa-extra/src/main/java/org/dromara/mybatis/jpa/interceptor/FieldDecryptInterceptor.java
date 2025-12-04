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
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.dromara.mybatis.jpa.metadata.ColumnMapper;
import org.dromara.mybatis.jpa.metadata.ColumnMetadata;
import org.dromara.mybatis.jpa.metadata.MapperMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class FieldDecryptInterceptor  implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(FieldDecryptInterceptor.class);
    
    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object dataSet = invocation.proceed();
        if (dataSet instanceof List) {
            return intercept((List<Object>) dataSet);
        }
        return dataSet;
    }

    private Object intercept(List<Object> dataSet) throws Throwable {
        for (Object entity : dataSet) {
            intercept(entity);
        }
        return dataSet;
    }

    private void intercept(Object entity) throws Throwable {
        List <ColumnMapper> listFieldColumn = ColumnMetadata.buildColumnMapper(entity.getClass());
        for (ColumnMapper encryptField : listFieldColumn) {
            if(encryptField.isEncrypted()) {
                logger.trace("FieldName {} is need Encrypted ",encryptField.getField());
                encryptField.getEntityField().setAccessible(true);
                String cipherValue = (String) encryptField.getEntityField().get(entity);
                String plainValue = decrypt(cipherValue,encryptField.getEncryptedAnnotation().algorithm());
                encryptField.getEntityField().set(entity, plainValue);
            }
        }
    }

    private String decrypt(String cipherValue,String algorithm) throws SQLException {
        return MapperMetadata.getEncryptFactory().getEncryptor(algorithm.toLowerCase()).decrypt(cipherValue);  
    }
    
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

}
