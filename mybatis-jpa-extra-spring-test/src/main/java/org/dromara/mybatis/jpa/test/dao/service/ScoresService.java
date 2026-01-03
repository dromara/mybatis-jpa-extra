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


package org.dromara.mybatis.jpa.test.dao.service;

import java.util.List;

import org.dromara.mybatis.jpa.service.IJpaService;
import org.dromara.mybatis.jpa.test.entity.Scores;

public interface ScoresService extends IJpaService<Scores,String>{

    public List<Scores> fetchPageResults1(Scores entity);

    public List<Scores> findByStdNo(String stdNo);


    public List<Scores> findByStdNoIs(String stdNo);

    public List<Scores> findByGradeBetween(int gradeStart,int gradeEnd);
    
    public List<Scores> findByStdNoEquals(String stdNo);

    public List<Scores> findByGradeLessThan(int ageLessThan);

    public List<Scores> findByGradeLessThanEqual(int ageLessThanEqual);


    public List<Scores> findByStdNameLike(String stdName);

    public List<Scores> findByStdNameNotLike(String stdName);

    public List<Scores> findByStdNameStartingWith(String stdName);

    public List<Scores> findByStdNameEndingWith(String stdName);

    public List<Scores> findByStdNameContaining(String stdName);

    public List<Scores> findByCourseNameOrderByGrade(String courseName);

    public List<Scores> findByCourseNameIsOrderByGrade(String courseName);

    public List<Scores> findByCourseNameIn(String... courseNames) ;

    public List<Scores> findByCourseNameNotIn(List<String> courseNames);

    public List<Scores> findByDeletedTrue();

    public List<Scores> findByDeletedFalse();

    public List<Scores> findByCourseNameIgnoreCase(String courseName);

    public List<Scores> findByStdNoNot(String stdNo);

    public List<Scores> findByStdNoAndCourseId(String stdNo,String courseId);

}
