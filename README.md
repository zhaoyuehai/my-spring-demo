# Gradle构建的服务端项目
 [my-android-base](https://github.com/zhaoyuehai/my-android-base) 和 [my-vue-demo](https://github.com/zhaoyuehai/my-vue-demo)的服务端
***
* 用IDEA导入此项目，gradle构建。 
* Lombok 需要开启Annotation Processing编译时注解处理器：Settings > Build > Compiler > Annotation Processors>勾选Enable annotation processing。
* 运行项目根目录下的my-spring-demo.sql在你的MySQL数据库创建三张表：用户表tb_user角色、表tb_role、用户角色关系表 tb_user_role。
* 默认管理员用户： root/123456。


***
### 依赖
**SpringBoot:version = '2.1.2.RELEASE' **

**spring-boot-starter-web**

**安全框架（权限管理，访问控制）：spring-boot-starter-security**
    
**JWT（Token认证）：io.jsonwebtoken:jjwt:0.9.1**

**模板引擎（可以完全代替jsp）：spring-boot-starter-thymeleaf**

**MySQL数据库驱动：mysql:mysql-connector-java**
    
**阿里巴巴数据库连接池druid：com.alibaba:druid-spring-boot-starter**
    
**ORM（Object Relational Mapping）框架mybatis：org.mybatis.spring.boot:mybatis-spring-boot-starter**

**分页：com.github.pagehelper:pagehelper-spring-boot-starter**

**RESTful API文档：io.springfox:springfox-swagger2 + springfox-swagger-ui**

***  
### 记录

**UserController**

**HelloController**

Swagger [http://localhost/swagger-ui.html]
```
  配置类：SwaggerConfig (注意：@EnableSwagger2 开启)

  使用：
  
    方法上注解 @ApiOperation(value = "查询所有用户XXX", notes = "XXX")
    参数上注解 @ApiParam(name = "pageNum", value = "XXX", required...)
  
  访问：http://localhost/swagger-ui.html
```
mybatis+Mapper.xml 
```
  yml配置：(需要配置好数据源spring:datasource:druid)
    mybatis:
      type-aliases-package: com.yuehai.entity
      mapper-locations: classpath:mapper/*.xml
      configuration: #mybatis sql日志输出
      log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

  Application添加注解@MapperScan("com.yuehai.mapper")扫描mapper
  
  resources/mapper/添加对应的Mapper.xml文件
```
pagehelper分页：
```
yml配置：
  pagehelper:
    helper-dialect: mysql #分页插件会自动检测当前的数据库链接，自动选择合适的分页方式。
    reasonable: false #分页合理化参数，默认值为false。当该参数设置为 true 时，pageNum<=0 时会查询第一页，pageNum>pages（超过总数时），会查询最后一页。默认false 时，直接根据参数进行查询。
    support-methods-arguments: true #支持通过 Mapper 接口参数来传递分页参数，默认值false 
    #  params: 为了支持startPage(Object params)方法，增加了该参数来配置参数映射，用于从对象中根据属性名取值， 可以配置 pageNum,pageSize,count,pageSizeZero,reasonable，不配置映射的用默认值， 默认值为pageNum=pageNum;pageSize=pageSize;count=countSql;reasonable=reasonable;pageSizeZero=pageSizeZero。

使用：
  在Service层调用Dao（mapper.findXX）查询前添加代码： PageHelper.startPage(pageNum, pageSize);
```

thymeleaf 模板引擎
```
  引入依赖 org.springframework.boot:spring-boot-starter-thymeleaf
  
  在resources下建立templates文件夹放.html文件
    
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    ...
    </html>
  
```

Spring Security 安全框架（权限管理，访问控制）+ JWT

```
  引入依赖 org.springframework.boot:spring-boot-starter-security
  
  编写配置文件SecurityConfig:
  配置类添加注解：
  @Configuration
  @EnableWebSecurity
  @EnableGlobalMethodSecurity(prePostEnabled = true)//启用方法级别的权限认证

  实现方法：configure(HttpSecurity http){ ..}
           configure(AuthenticationManagerBuilder auth){..

  实现UserDetailsService接口，用于加载用户信息。 
```
