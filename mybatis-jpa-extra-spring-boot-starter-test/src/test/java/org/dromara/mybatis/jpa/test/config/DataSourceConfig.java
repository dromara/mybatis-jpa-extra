package org.dromara.mybatis.jpa.test.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: orangeBabu
 * @time: 2025/9/23 15:31
 */

@Configuration
public class DataSourceConfig {
    @Bean
    @Primary
    public DynamicRoutingDataSource dynamicRoutingDataSource() {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

        // 创建多个测试数据源
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 默认数据源 - H2内存数据库1
        DataSource defaultDataSource = createH2DataSource("default", "mem:testdb1");
        targetDataSources.put("default", defaultDataSource);

        // 测试数据源1 - H2内存数据库2
        DataSource testDataSource1 = createH2DataSource("test1", "mem:testdb2");
        targetDataSources.put("test1", testDataSource1);

        // 测试数据源2 - H2内存数据库3
        DataSource testDataSource2 = createH2DataSource("test2", "mem:testdb3");
        targetDataSources.put("test2", testDataSource2);

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    private DataSource createH2DataSource(String name, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:" + database + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        config.setPoolName("HikariPool-" + name);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        return new HikariDataSource(config);
    }
}
