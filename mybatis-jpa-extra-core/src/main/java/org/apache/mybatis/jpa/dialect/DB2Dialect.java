package org.apache.mybatis.jpa.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mybatis.jpa.persistence.JpaPagination;



public class DB2Dialect extends Dialect {

	public DB2Dialect() {
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
		StringBuilder pagingSelectSql = new StringBuilder( sql.length() + 200 )
				.append(
						"select * from ( select inner2_.*, rownumber() over(order by order of inner2_) as rownumber_ from ( "
				)
				.append( sql )  //nest the main query in an outer select
				.append(" fetch first ")
				.append(pagination.getPageSize())
				.append(" rows only ) as inner2_ ) as inner1_ where rownumber_ > " )
				.append(pagination.getStartRow())
				.append(" order by rownumber_");
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
		return "DB2Dialect [" + DB2Dialect.class + "]";
	}
	
	
}
