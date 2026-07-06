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

import org.dromara.mybatis.jpa.test.entity.StudentsInst;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurdInstTestRunner  extends BaseTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(CurdInstTestRunner.class);
    
    @Test
    void insert(){
        _logger.info("insert...");
        StudentsInst student=new StudentsInst();
        student.setStdNo("10024");
        student.setStdGender("M");
        student.setPassword("shimingxy");
        student.setStdName("司马昭");
        //student.setStdAge(20);
        student.setStdMajor("政治");
        student.setStdClass("4");
        student.setInstId("1");
        serviceInst.insert(student);
        
        _logger.info("insert id {}" , student.getId());
    }
    


    @Test
    void get(){
        _logger.info("get...");
        StudentsInst student=serviceInst.get("1271630485503614976","1");
         _logger.info("Students {}",student);
    }
    
    @Test
    void findById(){
        _logger.info("get...");
        StudentsInst student=serviceInst.findById("1271630485503614976","1");
        _logger.info("Students {}",student);
    }
    
    @Test
    void findByIds(){
        _logger.info("get...");
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        _logger.info("Students {}",serviceInst.findByIds(idList,"1"));
    }
    
    @Test
    void update(){
        _logger.info("get...");
        StudentsInst student=serviceInst.get("1271630485503614976","1");
        student.setInstId("1");
        _logger.info("Students {}",student);
         
        _logger.info("update...");
        student.setImages(null);
        serviceInst.update(student);
        _logger.info("updateed.");
         
        student.setImages("ssss".getBytes());
        serviceInst.update(student);
        _logger.info("updateed2.");
    }
    
    @Test
    void delete(){
        _logger.info("delete...");
        serviceInst.delete("b1e2-92fb23b5e512","1");
        serviceInst.delete("921d3377-937a-4578-b1e2-92fb23b5e512","1");
    }
    
    @Test
    void deleteBatch(){
        _logger.info("batchDelete...");
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
        idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
        serviceInst.deleteBatch(idList,"1");
    }
    
    @Test
    void softdeleteById(){
        _logger.info("delete...");
        serviceInst.softDelete("b1e2-92fb23b5e512","1");
    }
    
    @Test
    void softdelete(){
        _logger.info("delete...");
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        serviceInst.softDelete(idList,"1");
    }
    
}