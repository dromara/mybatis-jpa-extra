package org.dromara.mybatis.jpa.repository.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.mybatis.jpa.SqlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRepositoryImpl implements SqlRepository {
	private static final  Logger logger = LoggerFactory.getLogger(SqlRepositoryImpl.class);
	
	SqlSessionFactory sqlSessionFactory;
	
	public SqlRepositoryImpl(SqlSessionFactory sqlSessionFactory){
		this.sqlSessionFactory = sqlSessionFactory;
		logger.debug("init SqlSessionFactory");
	}
	
	public SqlSession openSession() {
		return sqlSessionFactory.openSession(true);
	}
	
	@Override
	public List<Map<String, Object>> selectList(String sql) {
		SqlSession sqlSession = this.openSession();
		List<Map<String, Object>> list = sqlSession.selectList(sql);
		sqlSession.close();
		return list;
	}

	@Override
	public List<Map<String, Object>> selectList(String sql, Object value) {
		SqlSession sqlSession = this.openSession();
		List<Map<String, Object>> list = sqlSession.selectList(sql, value);
		sqlSession.close();
		return list;
	}

	@Override
	public int insert(String sql) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.insert(sql);
		sqlSession.close();
		return effectCount;
	}

	@Override
	public int insert(String sql, Object value) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.insert(sql, value);
		sqlSession.close();
		return effectCount;
	}

	@Override
	public int update(String sql) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.update(sql);
		sqlSession.close();
		return effectCount;
	}

	@Override
	public int update(String sql, Object value) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.update(sql, value);
		sqlSession.close();
		return effectCount;
	}

	@Override
	public int delete(String sql) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.delete(sql);
		sqlSession.close();
		return effectCount;
	}

	@Override
	public int delete(String sql, Object value) {
		SqlSession sqlSession = this.openSession();
		int effectCount = sqlSession.delete(sql, value);
		sqlSession.close();
		return effectCount;
	}

}
