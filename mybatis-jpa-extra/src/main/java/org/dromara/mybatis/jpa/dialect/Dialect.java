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
 

package org.dromara.mybatis.jpa.dialect;


import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.dromara.mybatis.jpa.entity.JpaPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DB Access Dialect,inspiration from hibernate
 * 
 * @author Crystal.Sea
 * Create 2017-7-24
 *
 */
public abstract class Dialect {

	private static final Logger logger 			= 	LoggerFactory.getLogger(Dialect.class);

	public static final String DEFAULT_BATCH_SIZE	= 	"20";
	public static final String NO_BATCH 			= 	"0";
	public static final String DEFAULT_DIALECT 		= 	"mysql";

	protected static HashMap<String,String> dialectMap;
	
	static {
		dialectMap=new HashMap<>();
		dialectMap.put("db2", 			"org.dromara.mybatis.jpa.dialect.DB2Dialect");
		dialectMap.put("derby", 		"org.dromara.mybatis.jpa.dialect.DerbyDialect");
		dialectMap.put(DEFAULT_DIALECT, "org.dromara.mybatis.jpa.dialect.MySQLDialect");
		dialectMap.put("oracle", 		"org.dromara.mybatis.jpa.dialect.OracleDialect");
		dialectMap.put("postgresql", 	"org.dromara.mybatis.jpa.dialect.PostgreSQLDialect");
		dialectMap.put("highgo", 		"org.dromara.mybatis.jpa.dialect.HighgoDialect");
		dialectMap.put("sqlserver", 	"org.dromara.mybatis.jpa.dialect.SQLServerDialect");
		
		logger.trace("Dialect Mapper : \n{}" ,dialectMap);
	}

	protected Dialect() {

	}

	@Override
    public String toString() {
		return getClass().getName();
	}

	/**
	 * Given a limit and an offset, apply the limit clause to the query.
	 *
	 * @param query The query to which to apply the limit.
	 * @param offset The offset of the limit
	 * @param limit The limit of the limit ;)
	 * @return The modified query statement with the limit applied.
	 */

	public String getLimitString(String query, JpaPage page) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	
	
	public String getPreparedStatementLimitString(String query, JpaPage page) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPage pagination) {
		throw new UnsupportedOperationException( "Paged queries not supported by " + getClass().getName());
	}
	
	
	public boolean supportsLimit() {
		return false;
	}

	/**
	 * @return the dialectMap
	 */
	public static Map<String, String> getDialectMap() {
		return dialectMap;
	}
	
	public static String getDialect(String dialect) {
		String dialectString =dialectMap.get(dialect);
		if(dialectString == null) {
			dialectString =dialectMap.get(DEFAULT_DIALECT);
		}
		return dialectString;
	}
	
	
}
