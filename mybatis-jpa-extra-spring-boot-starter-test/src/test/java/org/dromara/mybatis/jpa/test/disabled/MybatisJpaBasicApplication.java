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


package org.dromara.mybatis.jpa.test.disabled;

import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MybatisJpaBasicApplication{
    static final Logger _logger = LoggerFactory.getLogger(MybatisJpaBasicApplication.class);

    @Autowired
    org.apache.ibatis.session.SqlSessionFactory SqlSessionFactory;

    @Autowired
    ApplicationContext applicationContext;

    @BeforeEach
    public void before() {
        _logger.info("---------------- before");
        MybatisJpaContext.init(applicationContext);

    }

}
