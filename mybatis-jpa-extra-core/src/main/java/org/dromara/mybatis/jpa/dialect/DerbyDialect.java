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
import java.sql.SQLException;

import org.dromara.mybatis.jpa.entity.JpaPagination;

public class DerbyDialect extends Dialect {

	public DerbyDialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPagination pagination) {
		StringBuilder pagingSelectSql = new StringBuilder(sql.length() + 50);

		pagingSelectSql.append( sql );
		
		if ( pagination.getStartRow() == 0 ) {
			pagingSelectSql.append( " fetch first " );
		}
		else {
			pagingSelectSql.append( " offset " ).append( pagination.getStartRow() ).append( " rows fetch next " );
		}

		pagingSelectSql.append( pagination.getPageSize() ).append( " rows only" );
		
		
		return pagingSelectSql.toString();
	}
	
	@Override
	public String getPreparedStatementLimitString(String sql,  JpaPagination pagination) {
		//LIMIT #{pageResults}  OFFSET #{startRow}
		if(pagination.getPageSize()>0&&pagination.getStartRow()>0){
			return sql +  " limit ? , ?";
		}else if(pagination.getPageSize()>0){
			return sql +  " limit  ? ";
		}else{
			return sql +  " limit ?";
		}
	}
	
	
	public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPagination pagination) {
		
		try {
			if(pagination.getPageSize()>0&&pagination.getStartRow()>0){
				preparedStatement.setInt(++parameterSize, pagination.getPageSize());
				preparedStatement.setInt(++parameterSize, pagination.getPageSize());
			}else if(pagination.getPageSize()>0){
				preparedStatement.setInt(++parameterSize, pagination.getPageSize());
			}else{
				preparedStatement.setInt(++parameterSize, 1000);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DerbyDialect [" + DerbyDialect.class + "]";
	}
}
