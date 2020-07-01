-- MyBatis入门学习--

1. MyBatis简介

1. MyBatisPlus官网:https://mp.baomidou.com/guide/

2. 快速开始

1. 对于MyBatis的整合常常有三种方式，分别是：MyBatis+MP 、 Spring+MyBatis+MP 、 SpringBoot+MyBatis+MP。

2.1 创建数据库以及表

1. 随便使用一个数据库和表都是可以的：

    CREATE DATABASE mybatis_plus CHARSET utf8;
    
    USE mybatis_plus;
    
    #创建用户表
    CREATE TABLE USER (
        id BIGINT(20) PRIMARY KEY NOT NULL COMMENT '主键',
        NAME VARCHAR(30) DEFAULT NULL COMMENT '姓名',
        age INT(11) DEFAULT NULL COMMENT '年龄',
        email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
        manager_id BIGINT(20) DEFAULT NULL COMMENT '直属上级id',
        create_time DATETIME DEFAULT NULL COMMENT '创建时间',
        CONSTRAINT manager_fk FOREIGN KEY (manager_id)
            REFERENCES USER (id)
    )  ENGINE=INNODB CHARSET=UTF8;
    
    #初始化数据：
    INSERT INTO USER (id, NAME, age, email, manager_id
    	, create_time)
    VALUES (1087982257332887553, '大boss', 40, 'boss@baomidou.com', NULL
    		, '2019-01-11 14:20:20'),
    	(1088248166370832385, '王天风', 25, 'wtf@baomidou.com', 1087982257332887553
    		, '2019-02-05 11:12:22'),
    	(1088250446457389058, '李艺伟', 28, 'lyw@baomidou.com', 1088248166370832385
    		, '2019-02-14 08:31:16'),
    	(1094590409767661570, '张雨琪', 31, 'zjq@baomidou.com', 1088248166370832385
    		, '2019-01-14 09:15:15'),
    	(1094592041087729666, '刘红雨', 32, 'lhm@baomidou.com', 1088248166370832385
    		, '2019-01-14 09:48:16');

2.2 创建工程

2.2.1 MyBatis + MP

1. 创建一个Maven项目不用选择骨架直接Next即可。



1. 导入依赖：MyBatis-plus（3.1.1）、Mysql驱动包、数据库连接池（Druid）、lombok（简化代码工具包）、junit（单元测试），slf4j（日志工具）、以及修改JDK版本的插件。

    <packaging>jar</packaging>
    
    
    <!--Mybatis-plus-->
      <!-- https://mvnrepository.com/artifact/com.baomidou/mybatis-plus -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus</artifactId>
                <version>3.1.1</version>
            </dependency>
    
            <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.6</version>
            </dependency>
    
            <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.1.12</version>
            </dependency>
    
            <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.12</version>
                <scope>provided</scope>
            </dependency>
    
            <!-- https://mvnrepository.com/artifact/junit/junit -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>
    
    <!-- jdk1.8 -->
    <build>
       <plugins>
    	<plugin>
    		<groupId>org.apache.maven.plugins</groupId>
    		<artifactId>maven-compiler-plugin</artifactId>
    		<version>3.2</version>
    		<configuration>
    			<source>1.8</source>
    			<target>1.8</target>
    			<encoding>UTF-8</encoding>
    		</configuration>
    	</plugin>
       </plugins>
    </build>

2.3 创建子module（就是创建一个子module继承自父module）



1. 创建的子module会集成来自父module中的依赖的；又是可能不会及时生效需要刷新一下Maven便可以了。



1. 编写全局的Mybatis配置文件在resources文件夹下，如果新创建的子module文件夹不是对应的source root文件夹或者不是resource文件夹需要进行手动的设置。下面是具体设置的操作图以及Mybatis全局配置文件mybatis-config.xml文件的内容。



1. mybatis-config.xml配置文件中的内容，mapper中配置的映射配置文件我创建在了与mybatis-config.xml文件夹同级的目录所以不需要在加上文件夹路径。

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE configuration
            PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-config.dtd">
    <!-- mybatis的主配置文件 -->
    <configuration>
        <!-- 配置环境 -->
        <environments default="mysql">
            <!-- 配置mysql的环境-->
            <environment id="mysql">
                <!-- 配置事务的类型-->
                <transactionManager type="JDBC"></transactionManager>
                <!-- 配置数据源（连接池） -->
                <dataSource type="POOLED">
                    <!-- 配置连接数据库的4个基本信息 -->
                    <property name="driver" value="com.mysql.jdbc.Driver"/>
                    <property name="url" value="jdbc:mysql://localhost:3306/mybatis_plus"/>
                    <property name="username" value="root"/>
                    <property name="password" value="root"/>
                </dataSource>
            </environment>
        </environments>
    
        <!-- 指定映射配置文件的位置，映射配置文件指的是每个dao独立的配置文件 -->
        <mappers>
            <mapper resource="UserMapper.xml"/>
        </mappers>
    </configuration>

1. 创建数据库对应的实体类：由于使用了lombok插件所以简化了代码，不用写setter和getter方法以及toString方法。还可以使用注解生成空参构造和全参构造；

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
        private Long id;
        private String name;
        private Integer age;
        private String email;
        private Long managerId;
        private Date createTime;
    }

1. 编写UserMapper接口，定义里面的查询全部的抽象方法findAll;

    public interface UserMapper {
    
        List<User> findAll();
    }

1. 编写UserMapper映射配置文件：

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <!--namespace中写上对应的Mapper接口所在位置-->
    <mapper namespace="com.lyp.mapper.UserMapper">
        <!--resultType中写上对应返回值类型-->
        <select id="findAll" resultType="com.lyp.pojo.User">
            select * from user;
        </select>
    </mapper>

1. 在test中编写测试类进行测试：

    public class MybatisTest {
        @Test
        public void testFindAll() throws IOException {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(in);
            SqlSession sqlSession = factory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.findAll();
            userList.forEach(System.out::println);
        }
    }

2.3.3 Mybatis+MP实现查询User （使用Mybatis—Plus的方式）

在Mybatis中整合Mybatis-Plus插件；

1. 第一步：将UserMapper继承BaseMapper，将拥有BaseMapper中定义的所有方法：
       /**
        * 使用Mybatis-Plus的方式进行查询
        */
       public interface UserMapper  extends BaseMapper<User> {
           
       //    List<User> findAll();
       }
   
2. 第二步：使用MP中的MybatisSqlSessionFactoryBuilder进行构建：

     @Test
        public void testFindAll() throws IOException {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder builder = new MybatisSqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(in);
            SqlSession sqlSession = factory.openSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            /*由于需要查询全部并且没有查询条件只需要传递一个null即可*/
            List<User> userList = userMapper.selectList(null);
            userList.forEach(System.out::println);
        }

1. 注意事项：当我们数据库的表名称和实体类的名称不是一一对应的时候，将会报错。解决办法：使用@TableName("数据表名称") 。







2.4 Spring + Mybatis + MP

引入Spring框架，数据源、构建工作就交给了Spring管理了。

2.4.1 创建子module

1. Spring的整合就暂时没有做。。。。

---



2.5 SpringBoot + Mybatis + MP整合使用

2.5.1 创建一个SpringBoot项目

1. 创建一个SpringBoot项目



2. 选择初始装备





2.5.2 导入依赖

1. 导入相关的依赖 ：

        <!-- https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-boot-starter -->
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-boot-starter</artifactId>
                <version>3.1.1</version>
            </dependency>
    
    
            <!-- https://mvnrepository.com/artifact/junit/junit -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>4.12</version>
                <scope>test</scope>
            </dependency>
    
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>1.2.12</version>
            </dependency>



2.5.3 编写配置文件

    spring.application.name=mybatis_plus_spring_boot
    spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mybatis_plus?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&useSSL=false
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.username=root
    spring.datasource.password=root

2.5.4 编写测试类

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class MybatisSpringBootTest {
    
        @Autowired
        private UserMapper userMapper;
    
        @Test
        public void findAll() {
            List<User> userList = userMapper.selectList(null);
            userList.forEach(System.out::println);
        }
    }



---



3. 通用CRUD

在前面，了解了通过继承BaseMapper就可以获取到各种各样的单表操作，接下来就是详细的使用这些操作：



3.1 测试insert方法

1. 注意 ： 如果在设置对象的值时不设置主键id的值，但是又想设置其为主键自增的，可以使用@TableId(type = IdType.Auto)在实体类的主键id上进行标注；



1. 测试insert方法：

     @Test
        public void insertTest() {
            User user = new User();
            user.setAge(12);
            user.setCreateTime(new Date());
            user.setEmail("123456@gmail.com");
            user.setManagerId(123456L);
            user.setName("张三");
            user.setId(123456L);
            int result = userMapper.insert(user); // 返回值是一个数据库库被影响的行数
            System.out.println("result => " + result);
            System.out.println(user.getId());// 插入之后主键id会自动填充
        }

3.1 @TableField注解

在MP中通过@Table注解可以指定字段的一些属性，常常解决的问题有两个：

1. 对象的属性名称和字段名称不一致的问题（非驼峰）； @tableField(value = 数据库中的实际字段)
2. 对象的属性字段在表中不存在的问题。@tableField(exist = false)
3. 查询时不返回该字段的值@tableField(select = false)

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class User {
    
        //如果需要设置主键id增长策略
    //    @TableId(type = IdType.AUTO) // 设置主键自增
        private Long id;
        private String name;
        // 当不想将某个字段查询出来的时候可以使用
        @TableField(select = false)
        private Integer age;
        // 当实体类的属性名称与数据库的字段名称不一致的使用可以使用@TableField(value = 数据库中字段的名称)
        @TableField(value = "email")
        private String mail;
        private Long managerId;
        private Date createTime;
        // 当某个字段在数据库中不存在的时候需要进行声明
        @TableField(exist = false)
        private String address;
    }

3.2 更新操作

在MP中，更新操作有两种，一种是根据id更新，另一种是根据条件进行更新；

3.2.1 根据id进行更新

1. 方法定义：

    blic void updateByIdTest() {
            User user = new User();
            user.setId(123456L);
            user.setAge(166);
            user.setMail("123@Gmal.com");
            int result = userMapper.updateById(user);
            System.out.println("====================" + result + "==================");
        }

3.2.2 根据条件进行更新

1. 方法定义：使用对象set的方式 

    	/**
         * 根据条件进行更新
         */
        @Test
        public void updateByWrapper() {
            User user = new User();
            user.setAge(1000);
            user.setMail("1000@Gmail.com");
            QueryWrapper<User> wrapper = new QueryWrapper<User>();
            wrapper.eq("manager_id","1087982257332887553");
            int result = userMapper.update(user, wrapper);
            System.out.println(result);
        }

2. 方法定义：在条件中set数据库字段对应的值 

    	/**
         * 根据条件进行更新的操作02
         */
        @Test
        public void updateByWrapper02() {
            UpdateWrapper<User> wrapper = new UpdateWrapper<User>();
            wrapper.set("age", 100).set("email", "666888@qq.com") // 需要更新的字段
                    .eq("manager_id", "1087982257332887553");// 更新的条件
            int result = userMapper.update(null, wrapper);
            System.out.println("=======操作结果========" + result);
        }





3.2.3 根据id进行删除

     	/**
         * 根据id进行删除
         */
        @Test
        public void deleteById() {
            int result = userMapper.deleteById(12345678L);
            System.out.println("==========删除执行结果=========" + result);
        }

 3.2.4 根据map条件进行删除

     	/**
         * 根据Map条件删除数据 ， 多个条件之间使用and连接
         */
        @Test
        public void deleteByMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("manager_id", "123456");
            map.put("age", 12);
            int result = userMapper.deleteByMap(map);
            System.out.println("==========执行结果==========" + result);
        }





3.2.5 根据QueryWrapper条件进行删除

      	/**
         * 根据条件进行删除 QueryWrapper
         */
        @Test
        public void deleteByQueryWrapper() {
            // 方法一 ：
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("name", "张三").eq("id", "1234567");
            int result = userMapper.delete(wrapper);
            System.out.println("=========执行结果=========" + result);
        }



3.2.6 根据id进行批量删除

    	/**
         * 根据id进行批量的删除操作
         */
        @Test
        public void testBatchId() {
            int result = userMapper.deleteBatchIds(Arrays.asList(1234567L, 1234568L));
            System.out.println("==========执行结果============" + result);
        }



3.3 查询操作

3.3.1 根据主键id进行查询

    	/**
         * 根据主键id进行查询
         */
        @Test
        public void testSelectById() {
            User user = userMapper.selectById(123456);
            System.out.println(user);
        }

3.3.2 根据id进行批量查询

    	/**
         * 根据id进行批量查询
         */
        @Test
        public void testSelectByBatchId() {
            List<User> userList = userMapper.selectBatchIds(Arrays.asList(123456L, 1087982257332887553L));
            userList.forEach(System.out::println);
        }



3.3.3 根据条件查询一个selectOne

    	/**
         * 根据条件查询一个
         */
        @Test
        public void testSelectOne() {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("name","刘红雨");
            User user = userMapper.selectOne(wrapper);
            System.out.println(user);
        }

3.3.4  根据条件查询数据的条数

    	/**
         * 根据条件查询数据的条数
         */
        @Test
        public void testSelectCount() {
            QueryWrapper wrapper = new QueryWrapper();
            //查询用户年龄大于30岁的
            wrapper.gt("age", 30);
            Integer userCount = userMapper.selectCount(wrapper);
            System.out.println("====> UserCount ===> " + userCount);
        }



3.3.5 查询数据列表 也可以根据条件进行查询

     	 /**
         * 查询列表 可以根据条件进行查询
         */
        @Test
        public void testSelectList() {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.like("name", "张");
            List<User> userList = userMapper.selectList(wrapper);
            userList.forEach(System.out::println);
        }



3.3.6 分页查询 selectPage

1. 需要使用分页首先需要配置一个分页插件；

    /**
     * 声明一个分页插件的配置类
     */
    @Configuration
    public class PaginationInterceptor {
    
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            return new PaginationInterceptor();
        }
    }
    

1. 分页查询测试 ： 

        /**
         * 使用分页查询
         */
        @Test
        public void testSelectPage() {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.like("name", "张");
            Page<User> page = new Page<>(1, 2);
            IPage iPage = userMapper.selectPage(page, wrapper);
            long current = iPage.getCurrent();
            System.out.println("当前页 ====> " + current);
            long size = iPage.getSize();
            System.out.println("每页显示条数 === > " + size);
            long pages = iPage.getPages();
            System.out.println("总共 " + pages + "页");
            List userList = iPage.getRecords();
            userList.forEach(System.out::println);
            long total = iPage.getTotal();
            System.out.println("数据总数 == > " + total);
        }

3.4 SQL注入的原理

。。。。。



4. 配置

4.1 基本配置

4.1.1 configLocation

Mybatis配置文件位置，如果您有单独的Mybatis配置，请将其路径配置到configLocation中，MybatisConfiguration的具体内容请参考Mybatis官方文档；

SpringBoot中的配置 ： 

    # 配置Mybatis-Plus额外的配置文件 指定全局配置文件
    # mybatis-plus.config-location=classpath:mybatis-config.xml

4.1.2 MapperLocations

1. 首先创建一个文件Mapper.xml用于配置额外的sql

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE mapper
            PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
            "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
    <mapper namespace="com.lyp.mapper.UserMapper">
        <select id="findById" resultType="com.lyp.pojo.User">
            select * from user where id = #{id};
        </select>
    </mapper>

1. 在application.propertis配置文件中配置Mapper.xml文件的位置

    #classpath之后有 * 表示 可以扫描所有依赖中的classpath下的xml文件 如果没有*表示只扫描当前resources下的xml
    #指定mapper.xml文件的位置
    mybatis-plus.mapper-locations=classpath*:mybatis/*.xml

1. 在Mapper接口中新增方法findById

    User findById(Long id);

1. 编写测试用例进行测试

     /**
         * 测试自定义的findById
         */
        @Test
        public void testFindById() {
            User user = userMapper.findById(123456L);
            System.out.println(user);
        }



4.1.3 typeAliasespackage

Mybatis别名包扫描路径，通过该属性可以给包中的类注册别名，注册后在Mapper对应的xml文件中可以直接使用类名，而不用使用全限定的类名（即XML调用的时候不包含包名）。



Springboot：

    # 配置别名包扫描路径 ; 实体对象的扫描包
    mybatis-plus.type-aliases-package=com.lyp.pojo


