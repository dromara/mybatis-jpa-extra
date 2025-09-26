package org.dromara.mybatis.jpa.test.existing;

import org.dromara.mybatis.jpa.SqlRepository;
import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.test.config.DataSourceConfig;
import org.dromara.mybatis.jpa.test.config.DatabaseInitializer;
import org.dromara.mybatis.jpa.test.entity.TestUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @description:
 * @author: shimh
 * @time: 2025/9/24 9:51
 */

@SpringBootTest
@Import(DataSourceConfig.class)
public class SqlRepositoryTest {
    static final Logger _logger = LoggerFactory.getLogger(SqlRepositoryTest.class);

    @Autowired
    private SqlRepository sqlRepository;
    
    @Autowired
    DatabaseInitializer databaseInitializer;

    @Test
    public void testSqlRepository() throws Exception {
    	DataSourceConfig config = new DataSourceConfig();
    	config.dynamicRoutingDataSource();
    	databaseInitializer.run(null);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        DataSourceSwitch.change("test1");
        // 
        
        String sql="INSERT INTO test_user (id,name, email, data_source) VALUES (#{id},#{name}, #{email}, #{dataSource})";
        // 并发访问不同数据源
        CompletableFuture<Void>[] futures = new CompletableFuture[30];

        for (int i = 0; i < 30; i++) {
            final int index = i;
            futures[i] = CompletableFuture.runAsync(() -> {
                String dataSourceKey = "test" + ((index % 2) + 1);
                TestUser user = new TestUser("User" + index, "user" + index + "@test.com", dataSourceKey);
                user.setId(Long.valueOf(index));
                sqlRepository.insert(sql, user);
            }, executor);
        }

        // 等待所有任务完成
        CompletableFuture.allOf(futures).join();

        // 验证数据
        List<Map<String,Object>> test1Users = sqlRepository.selectList("select * FROM test_user ");
        _logger.debug("test1Users {}",test1Users);
        assertTrue(test1Users.size() >= 15);

        executor.shutdown();
    }
    
    
}
