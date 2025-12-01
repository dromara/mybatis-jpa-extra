package org.dromara.mybatis.jpa.test.dao.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.test.entity.TestUser;

import java.util.List;

/**
 * @author 24096
 */

@Mapper
public interface TestUserMapper extends IJpaMapper<TestUser> {

    @Select("SELECT COUNT(*) FROM test_user")
    int countUsers();

    @Select("SELECT * FROM test_user")
    List<TestUser> selectAllUsers();
}
