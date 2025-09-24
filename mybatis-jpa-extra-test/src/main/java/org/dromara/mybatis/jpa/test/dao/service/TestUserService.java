package org.dromara.mybatis.jpa.test.dao.service;

import org.dromara.mybatis.jpa.IJpaService;
import org.dromara.mybatis.jpa.test.entity.TestUser;

import java.util.List;

public interface TestUserService extends IJpaService<TestUser> {


    void insertUser(TestUser user);

    List<TestUser> getAllUsers();

    int countUsers();

    void insertUserWithDataSource(String dataSourceKey, TestUser user);

    List<TestUser> getUsersFromDataSource(String dataSourceKey);
}
