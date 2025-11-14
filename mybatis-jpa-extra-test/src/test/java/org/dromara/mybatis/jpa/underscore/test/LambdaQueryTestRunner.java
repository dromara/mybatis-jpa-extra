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
import java.util.Arrays;
import java.util.List;

import org.dromara.mybatis.jpa.query.LambdaQuery;
import org.dromara.mybatis.jpa.test.entity.Scores;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LambdaQueryTestRunner  extends BaseUnderscoreTestRunner{
    private static final Logger _logger = LoggerFactory.getLogger(LambdaQueryTestRunner.class);

    @Test
    void queryByLambdaQuery(){
        _logger.info("query by LambdaQuery ...");
        List<String> majorList = new ArrayList<>(Arrays.asList("政治","化学"));
        service.query(
                new LambdaQuery<Scores>().eq(Scores::getCourseName, "政治").and().gt(Scores::getGrade, Integer.valueOf(30)).and().in(Scores::getCourseName, majorList)
                .or(new LambdaQuery<Scores>().eq(Scores::getStdName, "周瑜").or().eq(Scores::getStdName, "吕蒙")));
    }
    
    @Test
    void queryByLambdaQuery2(){
        _logger.info("query by LambdaQuery ...");
        List<String> majorList = new ArrayList<>(Arrays.asList("政治","化学"));
        service.query(
                new LambdaQuery<Scores>().eq(Scores::getCourseName, "政治").gt(Scores::getGrade, Integer.valueOf(30)).in(Scores::getCourseName, majorList)
                .or(new LambdaQuery<Scores>().eq(Scores::getStdName, "周瑜").or().eq(Scores::getStdName, "吕蒙")));
    }
    
    
    @Test
    void queryByLambdaQueryIterator(){
        _logger.info("query by LambdaQuery ...");
        List<String> majorList = List.of("政治","化学");
        _logger.debug("{}",majorList.getClass().getSimpleName());
        List<Scores> list = service.query(new LambdaQuery<Scores>().in(Scores::getCourseName, majorList));

        _logger.info("list {}",list);
        
        List<String > ids = list.stream().map(Scores::getId).distinct().toList();
        
        _logger.info("ids {}",ids.getClass().getCanonicalName());
        
        list = service.query(new LambdaQuery<Scores>().in(Scores::getId, majorList));
        _logger.info("id query list {}",list);    }

}