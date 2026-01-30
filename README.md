# MyBatis JPA Extra
<p align="center" >
    <img src="mybatis-jpa-extra.png?raw=true"  width="200px"   alt=""/>
</p>


**MyBatis JPA Extra**对MyBatis扩展JPA功能
   
1.Jakarta JPA 3注释**简化CUID操作**;
    
2.增强**SELECT分页**查询;
    
3.**链式**Query查询条件构造器；支持Lambda 形式调用，方便编写各类查询条件

4.@Encrypted注解轻松实现字段数据加密和解密;

5.字段数据自动填充功能;

6.数据库支持

|  数据库        |  支持 |
| ---           | ---   |
|  MySQL        | ✅    |
|  PostgreSQL   | ✅    |
|  Oracle       | ✅    |
|  SqlServer    | ✅    |
|  DB2          | ✅    |

代码托管  

|  <a href="https://gitcode.com/dromara/mybatis-jpa-extra/overview" target="_blank"><b>GitCode</b></a>        |  <a href="https://gitee.com/dromara/mybatis-jpa-extra" target="_blank"><b>Gitee</b></a>  |<a href="https://github.com/dromara/mybatis-jpa-extra" target="_blank"><b>GitHub</b></a>  |

## 1、JPA 3注释

## 1.1、注释

 * @Entity
 * @Table
 * @Column
 * @ColumnDefault
 * @Id
 * @GeneratedValue
 * @Encrypted
 * @Transient 
 * @Temporal
 * @PartitionKey
 * @SoftDelete


## 1.2、主键策略

支持3种主键策略

| 序号    | 策略      |   支持  |
| --------| :-----        | :----   |
| 1     | AUTO          | 主键自动填充策略<br>snowflakeid(雪花ID-默认)<br>uuid(UUID) | 
| 2     | SEQUENCE      | 数据库序列生成，generator值为数据库序列名 | 
| 3     | IDENTITY      | 数据库表自增主键  |

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
    @Encrypted
    private String password;
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
    @SoftDelete
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
    void delete() throws Exception{
        service.delete("921d3377-937a-4578-b1e2-92fb23b5e512");
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
```
## 2.2、逻辑删除
```java    
    //根据IDS批量逻辑删除
    @Test
    void softDelete() throws Exception{
        List<String> idList=new ArrayList<String>();
        idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
        idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
        //...
        service.softDelete(idList);
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
         JpaPage page = new JpaPage(1);
         Students student = new Students();
         student.setStdGender("M");
         JpaPageResults<Students>  results = service.fetch(page,student);
    }
    //根据Query条件分页查询 where stdMajor = '政治' and STDAGE > 30
    @Test
    void fetchByCondition() throws Exception{
         JpaPage page = new JpaPage(1,20);
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
         student.setPageNumber(1);
         JpaPageResults<Students>  results = service.fetchPageResults(student);
    }
    //根据Mapper xml id分页查询,fetchPageResults1在mapper的xml中配置
    @Test
    void fetchPageResultsByMapperId() throws Exception{
         Students student=new Students();
         student.setStdGender("M");
         student.setPageNumber(1);
         JpaPageResults<Students> results = service.fetchPageResults("fetchPageResults1",student);
    }
```


## 2.6、 Lambda查询
```java

    //根据Lambda链式条件构造器查询
    //WHERE (stdMajor = '政治' and STDAGE > 30 and stdMajor in ( '政治' , '化学' )  or  ( stdname = '周瑜' or stdname = '吕蒙' ) )
    service.query(
                new LambdaQuery<Students>().eq(Students::getStdMajor, "政治")
                                     .and().gt(Students::getStdAge, Integer.valueOf(30))
                                     .and().in(Students::getStdMajor, new Object[]{"政治","化学"})
                                     .or(
                                            new LambdaQuery<Students>().eq(Students::getStdName, "周瑜")
                                                                  .or().eq(Students::getStdName, "吕蒙")
                                        )
                );

    //根据Lambda链式条件构造器分页查询
    //where stdMajor = '政治' and stdAge > 30
    JpaPage page = new JpaPage();
    page.setPageSize(20);
    page.setPageable(true);
    LambdaQuery<Students> lambdaQuery =new LambdaQuery<>();
    lambdaQuery.eq(Students::getStdMajor, "政治").and().gt(Students::getStdAge, Integer.valueOf(30));
    JpaPageResults<Students>  results = service.fetch(page,lambdaQuery);

    ...

```

## 2.6、 UpdateWrapper更新
```java
    //根据UpdateWrapper更新数据
    UpdateWrapper updateWrapper = new UpdateWrapper();
    updateWrapper.set("StdMajor", "历史").eq("StdName", "周瑜").or().eq("StdName", "吕蒙");   
    service.update(updateWrapper);

    //更新LambdaUpdateWrapper更新数据，带多重LambdaUpdateWrapper
    List<String> majorList = new ArrayList<>(Arrays.asList("政治","化学"));
    LambdaUpdateWrapper<Students> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper.set(Students::getStdMajor, "历史")
                 .eq(Students::getStdMajor, "政治")
                 .and().gt(Students::getStdAge, Integer.valueOf(30))
                 .and().in(Students::getStdMajor, majorList)
                 .or(new LambdaUpdateWrapper<Students>().eq(Students::getStdName, "周瑜").or().eq(Students::getStdName, "吕蒙"));
        
    service.update(updateWrapper);

```

## 2.8、FindBy查询

实现spring data jpa的findBy功能

```java
    //Mapper接口定义
    //where x.stdNo = ?1
    @Select({})
    public List<Students> findByStdNo(String stdNo);
    //where x.stdNo = ?1
    @Select({})
    public List<Students> findByStdNoIs(String stdNo);
    //where x.stdNo = ?1
    @Select({})
    public List<Students> findByStdNoEquals(String stdNo);
    //where x.stdAge between ?1 and ?2
    @Select({})
    public List<Students> findByStdAgeBetween(int ageStart,int ageEnd);
    //where x.stdAge < ?1
    @Select({})
    public List<Students> findByStdAgeLessThan(int ageLessThan);
    //where x.stdAge <= ?1
    @Select({})
    public List<Students> findByStdAgeLessThanEqual(int ageLessThanEqual);
    //where x.stdAge > ?1
    @Select({})
    public List<Students> findByStdAgeAfter(int ageAfter);
    //where x.stdAge < ?1
    @Select({})
    public List<Students> findByStdAgeBefore(int ageBefore);
    //where x.images is null
    @Select({})
    public List<Students> findByImagesNull();
    //where x.images is null
    @Select({})
    public List<Students> findByImagesIsNull();
    //where x.images is not null
    @Select({})
    public List<Students> findByImagesIsNotNull();
    //where x.images is not null
    @Select({})
    public List<Students> findByImagesNotNull();
    //where x.stdName like ?1
    @Select({})
    public List<Students> findByStdNameLike(String stdName);
    //where x.stdName not like ?1
    @Select({})
    public List<Students> findByStdNameNotLike(String stdName);
    //where x.stdName like ?1 (parameter bound with appended %)
    @Select({})
    public List<Students> findByStdNameStartingWith(String stdName);
    //where x.stdName like ?1 (parameter bound with prepended %)
    @Select({})
    public List<Students> findByStdNameEndingWith(String stdName);
    //where x.stdName like ?1 (parameter bound wrapped in %)
    @Select({})
    public List<Students> findByStdNameContaining(String stdName);
    //where x.stdGender = ?1 order by x.stdAge desc
    @Select({})
    public List<Students> findByStdGenderOrderByStdAge(String stdGender);
    //where x.stdGender = ?1 order by x.stdAge desc
    @Select({})
    public List<Students> findByStdGenderIsOrderByStdAge(String stdGender);
    //where x.stdMajors in ?1
    @Select({})
    public List<Students> findByStdMajorIn(String... stdMajors) ;
    //where x.stdMajors not in ?1
    @Select({})
    public List<Students> findByStdMajorNotIn(List<String> stdMajors);
    //where x.deleted = true
    @Select({})
    public List<Students> findByDeletedTrue();
    //where x.deleted = false
    @Select({})
    public List<Students> findByDeletedFalse();
    //where UPPER(x.stdGender) = UPPER(?1)
    @Select({})
    public List<Students> findByStdGenderIgnoreCase(String stdGender);
    //where x.stdNo <> ?1
    @Select({})
    public List<Students> findByStdNoNot(String stdNo);
    //where x.lastname = ?1 and x.firstname = ?2
    @Select({})
    public List<Students> findByStdMajorAndStdClass(String stdMajor,String stdClass);
```

## 2.9、默认数据自动填充

继承FieldAutoFillHandler，实现insertFill和updateFill函数，可以完成租户字段，创建人、创建时间、修改人、修改时间等默认字段的填充

```java

import org.apache.ibatis.reflection.MetaObject;
import org.dromara.mybatis.jpa.handler.FieldAutoFillHandler;

public class MxkFieldAutoFillHandler  extends FieldAutoFillHandler{

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValue(metaObject , "stdNo", "AutoFill_Insert");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValue(metaObject , "stdNo", "AutoFill_Update");
    }
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
            id , stdno , stdname ,stdgender , stdage , stdmajor , stdclass 
        FROM STUDENTS 
        <include refid="sql_condition"/>
    </select>
 
     <select id="fetchPageResults1" parameterType="Students" resultType="Students">
        SELECT 
            id , stdno , stdname ,stdgender , stdage , stdmajor , stdclass 
        FROM STUDENTS 
        <include refid="sql_condition"/>
    </select>
       
     <select id="queryBy" parameterType="Students" resultType="Students">
        SELECT 
            id , stdno , stdname ,stdgender , stdage , stdmajor , stdclass 
        FROM ROLES 
        <include refid="sql_condition"/>
    </select>

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
#mybatis.configuration.map-underscore-to-camel-case=true
```

###  3.7、依赖引用
例如依赖版本是`3.3.9`,项目的配置如下
### 3.7.1 Maven依赖
```xml
<dependency>
    <groupId>org.dromara.mybatis-jpa-extra</groupId>
    <artifactId>mybatis-jpa-extra</artifactId>
    <version>3.3.9</version>
</dependency>
<dependency>
    <groupId>org.dromara.mybatis-jpa-extra</groupId>
    <artifactId>mybatis-jpa-extra-spring-boot-starter</artifactId>
    <version>3.3.9</version>
</dependency>
```

### 3.7.2 Gradle依赖
```gradle
implementation group: 'org.dromara.mybatis-jpa-extra', name: 'mybatis-jpa-extra', version: '3.3.9'
implementation group: 'org.dromara.mybatis-jpa-extra', name: 'mybatis-jpa-extra-spring-boot-starter', version: '3.3.9'
```
> [提示]
> 当前版本支持springboot v3+， springboot v4 请参考`master`分支


##  5、相关资源

[MyBatis网站][1]

[MyBatis GitHub源码][2]

[MaxKey单点登录认证系统 GitHub源码][3]

[MaxKey单点登录认证系统 Gitee源码][4]

  [1]: http://www.mybatis.org/mybatis-3/
  [2]: https://github.com/mybatis/mybatis-3/
  [3]: https://github.com/dromara/MaxKey
  [4]: https://gitee.com/dromara/MaxKey
