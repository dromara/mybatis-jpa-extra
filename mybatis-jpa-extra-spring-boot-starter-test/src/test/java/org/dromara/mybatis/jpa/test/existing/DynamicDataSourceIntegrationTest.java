package org.dromara.mybatis.jpa.test.existing;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.test.dao.service.TestUserService;
import org.dromara.mybatis.jpa.test.entity.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/24 9:51
 */

@SpringBootTest
public class DynamicDataSourceIntegrationTest {
    @Autowired
    private TestUserService testUserService;

    @Test
    public void testConcurrentDataSourceAccess() {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 并发访问不同数据源
        CompletableFuture<Void>[] futures = new CompletableFuture[30];

        for (int i = 0; i < 30; i++) {
            final int index = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                String dataSourceKey = "test" + ((index % 2) + 1);
                TestUser user = new TestUser("User" + index, "user" + index + "@test.com", dataSourceKey);
                testUserService.insertUserWithDataSource(dataSourceKey, user);
            }, executor);
        }

        // 等待所有任务完成
        CompletableFuture.allOf(futures).join();

        // 验证数据
        List<TestUser> test1Users = testUserService.getUsersFromDataSource("test1");
        List<TestUser> test2Users = testUserService.getUsersFromDataSource("test2");

        assertTrue(test1Users.size() >= 15);
        assertTrue(test2Users.size() >= 15);

        executor.shutdown();
    }

    @Test
    @Transactional
    @Rollback
    public void testTransactionIsolation() {
        // 测试事务隔离性
        DataSourceSwitch.change("test1");

        TestUser user1 = new TestUser("TransactionUser1", "tx1@test.com", "test1");
        testUserService.insertUser(user1);

        int countBefore = testUserService.countUsers();

        DataSourceSwitch.change("test2");
        TestUser user2 = new TestUser("TransactionUser2", "tx2@test.com", "test2");
        testUserService.insertUser(user2);

        DataSourceSwitch.change("test1");
        int countAfter = testUserService.countUsers();

        // test1数据源中的记录数应该只增加1
        assertEquals(countBefore, countAfter);
    }
}
