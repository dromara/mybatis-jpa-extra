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
public abstract class AbstractDialect implements Dialect{
	private static final Logger logger 			= 	LoggerFactory.getLogger(AbstractDialect.class);

	protected AbstractDialect() {

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
	
}
