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

public class DB2Dialect extends AbstractDialect {

	public DB2Dialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPage page) {
		if ( page.getPageSize() == 0 ) {
			return sql + " fetch first " + page.getStartRow() + " rows only";
		}
		
		StringBuilder pagingSelectSql = new StringBuilder( sql.length() + 200 )
				.append(
						"select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
				)
				.append( sql )  //nest the main query in an outer select
				.append(" fetch first ")
				.append(page.getPageSize())
				.append(" rows only ) as inner2_ ) as inner1_ where rownumber_ > " )
				.append(page.getStartRow())
				.append(" order by rownumber_");
		
		return pagingSelectSql.toString();
	}
	
	/**
	 * LIMIT #{pageResults}  OFFSET #{startRow}
	 */
	@Override
	public String getPreparedStatementLimitString(String sql,  JpaPage page) {
		if(page.getPageSize()>0&&page.getStartRow()>0){
			return sql +  " limit ? , ?";
		}else if(page.getPageSize()>0){
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
		return "DB2Dialect [" + DB2Dialect.class + "]";
	}
	
	
}
