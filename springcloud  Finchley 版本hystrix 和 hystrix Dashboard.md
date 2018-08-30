hystrix的断路功能

引用上个项目，创建新的model ，cloud-hystrix

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zhaowb.springcloud</groupId>
    <artifactId>cloud-hystrix</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>cloud-hystrix</name>
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
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.10</version>
        </dependency>

        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis.spring.boot/mybatis-spring-boot-starter -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
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

主要的依赖是

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

在启动类上加上注解

```java
package com.zhaowb.springcloud.cloudhystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableHystrix
public class CloudHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHystrixApplication.class, args);
    }
}
```

然后将eureka-hi 的 controller ，dao，service，resource 下mybatis相关 复制过来，修改DeptMapper.xml中的namespace 为当前项目下的Deptdao，这个一定要修改，否则会报找不到该类下的方法，修改controller

```java
package com.zhaowb.springcloud.cloudhystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zhaowb.springcloud.cloudapi.entities.Dept;
import com.zhaowb.springcloud.cloudhystrix.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {

    @Autowired
    private DeptService service;

    @RequestMapping(value = "/dept/add", method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept) {
        return service.add(dept);
    }

    @RequestMapping(value = "/dept/get/{id}", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "getDeptError")
    public Dept get(@PathVariable("id") Long id) {
        Dept dept = service.get(id);
        if (dept == null){
            throw new RuntimeException("dept is null id =" + id);
        }
        return service.get(id);
    }

    public Dept getDeptError(@PathVariable("id") Long id){
        Dept dept = new Dept();
        dept.setDeptno(id);
        dept.setDname("该ID：" + id + "没有没有对应的信息,null--@HystrixCommand");
        dept.setDb_source("no this database in MySQL");
        return dept;
    }

    @RequestMapping(value = "/dept/list", method = RequestMethod.GET)
    public List<Dept> list() {
        return service.list();
    }
}
```

代码已经完成，启动eureka-server ,cloud-hystrix,在浏览器输入http://localhost:8766/dept/get/1，

显示查询结果，eptno":1,"dname":"部门1","db_source":"clouddb01"}，

然后在浏览器输入http://localhost:8766/dept/get/555，查询结果为{"deptno":555,"dname":"该ID：555没有没有对应的信息,null--@HystrixCommand","db_source":"no this database in MySQL"}。说明hystrix的断路功能实现成功。我数据库只有几条数据，555为在数据库没有该id的任意数字都可。

hystrix 的fallbackFactory实现

修改cloud-api ,类

```java
package com.zhaowb.springcloud.cloudapi.service;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import feign.hystrix.FallbackFactory;

import java.util.List;

@Component
public class IDeptClientServiceFallbackFactory implements FallbackFactory<DeptClientService> {

    private static final Logger logger = LoggerFactory.getLogger(IDeptClientServiceFallbackFactory.class);

    @Override
    public DeptClientService create(Throwable cause) {
        return new DeptClientService() {
            @Override
            public Dept get(long id) {

                logger.info("DeptClientServiceFallbackFactory.DeptClientService.get id= " + id);
                logger.info("错误信息：" + cause);
                Dept dept = new Dept();
                dept.setDeptno(id);
                dept.setDname("该ID：" + id + "没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭");
                dept.setDb_source("no this database in MySQL");
                return dept;
            }

            @Override
            public List<Dept> list() {
                return null;
            }

            @Override
            public boolean add(Dept dept) {
                return false;
            }
        };
    }
}
```

@Component 必须要有。

在DeptClientService增加

```java
@FeignClient(value = "eureka-client-hi", fallbackFactory = IDeptClientServiceFallbackFactory.class)
```

修改cloud-feign,增加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

Feign是自带断路器的，在D版本的Spring Cloud之后，它没有默认打开。需要在配置文件中配置打开它，修改配置文件，application.yml

```yaml
feign:
  hystrix:
    enabled: true
```

这样基本上可以了，如果依然扫描不到包，怎添加上下边这个注解

```java
@ComponentScan(basePackages = {"com.zhaowb.springcloud.servicefeign","com.zhaowb.springcloud.cloudapi.service"}) // 参考https://my.oschina.net/u/2948566/blog/1591147
```

这样扫描就可以了，如果能扫描到就不需要添加。我是遇到了扫描不到的情况，需要添加。

然后启动，eureka-server，eureka-hi，cloud-feign，在浏览器输入http://localhost:8765//consumer/dept/get/1，能正常访问，然后关闭eureka-hi，刷新浏览器，

```json
{"deptno":1,"dname":"该ID：1没有没有对应的信息,Consumer客户端提供的降级信息,此刻服务Provider已经关闭","db_source":"no this database in MySQL"}
```

验证成功。

## Hystrix Dashboard监控

在微服务架构中为例保证程序的可用性，防止程序出错导致网络阻塞，出现了断路器模型。断路器的状况反应了一个程序的可用性和健壮性，它是一个重要指标。Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面。

新建model，cloud-hystrix-dashboard，引入依赖，有人说spring-boot-starter-actuato也是必须引用的 ，但是我没有引入，也能运行，就没有引入。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zhaowb.springcloud</groupId>
    <artifactId>cloud-hystrix-dashboard</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>cloud-hystrix-dashboard</name>
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
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

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
  port: 8767

management:
  endpoints:
    web:
      exposure:
        include: hystrix.stream
```

在启动类上加上@EnableHystrixDashboard开启HystrixDashboard

```java
package com.zhaowb.springcloud.cloudhystrixdashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class CloudHystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHystrixDashboardApplication.class, args);
    }
}
```

修改cloud-hystrix的启动类，在启动类上CloudHystrixApplication添加@EnableHystrix注解开启断路器。

依次启动eureka-server，cloud-hystrix，cloud-hystrix-dashboard，打开,http://localhost:8766/dept/get/1，

http://localhost:8766/actuator/hystrix.stream 会有ping: data:{...} 

打开locahost:8767/hystrix会有一个豪猪的图片显示说明成功。在界面一次填入http://localhost:8766/actuator/hystrix.stream ，2000，hellokity,分别为，监控的微服务的hystrix地址，延迟时间毫秒，title，点击Monitor Stream ，刷新http://localhost:8766/dept/get/1，就会有图像页面显示。

[码云地址](https://gitee.com/JiShiMoWang/cloud-demo)

[GitHub地址](https://github.com/zhaowenbo1234/cloud-demo)