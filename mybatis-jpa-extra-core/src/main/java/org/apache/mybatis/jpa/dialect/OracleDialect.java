package org.apache.mybatis.jpa.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mybatis.jpa.persistence.JpaPagination;



public class OracleDialect extends Dialect {

	public OracleDialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPagination pagination) {
		if ( pagination.getPageSize() == 0 ) {
			return sql + " fetch first " + pagination.getStartRow() + " rows only";
		}
		StringBuilder pagingSelect = new StringBuilder( sql.length() + 200 )
				.append(
						"select * from (select inner_table_.*, rownum as rownumber_  from ( "
				)
				.append( sql )  //nest the main query in an outer select
				.append( ")  inner_table_ )  where rownumber_ > " )
				.append( pagination.getStartRow() )
				.append(" and rownumber_ <=")
				.append( pagination.getEndRow() )
				.append( " order by rownumber_" );
		return pagingSelect.toString();
	}
	
	@Override
	public String getPreparedStatementLimitString(String sql,  JpaPagination pagination) {
		//LIMIT #{pageResults}  OFFSET #{startRow}
		if(pagination.getPageSize()>0&&pagination.getStartRow()>0){
			return sql +  " LIMIT ? , ?";
		}else if(pagination.getPageSize()>0){
			return sql +  " LIMIT  ? ";
		}else{
			return sql +  " LIMIT ?";
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
		return "OracleDialect [" + OracleDialect.class + "]";
	}
}
