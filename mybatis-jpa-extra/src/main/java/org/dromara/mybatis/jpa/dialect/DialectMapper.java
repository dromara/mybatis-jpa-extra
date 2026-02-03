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
 
package org.dromara.mybatis.jpa.dialect;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialectMapper {
    private static final Logger logger             =     LoggerFactory.getLogger(DialectMapper.class);

    public static final String DEFAULT_DIALECT     =     DialectName.MYSQL;

    protected static HashMap<String,String> dialectMap;
    
    static {
        dialectMap=new HashMap<>();
        dialectMap.put(DialectName.DB2,           DialectClass.DB2);
        dialectMap.put(DialectName.DERBY,         DialectClass.DERBY);
        dialectMap.put(DialectName.MYSQL,         DialectClass.MYSQL);
        dialectMap.put(DialectName.ORACLE,        DialectClass.ORACLE);
        dialectMap.put(DialectName.POSTGRESQL,    DialectClass.POSTGRESQL);
        dialectMap.put(DialectName.HIGHGO,        DialectClass.HIGHGO);
        dialectMap.put(DialectName.SQLSERVER,     DialectClass.SQLSERVER);
        dialectMap.put(DialectName.MARIADB,       DialectClass.MARIADB);
        
        logger.trace("Dialect Mapper : \n{}" ,dialectMap);
    }
    
    /**
     * @return the dialectMap
     */
    public static Map<String, String> getDialectMap() {
        return dialectMap;
    }
    
    public static String getDialect(String dialect) {
        String dialectString = dialectMap.get(dialect);
        if(dialectString == null) {
            dialectString = dialectMap.get(DEFAULT_DIALECT);
        }
        return dialectString;
    }
}
