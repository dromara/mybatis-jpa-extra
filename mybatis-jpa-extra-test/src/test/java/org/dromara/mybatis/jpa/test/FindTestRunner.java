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
 

package org.dromara.mybatis.jpa.test;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindTestRunner extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(FindTestRunner.class);
    
    @Test
    void findAll(){
        _logger.info("findAll...");
        service.findAll();
    }
    
    @Test
    void findById(){
        _logger.info("findById...");
        service.findById("1004539842878504988");
    }
    
    @Test
    void findByIds(){
        _logger.info("findByIds...");
        List<String> idList=new ArrayList<String>();
        idList.add("1004539842878504981");
        idList.add("1004539842878504982");
        idList.add("1004539842878504983");
        idList.add("1004539842878504984");
        idList.add("1004539842878504985");
        service.findByIds(idList);
    }
    
    @Test
    void find(){
        _logger.info("find by filter  StdNo = '10024' or StdNo = '10004'");

        service.find(" StdNo = ? or StdNo = ?  or StdNo ='11111'",
                new Object[]{"10024","10004"},
                new int[]{Types.VARCHAR,Types.VARCHAR}
            );
    }    

}