# MyBatis JPA Extra
**MyBatis JPA Extra**对MyBatis扩展JPA功能
   
1.JPA 2.1注释**简化CUID操作**;
	
2.Interceptor实现数据库**SELECT分页查询**;
	
3.**链式**Query查询条件构造器;

4.提供starter,**简化SpringBoot集成**;

## 1、JPA 2.1注释

仅支持6个注释
> * @Entity
> * @Table
> * @Column
> * @Id
> * @GeneratedValue
> * @Transient 

主键策略

支持3种主键策略，4种AUTO自动主键策略 

 1. **AUTO**
	
	snowflakeid
	
    uuid

    uuid.hex

    serial

 2. **SEQUENCE**
 
    generator值为数据库序列名

 3. **IDENTITY**
 
    generator无，根据数据库自动生成方式

## 1、JPA注释

```java

@Entity
@Table(name = "STUDENTS")  
public class Students extends JpaBaseEntity implements Serializable{
	
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
	
	public Students() {}

	public get(){};
	public void set(){};
	//...
}
```

## 2、CURD、Qruey构造器和分页查询

```java
	//新增数据
	@Test
	public void insert() throws Exception{
		_logger.info("insert...");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		service.insert(student);
		
		Thread.sleep(1000);
		_logger.info("insert id " + student.getId());
	}
	
	//根据实体查询并更新
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
		_logger.info("merge id " + student.getId());
	}
	
	//springJDBC 的查询方式
	@Test
	public void find() throws Exception{
		_logger.info("find...");
		_logger.info("find by filter  " 
					+ service.find(" StdNo = '10024' or StdNo = '10004'")
		);

		_logger.info("find by filter with args " 
				+ service.find(
							" StdNo = ? or StdNo = ?  ",
							new Object[]{"10024","10004"},
							new int[]{Types.VARCHAR,Types.INTEGER}
						)
		);	
	}
	
	//根据ID查询
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		Students student=service.get("317d5eda-927c-4871-a916-472a8062df23");
		System.out.println("Students "+student);
		 _logger.info("Students "+student);
	}
	
	//查询数据实体并更新
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
	
	//根据ID删除
	@Test
	public void remove() throws Exception{
		_logger.info("remove...");
		service.remove("921d3377-937a-4578-b1e2-92fb23b5e512");
	}
	
	//根据ID集合批量删除
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.deleteBatch(idList);
	}
	
	//根据ID批量逻辑删除
	@Test
	public void logicDelete() throws Exception{
		_logger.info("logicDelete...");
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		service.logicDelete(idList);
	}

	//根据ID批量删除
	@Test
	public void batchDeleteByIds() throws Exception{
		_logger.info("batchDeleteByIds...");
		service.deleteBatch("2");
		service.deleteBatch("2,639178432667713536");
	}

	//分页查询
	@Test
	public void queryPageResults() throws Exception{
		_logger.info("queryPageResults...");
		 Students student=new Students();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		 //student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 //student.setPageNumber(2);
		 student.calculate(21);
		 List<Students> listStudents = 
				 service.queryPageResults(student).getRows();
		 for (Students s : listStudents) {
			 _logger.info("Students "+s);
		 }
	}

	//mapper id分页查询
	@Test
	public void queryPageResultsByMapperId() throws Exception{
		_logger.info("queryPageResults by mapperId...");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageSize(10);
		 student.setPageNumber(2);
		 List<Students> listStudents = 
				 service.queryPageResults("queryPageResults1",student).getRows();
		 for (Students s : listStudents) {
			 _logger.info("Students "+s);
		 }
	}
	//根据实体查询
	@Test
	public void query() throws Exception{
		_logger.info("query...");
		Students student=new Students();
		student.setStdGender("M");
		List<Students> listStudents =service.query(student);
		 for (Students s : listStudents) {
			 _logger.info("Students "+s);
		 }
	}

	//根据链式条件构造器查询
	//WHERE (stdMajor = '政治' and STDAGE > 30 and stdMajor in ( '政治' , '化学' )  or  ( stdname = '周瑜' or stdname = '吕蒙' ) )
	@Test
	public void filterByQuery() throws Exception{
		_logger.info("filterByQuery...");
		List<Students> listStudents =service.query(
				new Query().eq("stdMajor", "政治").and().gt("STDAGE", 30).and().in("stdMajor", new Object[]{"政治","化学"})
				.or(new Query().eq("stdname", "周瑜").or().eq("stdname", "吕蒙")));
		 for (Students s : listStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	//查询所有记录
	@Test
	public void findAll() throws Exception{
		_logger.info("findAll...");
		List<Students> listStudents =service.findAll();
		 for (Students s : listStudents) {
			 _logger.info("Students "+s);
		 }
	}
	
	//...
```

## 3、映射文件配置

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


##  4、SpringBoot配置

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


##  5、相关资源

[MyBatis网站][1]

[MyBatis GitHub源码][2]

  [1]: http://www.mybatis.org/mybatis-3/
  [2]: https://github.com/mybatis/mybatis-3/
