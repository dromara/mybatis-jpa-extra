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


package org.dromara.mybatis.jpa.test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "SCORES")
public class Scores extends JpaEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 2525767070397190697L;
    @Id
    @Column
    @GeneratedValue
    private String id;
    /**
     * 学号
     */
    @Column
    private String stdNo;
    /**
     * 学生姓名
     */
    @Column
    private String stdName;
    /**
     * 课程编码
     */
    @Column
    private String courseId;
    /**
     * 课程名称
     */
    @Column
    private String courseName;
    /**
     * 成绩
     */
    @Column
    int grade;
    /**
     * 修改人
     */
    @Column
    private String modifyBy;
    /**
     * 修改时间
     */
    @GeneratedValue
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifyDate;
    /**
     * 逻辑删除
     */
    @SoftDelete
    @Column(name = "deleted")
    private char deleted;

    public Scores() {
        super();
    }
    
    public Scores(String courseId) {
        this.courseId = courseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStdNo() {
        return stdNo;
    }

    public void setStdNo(String stdNo) {
        this.stdNo = stdNo;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public char getDeleted() {
        return deleted;
    }

    public void setDeleted(char deleted) {
        this.deleted = deleted;
    }

}
