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
                    id BIGINT NOT NULL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(100),
                    data_source VARCHAR(50)
                )
                """;
            jdbcTemplate.execute(createTableSql);
            
            jdbcTemplate.execute("delete from test_user");
        } finally {
            DataSourceSwitch.switchToDefault();
        }
    }
}
