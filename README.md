# MyBatis JPA Extra
**MyBatis JPA Extra**对MyBatis扩展JPA功能
   
1.Jakarta JPA 3注释**简化CUID操作**;
    
2.Interceptor实现数据库**SELECT分页查询**;
    
3.**链式**Query查询条件构造器;

4.提供starter,**简化SpringBoot集成**;

5.数据库支持
| 数据库          |   支持  |
| :-----          | :----   |
| **MySQL**       | ✔      | 
| **PostgreSQL**  | ✔      | 
| **Oracle**      | ✔      |
| **SqlServer**   | ✔      |
| **DB2**         | ✔      |
| **Derby**       | ✔      |


## 1、JPA 3注释

## 1.1、注释

> * @Entity
> * @Table
> * @Column
> * @Id
> * @GeneratedValue
> * @Transient 
> * @Temporal
> * @PartitionKey
> * @ColumnDefault
> * @ColumnLogic

## 1.2、主键策略

支持3种主键策略

| 序号    | 策略      |   支持  |
| --------| :-----        | :----   |
| 1     | **AUTO**          | 4种主键自动填充策略<br>snowflakeid(雪花ID-默认)<br>uuid(UUID)<br>uuid.hex(UUID十六进制)<br>serial(JPA Extra序列)   | 
| 2     | **SEQUENCE**      | 数据库序列生成，generator值为数据库序列名 | 
| 3     | **IDENTITY**      | 数据库表自增主键  |

## 1.3、Java Bean 注释

```java
@Entity
@Table(name = "STUDENTS")  
public class Students extends JpaEntity implements Serializable{
    @Id
    @Column
    @GeneratedValue
    private String id;
    @Column
    private String stdNo;
    @Column
    private String stdName;
    @Column
    @ColumnDefault("'M'")
    private String stdGender;
    @Column
    private int stdAge;
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
    @ColumnLogic
    @Column(name ="is_deleted")
    private int isDeleted;
    //getter setter
}
```
## 2、基本操作

## 2.1、CURD

```java
    //新增数据
    @Test
    void insert() throws Exception{
        Students student = new Students();
        student.setStdNo("10024");
        student.setStdGender("M");
        student.setStdName("司马昭");
        student.setStdAge(20);
        student.setStdMajor("政治");
        student.setStdClass("4");
        service.insert(student);
    }
    //查询数据实体并更新
    @Test
    void update() throws Exception{
        Students student = service.get("317d5eda-927c-4871-a916-472a8062df23");
        student.setStdMajor("政治");
        service.update(student);
    }
    //根据实体查询并更新
    @Test
    void merge() throws Exception{
        Students student = new Students();
        student.setStdMajor("政治");
        student.setStdClass("4");
        service.merge(student);
    }
    //根据ID查询
    @Test
    void get() throws Exception{
        Students student = service.get("317d5eda-927c-4871-a916-472a8062df23");
    }
    //根据实体查询
    @Test
    void query() throws Exception{
        Students student = new Students();
        student.setStdGender("M");
        List<Students> listStudents = service.query(student);
    }
    //查询所有记录
    @Test
    void findAll() throws Exception{
        List<Students> listStudents = service.findAll();
    }
    //根据ID删除
    @Test
    void remove() throws Exception{
        service.remove("921d3377-937a-4578-b1e2-92fb23b5e512");
    }
    //根据ID集合批量删除
    @Test
    void batchDelete() throws Exception{
        List<String> idList = new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        //...
        service.deleteBatch(idList);
    }
    //根据ID批量删除
    @Test
    void batchDeleteByIds() throws Exception{
        service.deleteBatch("2");
        service.deleteBatch("2,639178432667713536");
    }
```
## 2.2、逻辑删除
```java    
    //根据ID删除或者ID字符串分隔符,批量逻辑删除
    @Test
    void logicDelete() throws Exception{
        service.logicDelete("2");
        service.logicDelete("2,639178432667713536");
    }
    //根据IDS批量逻辑删除
    @Test
    void logicBatchDelete() throws Exception{
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        //...
        service.logicDelete(idList);
    }
    //根据IDS字符串和分割符批量逻辑删除
    @Test
    void logicDeleteSplit() throws Exception{
        service.logicDeleteSplit("2,639178432667713536",",");
    }

```

## 2.3、Find查询和Qruey构造器

```java
    //SpringJDBC的查询方式 where StdNo = '10024' or StdNo = '10004'
    @Test
    void find() throws Exception{
        List<Students> listStudents = service.find(" StdNo = ? or StdNo = ?  ",
                            new Object[]{"10024","10004"},
                            new int[]{Types.VARCHAR,Types.INTEGER}
                        );
    }
    //根据链式条件构造器查询
    //WHERE (stdMajor = '政治' and STDAGE > 30 and stdMajor in ( '政治' , '化学' )  or  ( stdname = '周瑜' or stdname = '吕蒙' ) )
    @Test
    void queryByCondition() throws Exception{
        List<Students> listStudents = service.query(
                new Query().eq("stdMajor", "政治").and().gt("STDAGE", 30).and().in("stdMajor", new Object[]{"政治","化学"})
                .or(new Query().eq("stdname", "周瑜").or().eq("stdname", "吕蒙")));
    }
```

## 2.4、单表分页查询

```java
    //根据实体分页查询
    @Test
    void fetch() throws Exception{
         JpaPage page = new JpaPage();
         page.setPageSize(20);
         page.setPageable(true);
         Students student = new Students();
         student.setStdGender("M");
         JpaPageResults<Students>  results = service.fetch(page,student);
    }
    //根据Query条件分页查询 where stdMajor = '政治' and STDAGE > 30
    @Test
    void fetchByCondition() throws Exception{
         JpaPage page = new JpaPage();
         page.setPageSize(20);
         page.setPageable(true);
         Query condition = new Query().eq("stdMajor", "政治").and().gt("STDAGE", 30);
         JpaPageResults<Students>  results = service.fetch(page,condition);
    }
```

## 2.5、根据mapper的xml分页查询

```java
    //根据Mapper xml配置fetchPageResults分页查询
    @Test
    void fetchPageResults() throws Exception{
         Students student=new Students();
         student.setStdGender("M");
         student.setPageSize(10);
         student.calculate(21);
         JpaPageResults<Students>  results = service.fetchPageResults(student);
    }
    //根据Mapper xml id分页查询,fetchPageResults1在mapper的xml中配置
    @Test
    void fetchPageResultsByMapperId() throws Exception{
         Students student=new Students();
         student.setStdGender("M");
         student.setPageSize(10);
         student.setPageNumber(2);
         JpaPageResults<Students> results = service.fetchPageResults("fetchPageResults1",student);
    }
```

## 3、mapper配置

```xml
<mapper namespace="org.apache.mybatis.jpa.test.dao.persistence.StudentsMapper" >
    <sql id="sql_condition">
        WHERE   1   =   1
        <if test="id != null">
            AND ID  =   '${id}'
        </if>
        <if test="stdName != null  and stdName != '' ">
            AND STDNAME like '%${stdName}%'
        </if>
        <if test="stdGender != null  and stdGender != '' ">
            AND STDGENDER   =   #{stdGender}
        </if>
        <if test="stdMajor != null">
            <![CDATA[AND    STDMAJOR    = #{stdMajor}]]>
        </if>
    </sql>
    
    <select id="fetchPageResults" parameterType="Students" resultType="Students">
        SELECT 
            ID         ,
            STDNO      ,
            STDNAME    ,
            STDGENDER  ,
            STDAGE     ,
            STDMAJOR   ,
            STDCLASS 
        FROM STUDENTS 
        <include refid="sql_condition"/>
    </select>
 
     <select id="fetchPageResults1" parameterType="Students" resultType="Students">
        SELECT 
            ID         ,
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
            ID         ,
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
#mybatis.table-column-escape-char=`
```


##  5、相关资源

[MyBatis网站][1]

[MyBatis GitHub源码][2]

[MaxKey单点登录认证系统 GitHub源码][3]

[MaxKey单点登录认证系统 Gitee源码][4]

  [1]: http://www.mybatis.org/mybatis-3/
  [2]: https://github.com/mybatis/mybatis-3/
  [3]: https://github.com/dromara/MaxKey
  [4]: https://gitee.com/dromara/MaxKey
