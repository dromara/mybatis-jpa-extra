package org.apache.mybatis.jpa.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mybatis.jpa.persistence.JpaPagination;


public class MySQLDialect extends Dialect {

	public MySQLDialect() {
		super();

	}

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}
	
	@Override
	public String getLimitString(String sql,  JpaPagination pagination) {
		//LIMIT #{pageResults}  OFFSET #{startRow}
		pagination.calculate();
		if(pagination.getPageResults()>0&&pagination.getStartRow()>0){
			return sql +  " LIMIT "+pagination.getStartRow()+" , " +pagination.getPageResults();
		}else if(pagination.getPageResults()>0){
			return sql +  " LIMIT  "+pagination.getPageResults();
		}else{
			return sql +  " LIMIT "+pagination.getPageResults();
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
		return "MySQLDialect [" + MySQLDialect.class + "]";
	}
}
