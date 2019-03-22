package org.apache.mybatis.jpa.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mybatis.jpa.persistence.JpaPagination;


public class PostgreSQLDialect extends Dialect {

	public PostgreSQLDialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql,  JpaPagination pagination) {
		//LIMIT #{pageResults}  OFFSET #{startRow}
		if(pagination.getPageResults()>0&&pagination.getStartRow()>0){
			return sql +  " LIMIT " + pagination.getPageResults()+" ,  "+pagination.getStartRow() ;
		}else if(pagination.getPageResults()>0){
			return sql +  " LIMIT " + pagination.getPageResults();
		}else{
			return sql +  " LIMIT 1000";
		}
	}
	
	@Override
	public String getPreparedStatementLimitString(String sql,  JpaPagination pagination) {
		//LIMIT #{pageResults}  OFFSET #{startRow}
		if(pagination.getPageResults()>0&&pagination.getStartRow()>0){
			return sql +  " LIMIT ? , ?";
		}else if(pagination.getPageResults()>0){
			return sql +  " LIMIT  ? ";
		}else{
			return sql +  " LIMIT ?";
		}
	}
	
	
	public void setLimitParamters(PreparedStatement preparedStatement,int parameterSize,JpaPagination pagination) {
		
		try {
			if(pagination.getPageResults()>0&&pagination.getStartRow()>0){
				preparedStatement.setInt(++parameterSize, pagination.getPageResults());
				preparedStatement.setInt(++parameterSize, pagination.getPageResults());
			}else if(pagination.getPageResults()>0){
				preparedStatement.setInt(++parameterSize, pagination.getPageResults());
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
		return "PostgreSQLDialect [" + PostgreSQLDialect.class + "]";
	}
}
