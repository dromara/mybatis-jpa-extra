/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
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

/**
 * @author Crystal.Sea
 *
 */
public class MetadataConstants{

	public static final String LOG_FORMAT = "%-30s";
	
	public static final String LOG_FORMAT_COUNT = "%-3s";
	
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
	
	public static 	final		String IJPA_SQL_PARAMETER_SQL	= "ijpa_sql_parameter_sql";
	/**
	 * 查询的中间表别名
	 */
	public static final String SELECT_TMP_TABLE 				= " tmp_t";
	
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
}
