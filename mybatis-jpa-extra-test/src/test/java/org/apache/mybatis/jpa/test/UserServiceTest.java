/*
 * Copyright [2021] [MaxKey of copyright http://www.maxkey.top]
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


package org.apache.mybatis.jpa.test;

import org.apache.mybatis.jpa.test.dao.service.UserService;
import org.apache.mybatis.jpa.test.entity.User;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceTest {
    private static final Logger _logger = LoggerFactory.getLogger(UserServiceTest.class);

    public static ApplicationContext context;
    public static UserService userService;

    @Test
    public void insert() throws Exception {
        _logger.info("insert...");
        User user = new User();
        //student.setId("10024");
        user.setFirstName("F");
        user.setLastName("L");
        userService.insert(user);

        Thread.sleep(1000);
        _logger.info("insert id " + user.getId());
        userService.remove(user.getId());
    }

    @Test
    public void get() throws Exception {
        _logger.info("get...");
        User user = userService.get("xxxxxx");
        System.out.println("User " + user);
        _logger.info("User " + user);
    }

    @Test
    public void remove() throws Exception {
        _logger.info("remove...");
        User user = new User();
        user.setId("xxxxxx");
        userService.remove(user.getId());

    }

    @Test
    public void batchDelete() throws Exception {
        _logger.info("batchDelete...");
        List<String> idList = new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
        idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
        userService.batchDelete(idList);
    }

    @Test
    public void queryPageResults() throws Exception {
        _logger.info("queryPageResults...");
        User user = new User();
        //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
        user.setFirstName("F");
        user.setPageSize(10);
        user.setPageNumber(2);
        List<User> list = userService.queryPageResults(user).getRows();
        for (User s : list) {
            _logger.info("User " + s);
        }
    }

    @Test
    public void findAll() throws Exception {
        _logger.info("findAll...");
        List<User> list = userService.findAll();
        for (User user : list) {
            _logger.info("User " + user);
        }
    }

    @Before
    public void initSpringContext() {
        if (context != null) return;
        _logger.info("init Spring Context...");
        SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf_ymdhms.format(new Date());

        try {
            UserServiceTest runner = new UserServiceTest();
            runner.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _logger.info("-- --Init Start at " + startTime + " , End at  " + sdf_ymdhms.format(new Date()));
    }

    //Initialization ApplicationContext for Project
    public void init() {

        _logger.info("Application dir " + System.getProperty("user.dir"));
        context = new ClassPathXmlApplicationContext(new String[]{"spring/applicationContext.xml"});

        WebContext.applicationContext = context;
        userService = (UserService) WebContext.getBean("userService");
    }
}
