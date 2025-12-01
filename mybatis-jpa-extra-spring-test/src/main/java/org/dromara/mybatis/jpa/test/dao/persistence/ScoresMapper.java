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

/**
 *
 */
package org.dromara.mybatis.jpa.test.dao.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.dromara.mybatis.jpa.IJpaMapper;
import org.dromara.mybatis.jpa.test.entity.Scores;

/**
 * @author Crystal.Sea
 *
 */

@Mapper
public interface ScoresMapper extends IJpaMapper<Scores> {

    public List<Scores> fetchPageResults1(Scores entity);

    public List<Scores> fetchPageResults(Scores entity);

    @Select({})
    public List<Scores> findByStdNo(String stdNo);

    @Select({})
    public List<Scores> findByStdNoIs(String stdNo);

    @Select({})
    public List<Scores> findByStdNoEquals(String stdNo);

    @Select({})
    public List<Scores> findByGradeLessThan(int ageLessThan);

    @Select({})
    public List<Scores> findByGradeLessThanEqual(int ageLessThanEqual);
    @Select({})
    public List<Scores> findByGradeBetween(int gradeStart,int gradeEnd);
    
    @Select({})
    public List<Scores> findByStdNameLike(String stdName);

    @Select({})
    public List<Scores> findByStdNameNotLike(String stdName);

    @Select({})
    public List<Scores> findByStdNameStartingWith(String stdName);

    @Select({})
    public List<Scores> findByStdNameEndingWith(String stdName);

    @Select({})
    public List<Scores> findByStdNameContaining(String stdName);

    @Select({})
    public List<Scores> findByCourseNameOrderByGrade(String courseName);

    @Select({})
    public List<Scores> findByCourseNameIsOrderByGrade(String courseName);

    @Select({})
    public List<Scores> findByCourseIdIn(String... courseIds) ;

    @Select({})
    public List<Scores> findByCourseIdNotIn(List<String> courseIds);

    @Select({})
    public List<Scores> findByDeletedTrue();

    @Select({})
    public List<Scores> findByDeletedFalse();

    @Select({})
    public List<Scores> findByCourseNameIgnoreCase(String courseName);

    @Select({})
    public List<Scores> findByStdNoNot(String stdNo);

    @Select({})
    public List<Scores> findByStdNoAndCourseId(String stdNo,String courseId);

}
