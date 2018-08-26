引用上一个项目，在原有的基础上进行更改，添加springcloud的内荣。

eureka-server  和上一个springcloud eureka的一样，没有改动。

添加cloud-api

```java
package com.zhaowb.springcloud.cloudapi.entities;

import java.io.Serializable;

public class Dept implements Serializable {

    private Long deptno; // 主键
    private String dname; // 部门名称
    private String db_source;// 来自那个数据库，因为微服务架构可以一个服务对应一个数据库，同一个信息被存储到不同数据库


    public Dept() {
    }

    public Dept(String dname) {
        super();
        this.dname = dname;
    }


    public Long getDeptno() {
        return deptno;
    }

    public void setDeptno(Long deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDb_source() {
        return db_source;
    }

    public void setDb_source(String db_source) {
        this.db_source = db_source;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", db_source='" + db_source + '\'' +
                '}';
    }
}
```

只需要添加这个实体类，在下边使用 。

更改 server-client ,更改为 eureka-hi

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zhawb.springcloud</groupId>
    <artifactId>eureka-hi</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>eureka-hi</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.example</groupId>
        <artifactId>cloud-demo</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>com.zhaowb.springcloud</groupId>
            <artifactId>cloud-api</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

配置application.yml

```yaml
server:
  port: 8762
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml        # mybatis配置文件所在路径
  type-aliases-package: com.zhaowb.springcloud.cloudapi.entities   # 所有Entity别名类所在包
  mapper-locations:
  - classpath:/mybatis/mapper/**/*.xml                       # mapper映射文件

spring:
  application:
    name: eureka-client-hi
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.jdbc.Driver              # mysql驱动包
    url: jdbc:mysql://localhost:3306/cloudDB01              # 数据库名称
    username: root
    password: 308539393
    dbcp2:
      min-idle: 5                                           # 数据库连接池的最小维持连接数
      initial-size: 5                                       # 初始化连接数
      max-total: 5                                          # 最大连接数
      max-wait-millis: 200                                  # 等待连接获取的最大超时时间

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    instance-id: eureka-client-hi
    prefer-ip-address: true
info:
  app.name: eureka-hi
  company.name: www.zhaowb.com
  build.artifactId: $project.artifactId$ # 这样写在idea 中运行出来不显示版本号，直接显示 $project.artifactId$ 字符串，可以自己修改
  build.version: $project.version$
```

配置mybatis

![1535273323348](/images\1535273323348.png)

目录结构如图：mybatis.cfg.xml ，今开启二级缓存

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<configuration>

    <settings>
        <setting name="cacheEnabled" value="true"/><!-- 二级缓存开启 -->
    </settings>

</configuration>
```

deptMapper.xml 操作表相关的

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.zhawb.springcloud.eurekahi.dao.DeptDao">

    <select id="findById" resultType="Dept" parameterType="Long">
      select deptno,dname,db_source from dept where deptno=#{deptno};
   </select>
    <select id="findAll" resultType="Dept">
      select deptno,dname,db_source from dept;
   </select>
    <insert id="addDept" parameterType="Dept">
      INSERT INTO dept(dname,db_source) VALUES(#{dname},DATABASE());
   </insert>

</mapper>
```

DeptController.java

```java
package com.zhawb.springcloud.eurekahi.controller;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import com.zhawb.springcloud.eurekahi.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService service;
    @Autowired
    private DiscoveryClient client;

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept) {
        return service.add(dept);
    }

    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    public Dept get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return service.list();
    }

    // @Autowired
// private DiscoveryClient client;
    @RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
    public Object discovery() {
        List<String> list = client.getServices();
        System.out.println("**********" + list);

        List<ServiceInstance> srvList = client.getInstances("MICROSERVICECLOUD-DEPT");
        for (ServiceInstance element : srvList) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.client;
    }

}
```

DeptDao.java

```java
package com.zhawb.springcloud.eurekahi.dao;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeptDao {

    boolean addDept(Dept dept);

    Dept findById(Long id);

    List<Dept> findAll();
}
```

DeptService.java

```java
package com.zhawb.springcloud.eurekahi.service;

import com.zhaowb.springcloud.cloudapi.entities.Dept;

import java.util.List;

public interface DeptService {
    
    boolean add(Dept dept);

    Dept get(Long id);

    List<Dept> list();
}
```

DeptServiceImpl.java

```java
package com.zhawb.springcloud.eurekahi.service.impl;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import com.zhawb.springcloud.eurekahi.dao.DeptDao;
import com.zhawb.springcloud.eurekahi.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao dao;

    @Override
    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    @Override
    public Dept get(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Dept> list() {
        return dao.findAll();
    }

}
```

启动类EurekaHiApplication.java

```java
package com.zhawb.springcloud.eurekahi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EurekaHiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaHiApplication.class, args);
    }
}
```

为了使用ribbon的负载均衡，将上面的eureka-hi 复制两份，分别为eureka-hi2，eureka-hi3

```yaml
server:
  port: 8764
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml        # mybatis配置文件所在路径
  type-aliases-package: com.zhaowb.springcloud.cloudapi.entities   # 所有Entity别名类所在包
  mapper-locations:
  - classpath:/mybatis/mapper/**/*.xml                       # mapper映射文件

spring:
  application:
    name: eureka-client-hi
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.jdbc.Driver              # mysql驱动包
    url: jdbc:mysql://localhost:3306/cloudDB02              # 数据库名称
    username: root
    password: 308539393
    dbcp2:
      min-idle: 5                                           # 数据库连接池的最小维持连接数
      initial-size: 5                                       # 初始化连接数
      max-total: 5                                          # 最大连接数
      max-wait-millis: 200                                  # 等待连接获取的最大超时时间

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    instance-id: eureka-client-hi2
    prefer-ip-address: true
info:
  app.name: eureka-hi3
  company.name: www.zhaowb.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```

修改端口号，数据库地址，

```yaml
spring:
  application:
    name: eureka-client-hi
```

这个一定不要修改。

eureka-hi3

```yaml
server:
  port: 8764
mybatis:
  config-location: classpath:mybatis/mybatis.cfg.xml        # mybatis配置文件所在路径
  type-aliases-package: com.zhaowb.springcloud.cloudapi.entities   # 所有Entity别名类所在包
  mapper-locations:
  - classpath:/mybatis/mapper/**/*.xml                       # mapper映射文件

spring:
  application:
    name: eureka-client-hi
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource            # 当前数据源操作类型
    driver-class-name: com.mysql.jdbc.Driver              # mysql驱动包
    url: jdbc:mysql://localhost:3306/cloudDB03              # 数据库名称
    username: root
    password: 308539393
    dbcp2:
      min-idle: 5                                           # 数据库连接池的最小维持连接数
      initial-size: 5                                       # 初始化连接数
      max-total: 5                                          # 最大连接数
      max-wait-millis: 200                                  # 等待连接获取的最大超时时间

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    instance-id: eureka-client-hi3
    prefer-ip-address: true
info:
  app.name: eureka-hi3
  company.name: www.zhaowb.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```

添加一个消费者，cloud-consumer,pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zhaowb.springcloud</groupId>
    <artifactId>cloud-consumer</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>cloud-consumer</name>
    <description>Demo project for Spring Boot</description>

    <parent>
        <groupId>com.example</groupId>
        <artifactId>cloud-demo</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <dependencies>
        <dependency>
            <groupId>com.zhaowb.springcloud</groupId>
            <artifactId>cloud-api</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

主要是添加，eureka-client 和ribbon的jar

配置 application.yml

```yaml
server:
  port: 8080

eureka:
  client:
    register-with-eureka: false
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: cloud-consumer
```

启动类CloudConsumerApplication.java

```java
package com.zhaowb.springcloud.cloudconsumer;

import com.zhaowb.springcloud.cloudconsumer.config.MyRibbonRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RibbonClient(value = "EUREKA-CLIENT-HI", configuration = MyRibbonRule.class)
public class CloudConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConsumerApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 使用ribbon 的随机算法
     * @return
     */
//    @Bean
//    public IRule myrule(){
//        return new RandomRule();
//    }


}
```

ribbon 默认的是轮询算法，如果想要用随机算法，可以使用下边那个。

DeptConsumerController.java

```java
package com.zhaowb.springcloud.cloudconsumer.controller;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class DeptConsumerController {

    //eureka-client-hi
    private static final String REST_URL_PREFIX = "http://EUREKA-CLIENT-HI";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
    }

}
```

EUREKA-CLIENT-HI  是在eureka server 注册的 application 的名字。

现在就可以在浏览器输入，http://localhost:8080/consumer/dept/list 访问，当看到"db_source":"clouddb02"字段改变，就说明成功了。

自定义ribbon 算法：MyRibbonRule.java 依然是轮训算法，不过更改为了每5次更改一次。

```java
package com.zhaowb.springcloud.cloudconsumer.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;


public class MyRibbonRule extends AbstractLoadBalancerRule {
    // total = 0 // 当total==5以后，我们指针才能往下走，
    // index = 0 // 当前对外提供服务的服务器地址，
    // total需要重新置为零，但是已经达到过一个5次，我们的index = 1

    private int total = 0;            // 总共被调用的次数，目前要求每台被调用5次
    private int currentIndex = 0;    // 当前提供服务的机器号

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                /*
                 * No servers. End regardless of pass, because subsequent passes only get more
                 * restrictive.
                 */
                return null;
            }

//       int index = rand.nextInt(serverCount);// java.util.Random().nextInt(3);
//       server = upList.get(index);


//       private int total = 0;           // 总共被调用的次数，目前要求每台被调用5次
//       private int currentIndex = 0;  // 当前提供服务的机器号
            if (total < 5) {
                server = upList.get(currentIndex);
                total++;
            } else {
                total = 0;
                currentIndex++;
                if (currentIndex >= upList.size()) {
                    currentIndex = 0;
                }
            }


            if (server == null) {
                /*
                 * The only time this should happen is if the server list were somehow trimmed.
                 * This is a transient condition. Retry after yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }
        return server;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // TODO Auto-generated method stub

    }	
}
```

在启动类上添加 ，`@RibbonClient(value = "EUREKA-CLIENT-HI", configuration = MyRibbonRule.class)`

就可以使用自定义算法了，value 是使用在那个微服务上 ，configuration  是自定义的类。

当访问的时候看到刷新5次，db_source"字段 更改一次，说明自定义的算法成功了。