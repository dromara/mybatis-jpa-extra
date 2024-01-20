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

import org.dromara.mybatis.jpa.entity.JpaPage;

public class SQLServerDialect extends Dialect {

	public SQLServerDialect() {
		super();
	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPage page) {
		StringBuilder pagingSelectSql = new StringBuilder( "" );
		if(page.getPageSize()>0){
			
			pagingSelectSql.append("select top "+page.getPageSize()+" * from ( ");
			pagingSelectSql.append("select row_number() over() as rownumber,mybatis_query_temp_table.* from ( ");
			pagingSelectSql.append("mybatis_query_temp_table ) mybatis_query_temp_page ");
			if(page.getStartRow()>0){
				pagingSelectSql.append("where  rownumber > "+page.getStartRow());
			}
		}else{
			return sql;
		}
		return pagingSelectSql.toString();
	}
	
	/**
	 * LIMIT #{pageResults}  OFFSET #{startRow}
	 */
	@Override
	public String getPreparedStatementLimitString(String sql,  JpaPage pagination) {
		
		if(pagination.getPageSize()>0&&pagination.getStartRow()>0){
			return sql +  " limit ? , ?";
		}else if(pagination.getPageSize()>0){
			return sql +  " limit  ? ";
		}else{
			return sql +  " limit ?";
		}
	}
	
	@Override
	public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPage page) {
		
		try {
			if(page.getPageSize()>0&&page.getStartRow()>0){
				preparedStatement.setInt(++parameterSize, page.getPageSize());
				preparedStatement.setInt(++parameterSize, page.getPageSize());
			}else if(page.getPageSize()>0){
				preparedStatement.setInt(++parameterSize, page.getPageSize());
			}else{
				preparedStatement.setInt(++parameterSize, 1000);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "SQLServerDialect [" + SQLServerDialect.class + "]";
	}
}
