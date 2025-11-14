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
 

package org.dromara.mybatis.jpa.underscore.test;

import java.util.ArrayList;
import java.util.List;

import org.dromara.mybatis.jpa.query.Query;
import org.dromara.mybatis.jpa.test.entity.Scores;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurdTestRunner  extends BaseUnderscoreTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(CurdTestRunner.class);
    
    @Test
    void insert(){
        _logger.info("insert...");
        Scores score=new Scores();
        score.setStdNo("10024");
        score.setStdName("司马昭");
        score.setCourseId("1");
        score.setCourseName("政治");
        score.setGrade(70);
        service.insert(score);
        
        _logger.info("insert id {}" , score.getId());
    }
    
    @Test
    void merge(){
        _logger.info("merge...");
        Scores score=new Scores();
        score.setStdNo("10024");
        score.setStdName("司马昭");
        score.setCourseId("1");
        score.setCourseName("政治");
        score.setGrade(85);
        service.merge(score);
        
        _logger.info("insert id {}" , score.getId());
        
    }

    @Test
    void get(){
        _logger.info("get...");
        Scores student=service.get("1071439277251887104");
         _logger.info("Students {}",student);
    }
    
    @Test
    void update(){
        _logger.info("get...");
        Scores score= service.get("1071432244066779136");
         _logger.info("Students {}",score);
         
         _logger.info("update...");
         score.setGrade(10);
         service.update(score);
         _logger.info("updateed.");
         
         score.setGrade(89);
         service.update(score);
         _logger.info("updateed2.");
    }
    
    @Test
    void updateByQuery(){
        _logger.info("updateByQuery...");
        service.update("id = '5'",Query.builder().eq("id", "2"));
    }
    
    @Test
    void remove(){
        _logger.info("remove...");
        service.delete("b1e2-92fb23b5e512");
        service.delete("921d3377-937a-4578-b1e2-92fb23b5e512");
    }
    
    @Test
    void batchDelete(){
        _logger.info("batchDelete...");
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
        idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
        service.deleteBatch(idList);
    }
    
    @Test
    void deleteByQuery(){
        _logger.info("deleteByQuery...");
        service.delete(Query.builder().eq("id", "2"));
    }
    
}