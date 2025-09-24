package org.dromara.mybatis.jpa.test.existing;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.dromara.mybatis.jpa.test.dao.service.TestUserService;
import org.dromara.mybatis.jpa.test.entity.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/24 9:49
 */

@SpringBootTest
@ActiveProfiles("test")
public class DynamicDataSourceTest {
    @Autowired
    private TestUserService userService;

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Test
    public void testDataSourceSwitch() {
        // 测试数据源切换功能
        String originalDataSource = DataSourceSwitch.getCurrentDataSource();

        // 切换到test1数据源
        String switchedTo = DataSourceSwitch.change("test1");
        assertEquals("test1", switchedTo);
        assertEquals("test1", DataSourceSwitch.getCurrentDataSource());

        // 切换到test2数据源
        DataSourceSwitch.change("test2");
        assertEquals("test2", DataSourceSwitch.getCurrentDataSource());

        // 切换回默认数据源
        DataSourceSwitch.switchToDefault();
        assertNull(DataSourceSwitch.getCurrentDataSource());
    }

    @Test
    public void testDataSourceExists() {
        // 测试数据源是否存在
        assertTrue(DataSourceSwitch.exists("default"));
        assertTrue(DataSourceSwitch.exists("test1"));
        assertTrue(DataSourceSwitch.exists("test2"));
        assertFalse(DataSourceSwitch.exists("nonexistent"));
    }

    @Test
    public void testInsertToMultipleDataSources() {
        // 向不同数据源插入数据
        TestUser user1 = new TestUser("Alice", "alice@test.com", "default");
        TestUser user2 = new TestUser("Bob", "bob@test.com", "test1");
        TestUser user3 = new TestUser("Charlie", "charlie@test.com", "test2");

        userService.insertUserWithDataSource("default", user1);
        userService.insertUserWithDataSource("test1", user2);
        userService.insertUserWithDataSource("test2", user3);

        // 验证每个数据源中的数据
        List<TestUser> defaultUsers = userService.getUsersFromDataSource("default");
        List<TestUser> test1Users = userService.getUsersFromDataSource("test1");
        List<TestUser> test2Users = userService.getUsersFromDataSource("test2");

        assertTrue(defaultUsers.size() >= 1);
        assertTrue(test1Users.size() >= 1);
        assertTrue(test2Users.size() >= 1);

        // 验证数据源标识
        assertTrue(defaultUsers.stream().anyMatch(u -> "Alice".equals(u.getName())));
        assertTrue(test1Users.stream().anyMatch(u -> "Bob".equals(u.getName())));
        assertTrue(test2Users.stream().anyMatch(u -> "Charlie".equals(u.getName())));
    }

    @Test
    public void testDataSourceHealth() {
        // 测试数据源健康检查
        assertTrue(dynamicRoutingDataSource.checkDataSourceHealth("default"));
        assertTrue(dynamicRoutingDataSource.checkDataSourceHealth("test1"));
        assertTrue(dynamicRoutingDataSource.checkDataSourceHealth("test2"));
        assertFalse(dynamicRoutingDataSource.checkDataSourceHealth("nonexistent"));
    }

    @Test
    public void testThreadLocalIsolation() throws InterruptedException {
        // 测试ThreadLocal隔离性
        Thread thread1 = new Thread(() -> {
            DataSourceSwitch.change("test1");
            assertEquals("test1", DataSourceSwitch.getCurrentDataSource());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertEquals("test1", DataSourceSwitch.getCurrentDataSource());
        });

        Thread thread2 = new Thread(() -> {
            DataSourceSwitch.change("test2");
            assertEquals("test2", DataSourceSwitch.getCurrentDataSource());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            assertEquals("test2", DataSourceSwitch.getCurrentDataSource());
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // 主线程应该不受影响
        assertNull(DataSourceSwitch.getCurrentDataSource());
    }

    @Test
    public void testInvalidDataSourceKey() {
        // 测试无效的数据源key
        assertThrows(IllegalArgumentException.class, () -> {
            DataSourceSwitch.change("nonexistent");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DataSourceSwitch.change(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DataSourceSwitch.change("");
        });
    }

    @Test
    public void testDataSourceCount() {
        // 测试数据源数量
        assertEquals(3, DynamicRoutingDataSource.getDataSourceCount());
    }
}
