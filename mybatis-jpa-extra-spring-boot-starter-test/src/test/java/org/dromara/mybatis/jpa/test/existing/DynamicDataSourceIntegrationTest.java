/*
 * Copyright [2025] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.dromara.mybatis.jpa.test.existing;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.test.config.DataSourceConfig;
import org.dromara.mybatis.jpa.test.dao.service.TestUserService;
import org.dromara.mybatis.jpa.test.entity.TestUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

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
@Import(DataSourceConfig.class)
public class DynamicDataSourceIntegrationTest {
    static final Logger _logger = LoggerFactory.getLogger(DynamicDataSourceIntegrationTest.class);

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    // 移除事务注解，使用编程式事务
    @Test
    public void testTransactionIsolationFixed() {
        // 清理测试数据
        clearTestData();

        // 在test1中插入数据并统计
        int test1CountAfterInsert = executeInNewTransaction("test1", () -> {
            TestUser user1 = new TestUser("TransactionUser1", "tx1@test.com", "test1");
            testUserService.insertUser(user1);
            return testUserService.countUsers();
        });

        // 在test2中插入数据并统计
        int test2CountAfterInsert = executeInNewTransaction("test2", () -> {
            TestUser user2 = new TestUser("TransactionUser2", "tx2@test.com", "test2");
            testUserService.insertUser(user2);
            return testUserService.countUsers();
        });

        // 再次检查test1的数据，应该没有变化
        int test1FinalCount = executeInNewTransaction("test1", () -> {
            return testUserService.countUsers();
        });

        _logger.debug("test1CountAfterInsert: {}, test2CountAfterInsert: {}, test1FinalCount: {}",
                test1CountAfterInsert, test2CountAfterInsert, test1FinalCount);

        // 验证事务隔离性
        assertEquals(test1CountAfterInsert, test1FinalCount, "test1数据源的记录数应该不受test2操作影响");
        assertTrue(test1CountAfterInsert > 0, "test1应该有数据");
        assertTrue(test2CountAfterInsert > 0, "test2应该有数据");
    }

    // 清理测试数据的方法
    private void clearTestData() {
        String[] dataSources = {"default", "test1", "test2"};
        for (String ds : dataSources) {
            executeInNewTransaction(ds, () -> {
                jdbcTemplate.execute("DELETE FROM test_user WHERE id > 0");
            });
        }
    }

    // 工具方法：在指定数据源上执行操作并返回结果
    private <T> T executeInNewTransaction(String dataSourceKey, java.util.function.Supplier<T> operation) {
        DataSourceSwitch.change(dataSourceKey);

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        try {
            return template.execute(status -> operation.get());
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }

    // 工具方法：在指定数据源上执行操作（无返回值）
    private void executeInNewTransaction(String dataSourceKey, Runnable operation) {
        DataSourceSwitch.change(dataSourceKey);

        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        try {
            template.execute(status -> {
                operation.run();
                return null;
            });
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }
}
