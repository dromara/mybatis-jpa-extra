package org.dromara.mybatis.jpa.test.config;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/24 9:28
 */

@Component
public class DatabaseInitializer implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        initializeDatabase("default");
        initializeDatabase("test1");
        initializeDatabase("test2");
    }

    private void initializeDatabase(String dataSourceKey) {
        DataSourceSwitch.change(dataSourceKey);
        try {
            // 创建测试表
            String createTableSql = """
                CREATE TABLE IF NOT EXISTS test_user (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100),
                    data_source VARCHAR(50)
                )
                """;
            jdbcTemplate.execute(createTableSql);

            // 插入测试数据
            String insertSql = "INSERT INTO test_user (name, email, data_source) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, "User from " + dataSourceKey,
                    "user@" + dataSourceKey + ".com", dataSourceKey);
            System.out.println("init dataSourceKey " +dataSourceKey);
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }
}
