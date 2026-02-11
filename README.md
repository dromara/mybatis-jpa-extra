# MyBatis JPA Extra
<p align="center" >
    <img src="mybatis-jpa-extra.png?raw=true"  width="200px"   alt=""/>
</p>


**MyBatis JPA Extra**对MyBatis扩展JPA功能
   
1.Jakarta JPA 注释**简化CUID操作**;
    
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

## 1、JPA 注释

## 1.1、注释

| 注释      | 作用    |功能描述 |
| :-----        | :----   | :----   |
| @Entity          | class  |  标识一个类为JPA实体  | 
| @Table           | class  |  指定实体类对应的数据库表名  | 
| @Column          | field  |  定义字段与数据库列的映射关系，支持自定义列名、是否可为空等属性  | 
| @Id              | field  |  标识主键字段  | 
| @GeneratedValue  | field  |  定义主键生成策略，支持AUTO、SEQUENCE和IDENTITY  | 
| @Encrypted       | field  |  标记加密的字段，支持加密算法(如SM4、AES、DES、DESede)  | 
| @PartitionKey    | field  |  分库分表，多租户区分  | 
| @SoftDelete      | field  |  标记逻辑删除字段。当执行删除操作时，该字段会被更新为标记值（如 y/1） | 
| @ColumnDefault   | field  |  字段设置默认值，避免手动初始化  | 
| @Transient       | field  |  标识字段不映射到数据库列  |


## 1.2、主键策略

支持3种主键Id策略

| 策略      |   支持  |
| :-----        | :----   |
| AUTO          | 主键自动填充策略<br>snowflakeid(雪花ID-默认)<br>ulid<br>uuid(UUID) | 
| SEQUENCE      | 数据库序列生成，generator值为数据库序列名 | 
| IDENTITY      | 数据库表自增主键  |


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
        Students student = service.get("1");
        student.setStdMajor("政治");
        service.update(student);
        //service.merge(student);
    }
    //根据ID查询
    @Test
    void get() throws Exception{
        Students student = service.get("2");
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
        service.delete("3");
    }
    //根据ID集合批量删除
    @Test
    void batchDelete() throws Exception{
        List<String> idList = new ArrayList<String>();
        idList.add("4");
        //...
        service.deleteBatch(idList);
    }
```
## 2.2、逻辑删除
```java    
    //根据IDS批量逻辑删除
    @Test
    void softDelete() throws Exception{
        //逻辑删除
        service.softDelete("5");

        //批量逻辑删除    
        List<String> idList=new ArrayList<String>();
        idList.add("6");
        idList.add("7");
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
                new Query().eq("stdMajor", "政治")
                .and().gt("STDAGE", 30)
                .and().in("stdMajor", new Object[]{"政治","化学"})
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

## 2.5、多表关联分页查询，根据mapper的xml分页查询

```java
    //根据Mapper xml配置fetchPageResults分页查询
    @Test
    void fetchPageResults() throws Exception{
         Students student = new Students();
         student.setStdGender("M");
         student.setPageNumber(1);
         JpaPageResults<Students>  results = service.fetchPageResults(student);
    }
    //根据Mapper xml id分页查询,fetchPageResults1在mapper的xml中配置
    @Test
    void fetchPageResultsByMapperId() throws Exception{
         Students student = new Students();
         student.setStdGender("M");
         student.setPageNumber(1);
         JpaPageResults<Students> results = service.fetchPageResults("fetchPageResults1",student);
    }
```


## 2.6、 Lambda查询
```java

    //根据Lambda链式条件构造器查询
    //WHERE (stdMajor = '政治' and STDAGE > 30 and stdMajor in ( '政治' , '化学' )  or  ( stdname = '周瑜' or stdname = '吕蒙' ) )
    service.query(new LambdaQuery<Students>()
                    .eq(Students::getStdMajor, "政治")
                    .and().gt(Students::getStdAge, Integer.valueOf(30))
                    .and().in(Students::getStdMajor, new Object[]{"政治","化学"})
                    .or( new LambdaQuery<Students>()
                            .eq(Students::getStdName, "周瑜")
                            .or().eq(Students::getStdName, "吕蒙"))
                );

    //根据Lambda链式条件构造器分页查询
    //where stdMajor = '政治' and stdAge > 30
    JpaPage page = new JpaPage(1,20);
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
                 .or(new LambdaUpdateWrapper<Students>()
                        .eq(Students::getStdName, "周瑜")
                        .or()
                        .eq(Students::getStdName, "吕蒙"));
        
    service.update(updateWrapper);

```

## 2.8、FindBy查询

`@Select({})` 标识findBy，实现spring data jpa的findBy功能

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
public class MxkFieldAutoFillHandler  extends FieldAutoFillHandler{
    //插入
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValue(metaObject , "stdNo", "AutoFill_Insert");
    }
    //更新
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValue(metaObject , "stdNo", "AutoFill_Update");
    }
}
```

## 3、 使用方法

### 3.1、实体类定义

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
    @Column(insertable = false)
    @GeneratedValue
    private LocalDateTime modifyDate;
    @SoftDelete
    @Column(name ="is_deleted")
    private String isDeleted;
    //...
    //getter setter
}
```

### 3.2、Mapper定义

```java
@Mapper
public interface StudentsMapper extends IJpaMapper<Students,String> {

    //根据mapper.xml的fetchPageResults语句分页查询
    public List<Students> fetchPageResults(Students entity);

    //根据mapper.xml的fetchPageResults1语句自定义分页查询
    public List<Students> fetchPageResults1(Students entity);

    //根据mapper.xml的fetchPageResultsVo语句自定返回类型分页查询，返回类型JpaPageResults<StudentVo>
    public List<StudentVo> fetchPageResultsVo(StudentQueryDto entity);

    //实现JPA findBy功能，无需写SQL
    @Select({})
    public List<Students> findByStdNo(String stdNo);

}
```

### 3.3、Service接口

```java
public interface StudentsService extends IJpaService<Students,String>{

    //fetchPageResults/fetchPageResults1无需定义接口

    public JpaPageResults<StudentVo> fetchPageResultsVo(StudentQueryDto entity) ;

    public List<Students> findByStdNo(String stdNo);
}
```

### 3.4、Service实现

```java
public class StudentsServiceImpl extends AbstractJpaRepository<StudentsMapper,Students,String> implements StudentsService{
    
    //fetchPageResults/fetchPageResults1无需定义实现

    //自定义返回类型JpaPageResults<StudentVo>
    @SuppressWarnings("unchecked")
    public JpaPageResults<StudentVo> fetchPageResultsVo(StudentQueryDto entity) {
        entity.build();
        return (JpaPageResults<StudentVo>) this.buildPageResults(entity, getMapper().fetchPageResultsVo(entity));
    }
    
    //mapper findBy调用
    public List<Students> findByStdNo(String stdNo) {
        return getMapper().findByStdNo(stdNo);
    }
}
```

### 3.5、mapper配置

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

###  3.6、SpringBoot配置

```properties
#
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost/test?autoReconnect=true&characterEncoding=UTF-8&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=maxkey

mybatis.dialect=mysql
mybatis.type-aliases-package=org.apache.mybatis.jpa.test.entity
mybatis.mapper-locations=classpath*:/org/apache/mybatis/jpa/test/dao/persistence/xml/${mybatis.dialect}/*.xml
#定界符，默认 false
#mybatis.table-column-escape=false
#表、字段等定界符
#mybatis.table-column-escape-char=`
#驼峰与下划线转换配置，默认 false
#mybatis.configuration.map-underscore-to-camel-case=true
```

###  3.7、依赖引用
例如依赖版本`3.4.4`配置如下
### 3.7.1 Maven依赖
```xml
<dependency>
    <groupId>org.dromara.mybatis-jpa-extra</groupId>
    <artifactId>mybatis-jpa-extra</artifactId>
    <version>3.4.4</version>
</dependency>
<dependency>
    <groupId>org.dromara.mybatis-jpa-extra</groupId>
    <artifactId>mybatis-jpa-extra-spring</artifactId>
    <version>3.4.4</version>
</dependency>
<dependency>
    <groupId>org.dromara.mybatis-jpa-extra</groupId>
    <artifactId>mybatis-jpa-extra-spring-boot-starter</artifactId>
    <version>3.4.4</version>
</dependency>
```

### 3.7.2 Gradle依赖
```gradle
implementation group: 'org.dromara.mybatis-jpa-extra', name: 'mybatis-jpa-extra', version: '3.4.4'
implementation group: 'org.dromara.mybatis-jpa-extra', name: 'mybatis-jpa-extra-spring', version: '3.4.4'
implementation group: 'org.dromara.mybatis-jpa-extra', name: 'mybatis-jpa-extra-spring-boot-starter', version: '3.4.4'
```
> [提示]
> 当前版本支持springboot v4+， springboot v3 请参考`3.3`分支

##  4、案例

|  项目         |  代码 |
| ---           | ---   |
|  MaxKey单点登录认证系统        | <a href="https://github.com/dromara/MaxKey" target="_blank">GitHub源码</a> -<a href="https://gitee.com/dromara/MaxKey" target="_blank">Gitee源码</a> |
|  Surpass API开放平台           | <a href="https://github.com/tomsun28/surpass" target="_blank">GitHub源码</a> -<a href="https://gitee.com/tomsun28/bootshiro" target="_blank">Gitee源码</a> |


##  5、MyBatis资源

[MyBatis网站][1]

[MyBatis 源码][2]


[1]: http://www.mybatis.org/mybatis-3/
[2]: https://github.com/mybatis/mybatis-3/
