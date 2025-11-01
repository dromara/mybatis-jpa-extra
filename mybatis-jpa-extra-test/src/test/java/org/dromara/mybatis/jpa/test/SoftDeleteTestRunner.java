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

import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoftDeleteTestRunner  extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(SoftDeleteTestRunner.class);

    @Test
    void softDelete(){
        _logger.info("softDelete By Id ...");
        service.softDelete("2");
    }
    
    @Test
    void softDeleteByIds(){
        _logger.info("softDelete by Ids...");
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
        idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
        service.softDelete(idList);
    }
    
    @Test
    void softDeleteByQuery(){
        _logger.info("softDelete By Query...");
        service.softDelete(Query.builder().eq("id", "2"));
    }
    
    @Test
    void softDeleteByLambdaQuery(){
        _logger.info("softDelete By LambdaQuery ...");
        service.softDelete(new LambdaQuery<Students>().eq(Students::getId, "2"));
    }
}