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
package org.dromara.mybatis.jpa.meta;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dromara.mybatis.jpa.crypto.EncryptFactory;
import org.dromara.mybatis.jpa.id.IdentifierGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Crystal.Sea
 *
 */
public class MapperMetadata{
	private static final Logger logger 	= 	LoggerFactory.getLogger(MapperMetadata.class);
	
	public  static  class SQL_TYPE{
		public static final String 	GET_SQL							= "_GET_SQL";
		public static final String 	FINDALL_SQL						= "_FINDALL_SQL";
		public static final String 	REMOVE_SQL						= "_REMOVE_SQL";
		public static final String 	BATCHDELETE_SQL					= "_BATCHDELETE_SQL";
		public static final String 	LOGICDELETE_SQL					= "_LOGICDELETE_SQL";
	}
	
	public static 	final		String ENTITY_CLASS				= "entityClass";
	
	public static 	final		String ENTITY					= "entity";
	
	public static 	final		String PAGE						= "page";
	
	public static 	final		String CONDITION				= "condition";
	
	public static 	final		String QUERY_FILTER				= "filter";
	public static 	final		String QUERY_ARGS				= "args";
	public static 	final		String QUERY_ARGTYPES			= "argTypes";
	
	public static 	final		String PARAMETER_PARTITION_KEY	= "partitionKey";
	public static 	final		String PARAMETER_ID_LIST		= "idList";
	public static 	final		String PARAMETER_ID				= "id";

	/**
	 * 表名和字段名
	 */
	public static int 		TABLE_COLUMN_CASE 					= CASE_TYPE.LOWERCASE;
	public static boolean   TABLE_COLUMN_ESCAPE                 = false;
	public static String    TABLE_COLUMN_ESCAPE_CHAR            =  "`";
	public static String    PARTITION_COLUMN           			=  "inst_id";
	
	public static class CASE_TYPE{
		public static final int 		NORMAL 							= 0;
		public static final int 		LOWERCASE 						= 1;
		public static final int 		UPPERCASE 						= 2;
	}
	
	static ConcurrentMap<String, String>sqlsMap 	= 	new ConcurrentHashMap<>();

	static IdentifierGeneratorFactory identifierGeneratorFactory = new IdentifierGeneratorFactory();
	
	static EncryptFactory encryptFactory;
	
	/**
	 * Case Converter
	 * @param name
	 * @return case
	 */
	public static String tableColumnCaseConverter(String name) {
		if(TABLE_COLUMN_CASE  == CASE_TYPE.LOWERCASE) {
			name = name.toLowerCase();
		}else if(TABLE_COLUMN_CASE  == CASE_TYPE.UPPERCASE) {
			name = name.toUpperCase();
		}
		return name;
	}

	/**
	 * Escape Converter
	 * @param name
	 * @return Escape name
	 */
	public static String tableColumnEscape(String name) {
		return TABLE_COLUMN_ESCAPE ? TABLE_COLUMN_ESCAPE_CHAR + name + TABLE_COLUMN_ESCAPE_CHAR : name;
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
	
}
