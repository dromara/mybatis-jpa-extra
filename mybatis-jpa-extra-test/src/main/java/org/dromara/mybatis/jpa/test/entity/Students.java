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

import org.dromara.mybatis.jpa.persistence.JpaBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
public class Students extends JpaBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6928570405840778151L;

	@Id
	@Column
	@GeneratedValue
	// @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_MYBATIS_STUD")
	// @GeneratedValue(strategy=GenerationType.IDENTITY,generator="SEQ_MYBATIS_STUD")
	private String id;
	@Column
	private String stdNo;
	@Column
	private String stdName;
	@Column
	private String stdGender;
	@Column
	private int stdAge;
	@Column
	private String stdMajor;
	@Column
	private String stdClass;
	@Column
	private byte[] images;

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

	public int getStdAge() {
		return stdAge;
	}

	public void setStdAge(int stdAge) {
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

	@Override
	public String toString() {
		return "Students [id=" + id + ", stdNo=" + stdNo + ", stdName=" + stdName + ", stdGender=" + stdGender
				+ ", stdAge=" + stdAge + ", stdMajor=" + stdMajor + ", stdClass=" + stdClass + "]";
	}

}
