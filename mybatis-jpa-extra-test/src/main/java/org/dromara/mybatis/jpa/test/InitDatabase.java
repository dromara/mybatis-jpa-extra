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
 

package org.dromara.mybatis.jpa.test;

import java.io.File;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

public class InitDatabase {
    private static final Logger _logger = LoggerFactory.getLogger(InitDatabase.class);
    
    public void init(DataSource dataSource){
        try {
            Resource schema = new ClassPathResource("/sql/schema-h2.sql");
            Resource data = new ClassPathResource("/sql/data-h2.sql");
            _logger.debug("schema path {}",schema.getURL());
            File f = new File("./db/test_h2.lock");
            if(!f.exists()){
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
