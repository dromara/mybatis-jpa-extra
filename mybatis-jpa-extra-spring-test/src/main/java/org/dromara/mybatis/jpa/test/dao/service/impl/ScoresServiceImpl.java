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


package org.dromara.mybatis.jpa.test.dao.service.impl;

import java.util.List;

import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.test.dao.persistence.ScoresMapper;
import org.dromara.mybatis.jpa.test.dao.service.ScoresService;
import org.dromara.mybatis.jpa.test.entity.Scores;
import org.springframework.stereotype.Service;

@Service
public class ScoresServiceImpl extends JpaServiceImpl<ScoresMapper,Scores,String> implements ScoresService{

    public ScoresServiceImpl() {
    }

    @Override
    public List<Scores> fetchPageResults1(Scores entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Scores> findByStdNo(String stdNo) {
        return getMapper().findByStdNo(stdNo);
    }

    @Override
    public List<Scores> findByStdNoIs(String stdNo) {
        return getMapper().findByStdNoIs(stdNo);
    }

    @Override
    public List<Scores> findByStdNoEquals(String stdNo) {
        return getMapper().findByStdNoEquals(stdNo);
    }

    @Override
    public List<Scores> findByGradeLessThan(int gradeLessThan) {
        return getMapper().findByGradeLessThan(gradeLessThan);
    }

    @Override
    public List<Scores> findByGradeLessThanEqual(int gradeLessThanEqual) {
        return getMapper().findByGradeLessThan(gradeLessThanEqual);
    }

    @Override
    public List<Scores> findByStdNameLike(String stdName) {
        return getMapper().findByStdNameLike(stdName);
    }

    @Override
    public List<Scores> findByStdNameNotLike(String stdName) {
        return getMapper().findByStdNameNotLike(stdName);
    }

    @Override
    public List<Scores> findByStdNameStartingWith(String stdName) {
        return getMapper().findByStdNameStartingWith(stdName);
    }

    @Override
    public List<Scores> findByStdNameEndingWith(String stdName) {
        return getMapper().findByStdNameEndingWith(stdName);
    }

    @Override
    public List<Scores> findByStdNameContaining(String stdName) {
        return getMapper().findByStdNameContaining(stdName);
    }

    @Override
    public List<Scores> findByCourseNameOrderByGrade(String courseName) {
        return getMapper().findByCourseNameOrderByGrade(courseName);
    }

    @Override
    public List<Scores> findByCourseNameIsOrderByGrade(String courseName) {
        return getMapper().findByCourseNameIsOrderByGrade(courseName);
    }

    @Override
    public List<Scores> findByCourseNameIn(String... courseNames) {
        return getMapper().findByCourseIdIn(courseNames);
    }

    @Override
    public List<Scores> findByCourseNameNotIn(List<String> courseNames) {
        return getMapper().findByCourseIdNotIn(courseNames);
    }

    @Override
    public List<Scores> findByDeletedTrue() {
        return getMapper().findByDeletedTrue();
    }

    @Override
    public List<Scores> findByDeletedFalse() {
        return getMapper().findByDeletedFalse();
    }

    @Override
    public List<Scores> findByCourseNameIgnoreCase(String courseName) {
        return getMapper().findByCourseNameIgnoreCase(courseName);
    }

    @Override
    public List<Scores> findByStdNoNot(String stdNo) {
        return getMapper().findByStdNoNot(stdNo);
    }

    @Override
    public List<Scores> findByStdNoAndCourseId(String stdNo, String courseId) {
        return getMapper().findByStdNoAndCourseId(stdNo,courseId);
    }

    @Override
    public List<Scores> findByGradeBetween(int gradeStart, int gradeEnd) {
        return getMapper().findByGradeBetween(gradeStart,gradeEnd);
    }
    
}
