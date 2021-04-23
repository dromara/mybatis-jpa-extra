
# MyBatis JPA Extra
   **MyBatis JPA Extra**对MyBatis扩展JPA功能，旨在基于JPA 2.1的注释简化对单表CUID操作；用Interceptor实现数据库SELECT分页查询；另外提供mybatis-jpa-extra-spring-boot-starter简化SpringBoot集成。
 
相关资源

[MyBatis网站][1]

[MyBatis GitHub源码][2]


## 1、JavaBean注释简单

只支持6个注释
> * @Entity
> * @Table
> * @Column
> * @Id
> * @GeneratedValue
> * @Transient 


@GeneratedValue有3中策略 

 1. **AUTO**
	
	snowflakeid
	
    uuid

    uuid.hex

    serial

 2. **SEQUENCE**
 
    generator值为数据库序列名

 3. **IDENTITY**
 
    generator无，根据数据库自动生成方式


```java
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
 

package org.apache.mybatis.jpa.test.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.mybatis.jpa.persistence.JpaBaseEntity;



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
public class Students extends JpaBaseEntity implements Serializable{
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


```

## 2、单表新增、修改、删除、查询、分页查询

```java
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
 

package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.entity.Students;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisTestRunner {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyBatisTestRunner.class);
	
	public static ApplicationContext context;
	public static StudentsService service;
	
	
	@Test
	public void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		//student.setId("10024");
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		service.insert(student);
		
		Thread.sleep(1000);
		_logger.info("insert id " + student.getId());
		//service.remove(student.getId());
		
	}
	
	
	@Test
	public void merge() throws Exception{
		_logger.info("merge...");
		Students student=new Students();
		//student.setId("10024");
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		service.merge(student);
		
		Thread.sleep(1000);
		_logger.info("insert id " + student.getId());
		//service.remove(student.getId());
		
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Students student=service.get("317d5eda-927c-4871-a916-472a8062df23");
		
		System.out.println("Students "+student);
		 _logger.info("Students "+student);
	}
	
	@Test
	public void update() throws Exception{
		_logger.info("get...");
		Students student=service.get("317d5eda-927c-4871-a916-472a8062df23");
		System.out.println("Students "+student);
		 _logger.info("Students "+student);
		 
		 _logger.info("update...");
		 student.setImages(null);
		 service.update(student);
		 _logger.info("updateed.");
		 
		 student.setImages("ssss".getBytes());
		 service.update(student);
		 _logger.info("updateed2.");
	}
	
	
	@Test
	public void find() throws Exception{
		_logger.info("find...");
		Students student=service.find(Students.class,"317d5eda-927c-4871-a916-472a8062df23");
		
		System.out.println("Students "+student);
		 _logger.info("Students "+student);	 

	}
	
	@Test
	public void remove() throws Exception{
		
		_logger.info("remove...");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		service.remove(student.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.batchDelete(idList);
	}

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		 Students student=new Students();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(1);
		 List<Students> allListStudents = 
				 service.queryPageResults(student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Test
	public void queryPageResultsByMapperId() throws Exception{

		_logger.info("queryPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 
		 List<Students> allListStudents = 
				 service.queryPageResults("queryPageResults1",student).getRows();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
		 
	}
	
	
	
	@Test
	public void findAll() throws Exception{
		_logger.info("findAll...");
		List<Students> allListStudents =service.findAll();
		 for (Students s : allListStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	@Before
	public void initSpringContext(){
		if(context!=null) return;
		_logger.info("init Spring Context...");
		SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=sdf_ymdhms.format(new Date());

		try{
			MyBatisTestRunner runner=new MyBatisTestRunner();
			runner.init();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		_logger.info("-- --Init Start at " + startTime+" , End at  "+sdf_ymdhms.format(new Date()));
	}
	
	//Initialization ApplicationContext for Project
	public void init(){
		
		_logger.info("Application dir "+System.getProperty("user.dir"));
		context = new ClassPathXmlApplicationContext(new String[] {"spring/applicationContext.xml"});
		
		WebContext.applicationContext=context;
		service =(StudentsService)WebContext.getBean("studentsService");
		
	}
	
}

```


## 4、映射文件配置

```xml
<mapper namespace="org.apache.mybatis.jpa.test.dao.persistence.StudentsMapper" >
	<sql id="sql_condition">
		WHERE	1	=	1
    	<if test="id != null">
			AND	ID	=	'${id}'
		</if>
		<if test="stdName != null  and stdName != '' ">
			AND STDNAME like '%${stdName}%'
		</if>
		<if test="stdGender != null  and stdGender != '' ">
			AND	STDGENDER	=	#{stdGender}
		</if>
		<if test="stdMajor != null">
			<![CDATA[AND	STDMAJOR	= #{stdMajor}]]>
		</if>
	</sql>
	
    <select id="queryPageResults" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM STUDENTS 
    	<include refid="sql_condition"/>
    </select>
 
     <select id="queryPageResults1" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM STUDENTS 
    	<include refid="sql_condition"/>
    </select>
       
     <select id="queryBy" parameterType="Students" resultType="Students">
    	SELECT 
    		ID		   ,
			STDNO      ,
			STDNAME    ,
			STDGENDER  ,
			STDAGE     ,
			STDMAJOR   ,
			STDCLASS 
    	FROM ROLES 
    	<include refid="sql_condition"/>
    </select>
  
    <delete id="delete" parameterType="Students" >
    	DELETE FROM STUDENTS WHERE ID=#{id}
    </delete>
    
```


##  5、SpringBoot配置

```ini
#
spring.datasource.username=root
spring.datasource.password=maxkey
spring.datasource.url=jdbc:mysql://localhost/test?autoReconnect=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

mybatis.dialect=mysql
mybatis.type-aliases-package=org.apache.mybatis.jpa.test.entity
mybatis.mapper-locations=classpath*:/org/apache/mybatis/jpa/test/dao/persistence/xml/${mybatis.dialect}/*.xml
mybatis.table-column-escape=true
mybatis.table-column-snowflake-datacenter-id=1
mybatis.table-column-snowflake-machine-id=1
#mybatis.table-column-escape-char=`
```


  [1]: http://www.mybatis.org/mybatis-3/
  [2]: https://github.com/mybatis/mybatis-3/
