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
 

/**
 * 
 */
package org.dromara.mybatis.jpa.metadata;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.constants.ConstCaseType;
import org.dromara.mybatis.jpa.crypto.EncryptFactory;
import org.dromara.mybatis.jpa.entity.JpaPageSqlCache;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author Crystal.Sea
 *
 */
public class MapperMetadata{
    private static final Logger logger     =     LoggerFactory.getLogger(MapperMetadata.class);
    
    /**
     * 定义全局缓存
     */
    public static final Cache<String, JpaPageSqlCache> PAGE_BOUNDSQL_CACHE = 
                            Caffeine.newBuilder()
                                .expireAfterWrite(300, TimeUnit.SECONDS)
                                .build();
    
    static ConcurrentMap<String, String>sqlsMap                 =     new ConcurrentHashMap<>();

    static IdentifierGeneratorFactory identifierGeneratorFactory= new IdentifierGeneratorFactory();
    
    static EncryptFactory encryptFactory;
    
       /**
     * 表名和字段名
     */
    static int         tableColumnCase                          = ConstCaseType.LOWERCASE;
    
    static boolean     tableColumnEscape                        = false;
    
    static boolean     mapUnderscoreToCamelCase                 = false;
    
    static String      tableColumnEscapeChar                    =  "`";
    
    static String      partitionColumn                          =  "inst_id";
    

    /**
     * Case Converter
     * @param name
     * @return case
     */
    public static String tableOrColumnCaseConverter(String name) {
        if(MapperMetadata.tableColumnCase  == ConstCaseType.LOWERCASE) {
            name = name.toLowerCase();
        }else if(MapperMetadata.tableColumnCase  == ConstCaseType.UPPERCASE) {
            name = name.toUpperCase();
        }
        return name;
    }

    /**
     * Escape Converter
     * @param name
     * @return Escape name
     */
    public static String tableOrColumnEscape(String name) {
        return MapperMetadata.tableColumnEscape ? MapperMetadata.tableColumnEscapeChar + name + MapperMetadata.tableColumnEscapeChar : name;
    }
    
    public static ConcurrentMap<String, String> getSqlsMap() {
        return sqlsMap;
    }

    public static void setSqlsMap(ConcurrentMap<String, String> sqlsMap) {
        MapperMetadata.sqlsMap = sqlsMap;
    }

    public static IdentifierGeneratorFactory getIdentifierGeneratorFactory() {
        return identifierGeneratorFactory;
    }

    public static void setIdentifierGeneratorFactory(IdentifierGeneratorFactory identifierGeneratorFactory) {
        MapperMetadata.identifierGeneratorFactory = identifierGeneratorFactory;
        logger.debug("Identifier Generator Factory {}",identifierGeneratorFactory);
    }

    public static EncryptFactory getEncryptFactory() {
        return encryptFactory;
    }

    public static void setEncryptFactory(EncryptFactory encryptFactory) {
        MapperMetadata.encryptFactory = encryptFactory;
    }
    

    public static boolean isMapUnderscoreToCamelCase() {
		return mapUnderscoreToCamelCase;
	}

	public static void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
		MapperMetadata.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
	}

	public static int getTableColumnCase() {
        return tableColumnCase;
    }

    public static void setTableColumnCase(int tableColumnCase) {
        MapperMetadata.tableColumnCase = tableColumnCase;
    }

    public static boolean isTableColumnEscape() {
        return tableColumnEscape;
    }

    public static void setTableColumnEscape(boolean tableColumnEscape) {
        MapperMetadata.tableColumnEscape = tableColumnEscape;
    }

    public static String getTableColumnEscapeChar() {
        return tableColumnEscapeChar;
    }

    public static void setTableColumnEscapeChar(String tableColumnEscapeChar) {
        MapperMetadata.tableColumnEscapeChar = tableColumnEscapeChar;
    }

    public static String getPartitionColumn() {
        return partitionColumn;
    }

    public static void setPartitionColumn(String partitionColumn) {
        MapperMetadata.partitionColumn = partitionColumn;
    }
    
}
