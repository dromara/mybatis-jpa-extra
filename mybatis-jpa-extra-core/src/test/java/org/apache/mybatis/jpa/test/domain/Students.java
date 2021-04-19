package org.apache.mybatis.jpa.test.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.mybatis.jpa.persistence.JpaBaseDomain;



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
public class Students extends JpaBaseDomain implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6928570405840778151L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO,generator="snowflakeid")
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_MYBATIS_STUD")
	//@GeneratedValue(strategy=GenerationType.IDENTITY,generator="SEQ_MYBATIS_STUD")
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
