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
import java.time.LocalDateTime;
import java.util.Arrays;

import org.dromara.mybatis.jpa.annotations.ColumnDefault;
import org.dromara.mybatis.jpa.annotations.Encrypted;
import org.dromara.mybatis.jpa.annotations.SoftDelete;
import org.dromara.mybatis.jpa.entity.JpaEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/*
   ID                   varchar(40)                    not null,
   NAME                 varchar(60)                    not null,
   STATUS               char(1)                        null,
   CREATEBY             varchar(40)                    null,
   CREATEDATE           date                           null,
   UPDATEBY             varchar(40)                    null,
   UPDATEDATE           date                           null,
   constraint PK_ROLES primary key clustered (ID)
 */
/**
 * @author Crystal.Sea
 *
 */
@Entity
@Table(name = "STUDENTS")
public class Students extends JpaEntity implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6928570405840778151L;

    @Id
    @Column
    @GeneratedValue
    private String id;
    @Column
    private String stdNo;
    //Encrypted default is AES , support SM4 , AES , DES , DESede
    @Column
    @Encrypted
    private String password;
    @Column
    private String stdName;
    @Column
    @ColumnDefault("'M'")
    private String stdGender;
    @Column(name ="STDAGE")
    private Integer stdAge;
    @Column
    private String stdMajor;
    @Column
    private String stdClass;
    @Column
    private byte[] images;
    @Column(insertable = false)
    @GeneratedValue
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifyDate;
    @SoftDelete
    @Column(name ="deleted")
    private char deleted;
    
    public Students() {
        super();
    }

    public Students(String stdNo) {
        this.stdNo = stdNo;
    }

    public String getStdNo() {
        return stdNo;
    }

    public void setStdNo(String stdNo) {
        this.stdNo = stdNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImages() {
        return images;
    }

    public void setImages(byte[] images) {
        this.images = images;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Students [id=");
        builder.append(id);
        builder.append(", stdNo=");
        builder.append(stdNo);
        builder.append(", password=");
        builder.append(password);
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
        builder.append(", images=");
        builder.append(Arrays.toString(images));
        builder.append(", modifyDate=");
        builder.append(modifyDate);
        builder.append(", deleted=");
        builder.append(deleted);
        builder.append("]");
        return builder.toString();
    }

}
