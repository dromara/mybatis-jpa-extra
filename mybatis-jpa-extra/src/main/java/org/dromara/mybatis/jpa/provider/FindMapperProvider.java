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
import org.dromara.mybatis.jpa.provider.impl.FindProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FindMapperProvider
 * @author Crystal.Sea
 *
 */
public class FindMapperProvider <T extends JpaEntity,ID extends Serializable>{    
    static final Logger logger     =     LoggerFactory.getLogger(FindMapperProvider.class);
    
    public FindMapperProvider() {
        logger.trace("constructor init .");
    }

    public String find(Map<String, Object>  parametersMap) throws Exception {
        return new FindProvider<>().find(parametersMap);  
    }
    
    public String findByIds(Map<String, Object>  parametersMap) {  
        return new FindProvider<>().findByIds(parametersMap);  
    }
    
    public String findAll(Map<String, Object>  parametersMap) {  
        return new FindProvider<>().findAll(parametersMap);  
    }
 
}
