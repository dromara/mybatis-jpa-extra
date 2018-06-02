package org.apache.mybatis.jpa;

import org.apache.ibatis.mapping.BoundSql;

public class PageResultsSqlCache {
	String sql;
	BoundSql boundSql;
	
	
	public PageResultsSqlCache(String sql, BoundSql boundSql) {
		super();
		this.sql = sql;
		this.boundSql = boundSql;
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public BoundSql getBoundSql() {
		return boundSql;
	}
	public void setBoundSql(BoundSql boundSql) {
		this.boundSql = boundSql;
	}
	
}
