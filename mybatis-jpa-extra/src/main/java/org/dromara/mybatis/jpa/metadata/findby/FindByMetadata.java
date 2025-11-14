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
 

package org.dromara.mybatis.jpa.metadata.findby;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindByMetadata {
    private static final Logger logger     =     LoggerFactory.getLogger(FindByMetadata.class);
    
    static ConcurrentMap<String, FindByMapper>findByMapperMap     =     new ConcurrentHashMap<>();

    public static ConcurrentMap<String, FindByMapper> getFindByMapperMap() {
        return findByMapperMap;
    }
    
    public static FindByMapper getFindByMapper(String mappedStatementId) {
        return findByMapperMap.get(mappedStatementId);
    }
    
    public static boolean containsKey(String mappedStatementId) {
        return findByMapperMap.containsKey(mappedStatementId);
    }
    
    public static FindByMapper put(String mappedStatementId,FindByMapper findByMapper) {
        logger.trace("mappedStatementId {}  ==> findByMapper {}" , mappedStatementId,findByMapper);
        return findByMapperMap.put(mappedStatementId,findByMapper);
    }
    
}
