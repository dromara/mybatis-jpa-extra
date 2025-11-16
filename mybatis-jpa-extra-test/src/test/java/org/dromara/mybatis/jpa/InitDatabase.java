package org.dromara.mybatis.jpa;

import java.io.File;

import javax.sql.DataSource;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.dromara.mybatis.jpa.test.BaseTestRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitDatabase extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(InitDatabase.class);
    
    @Test
    void initDB(){
        try {
            Resource schema = new ClassPathResource("/sql/schema-h2.sql");
            Resource data = new ClassPathResource("/sql/data-h2.sql");
            _logger.debug("schema path {}",schema.toString());
            File f = new File("./db/test_h2.lock");
            if(!f.exists()){
                DataSource dataSource = MybatisJpaContext.getBean(DataSource.class);
                ScriptUtils.executeSqlScript(dataSource.getConnection(),schema);
                ScriptUtils.executeSqlScript(dataSource.getConnection(),data);
            }
            f.createNewFile();
            _logger.debug("db init success .");
        }catch(Exception e) {
            _logger.error("db init Exception !",e);
        }
    }

}
