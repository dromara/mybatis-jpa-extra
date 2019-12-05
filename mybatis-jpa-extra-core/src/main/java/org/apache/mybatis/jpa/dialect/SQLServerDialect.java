
package org.apache.mybatis.jpa.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mybatis.jpa.persistence.JpaPagination;



public class SQLServerDialect extends Dialect {

	public SQLServerDialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPagination pagination) {
		StringBuilder pagingSelectSql = new StringBuilder( "" );
		if(pagination.getPageSize()>0){
			
			pagingSelectSql.append("SELECT TOP "+pagination.getPageSize()+" * FROM ( ");
			pagingSelectSql.append("SELECT ROW_NUMBER() OVER() AS ROWNUMBER,MYBATIS_QUERY_TEMP_TABLE.* FROM ( ");
			pagingSelectSql.append("MYBATIS_QUERY_TEMP_TABLE ) MYBATIS_QUERY_TEMP_PAGE ");
			if(pagination.getStartRow()>0){
				pagingSelectSql.append("WHERE  ROWNUMBER > "+pagination.getStartRow());
			}
		}else{
			return sql;
		}
		return pagingSelectSql.toString();
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
		return "SQLServerDialect [" + SQLServerDialect.class + "]";
	}
}
