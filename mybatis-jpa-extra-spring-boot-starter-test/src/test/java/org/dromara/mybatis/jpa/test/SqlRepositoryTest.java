package org.dromara.mybatis.jpa.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dromara.mybatis.jpa.datasource.DataSourceSwitch;
import org.dromara.mybatis.jpa.entity.JpaPage;
import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.repository.IJpaSqlRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.UseMainMethod;

@SpringBootTest(useMainMethod = UseMainMethod.ALWAYS)
public class SqlRepositoryTest {
    static final Logger _logger = LoggerFactory.getLogger(SqlRepositoryTest.class);

    @Autowired
    DataSource dataSource;
    
    @Autowired
    private IJpaSqlRepository sqlRepository;
    
    @Test
    public void testSelectList() throws Exception {
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
        DataSourceSwitch.change("test1");
        _logger.debug("CurrentDataSource {}",DataSourceSwitch.getCurrentDataSource());
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
        
        //change to test2
        changeDataSource();
    }
    
    public void changeDataSource() {
        DataSourceSwitch.change("test2");
        Map<String, Object> p = new HashMap<>();
        p.put("name", "User1");
        JpaPage page = new JpaPage(2,5);
        String selectSql = "select id,name, email, data_source  FROM test_user where name like '%${name}%'";
        JpaPageResults<Map<String, Object>> pageResults = sqlRepository.fetch(selectSql, page, p);
        _logger.debug("pageResults {}",pageResults);
    }
}
