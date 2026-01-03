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


package org.dromara.mybatis.jpa.test.dao.service.impl;

import java.util.List;

import org.dromara.mybatis.jpa.entity.JpaPageResults;
import org.dromara.mybatis.jpa.service.impl.JpaServiceImpl;
import org.dromara.mybatis.jpa.test.dao.persistence.StudentsMapper;
import org.dromara.mybatis.jpa.test.dao.service.StudentsService;
import org.dromara.mybatis.jpa.test.entity.StudentQueryDto;
import org.dromara.mybatis.jpa.test.entity.Students;
import org.dromara.mybatis.jpa.test.entity.StudentVo;
import org.springframework.stereotype.Service;

@Service
public class StudentsServiceImpl extends JpaServiceImpl<StudentsMapper,Students,String> implements StudentsService{

    @SuppressWarnings("unchecked")
    public JpaPageResults<StudentVo> fetchPageResultsVo(StudentQueryDto entity) {
        entity.build();
        return (JpaPageResults<StudentVo>) this.buildPageResults(entity, getMapper().fetchPageResultsVo(entity));
    }
    
    public List<Students> findByStdNo(String stdNo) {
        return getMapper().findByStdNo(stdNo);
    }
    
    public List<Students> findByStdNoIs(String stdNo) {
        return getMapper().findByStdNoIs(stdNo);
    }
    
    public List<Students> findByStdNoEquals(String stdNo) {
        return getMapper().findByStdNoEquals(stdNo);
    }
    
    
    public List<Students> findByStdAgeBetween(int ageStart,int ageEnd){
        return getMapper().findByStdAgeBetween(ageStart,ageEnd);
    }
    
    public List<Students> findByStdAgeLessThan(int ageLessThan){
        return getMapper().findByStdAgeLessThan(ageLessThan);
    }
    
    public List<Students> findByStdAgeLessThanEqual(int ageLessThanEqual){
        return getMapper().findByStdAgeLessThanEqual(ageLessThanEqual);
    }
    
    public List<Students> findByStdAgeAfter(int ageAfter){
        return getMapper().findByStdAgeAfter(ageAfter);
    }
    
    public List<Students> findByStdAgeBefore(int ageBefore){
        return getMapper().findByStdAgeBefore(ageBefore);
    }
    
    public List<Students> findByImagesNull(){
        return getMapper().findByImagesNull();
    }
    
    public List<Students> findByImagesIsNull(){
        return getMapper().findByImagesIsNull();
    }
    
    public List<Students> findByImagesIsNotNull(){
        return getMapper().findByImagesIsNotNull();
    }
    
    
    public List<Students> findByImagesNotNull(){
        return getMapper().findByImagesNotNull();
    }
    
    public List<Students> findByStdNameLike(String stdName) {
        return getMapper().findByStdNameLike(stdName);
    }
    
    public List<Students> findByStdNameNotLike(String stdName) {
        return getMapper().findByStdNameNotLike(stdName);
    }
    
    public List<Students> findByStdNameStartingWith(String stdName) {
        return getMapper().findByStdNameStartingWith(stdName);
    }
    
    public List<Students> findByStdNameEndingWith(String stdName) {
        return getMapper().findByStdNameEndingWith(stdName);
    }
    
    public List<Students> findByStdNameContaining(String stdName) {
        return getMapper().findByStdNameContaining(stdName);
    }
    
    public List<Students> findByStdGenderOrderByStdAge(String stdGender) {
        return getMapper().findByStdGenderOrderByStdAge(stdGender);
    }
    
    public List<Students> findByStdGenderIsOrderByStdAge(String stdGender) {
        return getMapper().findByStdGenderIsOrderByStdAge(stdGender);
    }
    
    public List<Students> findByStdMajorIn(String... stdMajors) {
        return getMapper().findByStdMajorIn(stdMajors);
    }
    
    public List<Students> findByStdMajorNotIn(List<String> stdMajors) {
        return getMapper().findByStdMajorNotIn(stdMajors);
    }
    
    public List<Students> findByDeletedTrue() {
        return getMapper().findByDeletedTrue();
    }
    
    public List<Students> findByDeletedFalse() {
        return getMapper().findByDeletedFalse();
    }
    
    public List<Students> findByStdGenderIgnoreCase(String stdGender) {
        return getMapper().findByStdGenderIgnoreCase(stdGender);
    }
    
    public List<Students> findByStdNoNot(String stdNo) {
        return getMapper().findByStdNoNot(stdNo);
    }
    
    public List<Students> findByStdMajorAndStdClass(String stdMajor,String stdClass) {
        return getMapper().findByStdMajorAndStdClass(stdMajor,stdClass);
    }
    
    
    public int updatePassword(Students std) {
        return getMapper().updatePassword(std);
    }

}
