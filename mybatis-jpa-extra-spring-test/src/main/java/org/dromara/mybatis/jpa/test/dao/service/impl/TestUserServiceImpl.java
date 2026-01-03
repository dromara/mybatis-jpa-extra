package org.dromara.mybatis.jpa.test.dao.service.impl;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.test.dao.persistence.TestUserMapper;
import org.dromara.mybatis.jpa.test.dao.service.TestUserService;
import org.dromara.mybatis.jpa.test.entity.TestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/23 16:09
 */

@Service
public class TestUserServiceImpl extends JpaServiceImpl<TestUserMapper, TestUser,String> implements TestUserService {

    @Autowired
    private TestUserMapper userMapper;

    @Override
    public void insertUser(TestUser user) {
        userMapper.insert(user);
    }

    @Override
    public List<TestUser> getAllUsers() {
        return userMapper.selectAllUsers();
    }

    @Override
    public int countUsers() {
        return userMapper.countUsers();
    }

    @Override
    public void insertUserWithDataSource(String dataSourceKey, TestUser user) {
        // 切换数据源
        DataSourceSwitch.change(dataSourceKey);
        try {
            insertUser(user);
        } finally {
            // 恢复默认数据源
            DataSourceSwitch.switchToDefault();
        }
    }

    @Override
    public List<TestUser> getUsersFromDataSource(String dataSourceKey) {
        DataSourceSwitch.change(dataSourceKey);
        try {
            return getAllUsers();
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }
}
