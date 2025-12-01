/*
 * Copyright [2024] [MaxKey of copyright http://www.maxkey.top]
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

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.dromara.mybatis.jpa.test.dao.persistence.StudentsMapper;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.dao.service.impl.StudentsServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseTestRunner {
    private static final Logger _logger = LoggerFactory.getLogger(FetchPageResultsTestRunner.class);

    public static StudentsService service;
    
    @BeforeAll
    public static void initContext() throws IOException {
    	_logger.debug("init Context");
        String resource = "config/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        new InitDatabase().init(sqlSession.getConnection());
        StudentsMapper studentsMapper = sqlSession.getMapper(StudentsMapper.class);
        StudentsServiceImpl studentsServiceImpl= new StudentsServiceImpl();
        studentsServiceImpl.setMapper(studentsMapper);
        BaseTestRunner.service = studentsServiceImpl;
        _logger.debug("init Context success ");
    }

}
