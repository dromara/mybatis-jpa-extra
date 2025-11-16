package org.dromara.mybatis.jpa.test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dromara.mybatis.jpa.datasource.DynamicRoutingDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class MybatisJpaDataSourceConfig {
    private static final Logger _logger = LoggerFactory.getLogger(MybatisJpaDataSourceConfig.class);
    
    public static final String DS_DEFUALT = "default";
    
    public static final String DS_TEST1 = "test1";
    
    public static final String DS_TEST2 = "test2";
    
    public static final String DS_TEST3 = "test3";
    
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource druidDataSource() {
        return  DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name="dataSource")
    public DynamicRoutingDataSource dynamicRoutingDataSource(DruidDataSource druidDataSource) {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

        // 创建多个测试数据源
        Map<Object, Object> targetDataSources = new HashMap<>();

        // 默认数据源 
        targetDataSources.put(DS_DEFUALT, druidDataSource);

        // 测试数据源1 - H2内存数据库2
        DataSource testDataSource1 = createH2DataSource(DS_TEST1);
        initializeDatabase(DS_TEST1,testDataSource1);
        targetDataSources.put(DS_TEST1, testDataSource1);

        // 测试数据源2 - H2内存数据库3
        DataSource testDataSource2 = createH2DataSource(DS_TEST2);
        initializeDatabase(DS_TEST2,testDataSource2);
        targetDataSources.put(DS_TEST2, testDataSource2);
        
        DataSource testDataSource3 = createH2DataSource(DS_TEST3);
        initializeDatabase(DS_TEST3,testDataSource3);
        targetDataSources.put(DS_TEST3, testDataSource3);

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(druidDataSource);

        return dynamicDataSource;
    }

    private DataSource createH2DataSource(String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:" + database + ";DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL;");
        config.setUsername("sa");
        config.setPassword("");
        config.setDriverClassName("org.h2.Driver");
        config.setPoolName("HikariPool-" + database);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        return new HikariDataSource(config);
    }
    
    private void initializeDatabase(String dsName,DataSource ds) {
        Resource dataDs = new ClassPathResource("/sql/data-ds-h2.sql");
        try {
            ScriptUtils.executeSqlScript(ds.getConnection(), dataDs);
            _logger.debug("initializeDatabase {}",dsName);
        } catch (ScriptException e) {
            _logger.error("ScriptException",e);
        } catch (SQLException e) {
            _logger.error("SQLException",e);
        }
    }
}
