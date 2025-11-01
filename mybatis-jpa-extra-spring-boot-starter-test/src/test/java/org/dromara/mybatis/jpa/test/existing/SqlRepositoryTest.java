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
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.repository.IJpaSqlRepository;
import org.dromara.mybatis.jpa.test.config.DataSourceConfig;
import org.dromara.mybatis.jpa.test.config.DatabaseInitializer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: shimh
 * @time: 2025/9/24 9:51
 */
@SpringBootTest
@ActiveProfiles("test")
@Import({DataSourceConfig.class,})
public class SqlRepositoryTest {
    static final Logger _logger = LoggerFactory.getLogger(SqlRepositoryTest.class);

    @Autowired
    private IJpaSqlRepository sqlRepository;
    
    @Autowired
    DatabaseInitializer databaseInitializer;

    @Test
    public void testSelectList() throws Exception {
        databaseInitializer.run(null);
        DataSourceSwitch.change("test1");
        // 
        
        String sql="INSERT INTO test_user (id,name, email, data_source) VALUES (#{id},#{name}, #{email}, #{dataSource})";
 
        for (int i = 1; i < 30; i++) {
            final int index = i;
                String dataSourceKey = "test" + ((index % 2) + 1);
                Map<String, Object> user = new HashMap<>();
                user.put("id", Long.valueOf(index));
                user.put("name", "User" + index);
                user.put("email", (index % 2) ==0 ? "user" + index + "@test.com":null);
                user.put("dataSource", dataSourceKey);
                
                sqlRepository.insert(sql, user);
        }
        Map<String, Object> p = new HashMap<>();
        p.put("name", "User1");
        String selectSql = "select * FROM test_user where name like '%${name}%'";
        
        // 验证数据
        List<Map<String,Object>> test1Users = sqlRepository.selectList(selectSql,p);
        _logger.debug("test1Users size  {}",test1Users.size());
        _logger.debug("test1Users {}",test1Users);

    }
    
    @Test
    public void testJpaPageResults() throws Exception {
        databaseInitializer.run(null);
        DataSourceSwitch.change("test1");
        // 
        
        String sql="INSERT INTO test_user (id,name, email, data_source) VALUES (#{id},#{name}, #{email}, #{dataSource})";
 
        for (int i = 1; i < 30; i++) {
            final int index = i;
                String dataSourceKey = "test" + ((index % 2) + 1);
                Map<String, Object> user = new HashMap<>();
                user.put("id", Long.valueOf(index));
                user.put("name", "User" + index);
                user.put("email", (index % 2) ==0 ? "user" + index + "@test.com":null);
                user.put("dataSource", dataSourceKey);
                
                sqlRepository.insert(sql, user);
        }
        
        
        Map<String, Object> p = new HashMap<>();
        p.put("name", "User1");
        JpaPage page = new JpaPage(2,5);
        String selectSql = "select id,name, email, data_source  FROM test_user where name like '%${name}%'";
        JpaPageResults<Map<String, Object>> pageResults = sqlRepository.fetch(selectSql, page, p);
        _logger.debug("pageResults {}",pageResults);
    }
    
    
}
