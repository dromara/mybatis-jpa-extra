/*
 * Copyright [2026] [MaxKey of copyright http://www.maxkey.top]
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
 

package org.dromara.mybatis.jpa.provider;

import java.io.Serializable;
import java.util.Map;

import org.dromara.mybatis.jpa.entity.JpaEntity;
import org.dromara.mybatis.jpa.provider.impl.SoftDeleteProvider;
import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SoftDeleteMapperProvider
 * @author Crystal.Sea
 *
 */
public class SoftDeleteMapperProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(SoftDeleteMapperProvider.class);
    
    public SoftDeleteMapperProvider() {
        logger.trace("constructor init .");
    }
    
    public String softDeleteById(Map<String, Object>  parametersMap) { 
        return new SoftDeleteProvider<>().softDeleteById(parametersMap);
    }
    
    public String softDelete(Map<String, Object>  parametersMap) { 
        return new SoftDeleteProvider<>().softDelete(parametersMap);
    }
    
    public String softDeleteByQuery(Class<?> entityClass,Query query) { 
        return new SoftDeleteProvider<>().softDeleteByQuery(entityClass,query);
    } 
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String softDeleteByLambdaQuery(Class<?> entityClass,LambdaQuery<T> lambdaQuery) { 
        return new SoftDeleteProvider().softDeleteByLambdaQuery(entityClass,lambdaQuery);
    } 
   
}
