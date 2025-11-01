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

package org.dromara.mybatis.jpa.test.entity;

import java.io.Serializable;
import org.dromara.mybatis.jpa.entity.JpaEntity;


public class StudentQueryDto extends JpaEntity implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -930733935605100727L;

    private String id;
    
    private String stdNo;


    private String stdName;

    private String stdGender;

    private Integer stdAge;

    private String stdMajor;

    private String stdClass;
    
    public StudentQueryDto() {
        super();
    }

    public StudentQueryDto(String stdNo) {
        this.stdNo = stdNo;
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

    public String getStdGender() {
        return stdGender;
    }

    public void setStdGender(String stdGender) {
        this.stdGender = stdGender;
    }

    public Integer getStdAge() {
        return stdAge;
    }

    public void setStdAge(Integer stdAge) {
        this.stdAge = stdAge;
    }

    public String getStdMajor() {
        return stdMajor;
    }

    public void setStdMajor(String stdMajor) {
        this.stdMajor = stdMajor;
    }

    public String getStdClass() {
        return stdClass;
    }

    public void setStdClass(String stdClass) {
        this.stdClass = stdClass;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StudentQueryDto [stdNo=");
        builder.append(stdNo);
        builder.append(", stdName=");
        builder.append(stdName);
        builder.append(", stdGender=");
        builder.append(stdGender);
        builder.append(", stdAge=");
        builder.append(stdAge);
        builder.append(", stdMajor=");
        builder.append(stdMajor);
        builder.append(", stdClass=");
        builder.append(stdClass);
        builder.append("]");
        return builder.toString();
    }

    
}
