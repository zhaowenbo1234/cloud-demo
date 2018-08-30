新建model ，cloud-zuul添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.zhaowb.springcloud</groupId>
    <artifactId>cloud-zuul</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>cloud-zuul</name>
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
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
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

添加配置application.yml

```yml
server:
  port: 8768

spring:
  application:
    name: cloud-zuul

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
  instance:
    instance-id: cloud-zuul
    prefer-ip-address: true

info:
  app.name: zhaowb-cloud-demo
  company.name: www.zhaowb.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
zuul:
  routes:
    myDept:
      serviceID: eureka-client-hi
      path: /myDept/**
```

在启动类上加上注解@EnableZuulProxy开启zuul的功能。

```java
package com.zhaowb.springcloud.cloudzuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableDiscoveryClient
public class CloudZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudZuulApplication.class, args);
    }
}
```

启动eureka-server，eureka-hi，cloud-zuul，在浏览器输入http://localhost:8762/dept/list，显示

```json
[{"deptno":1,"dname":"部门1","db_source":"clouddb01"},{"deptno":2,"dname":"部门2","db_source":"clouddb01"},{"deptno":3,"dname":"部门3","db_source":"clouddb01"},{"deptno":4,"dname":"部门4","db_source":"clouddb01"},{"deptno":5,"dname":"部门5","db_source":"clouddb01"},{"deptno":6,"dname":"部门6","db_source":"clouddb01"}]
```

说明eureka-hi，启动成功，然后输入http://localhost:8768/myDept/dept/list，也显示同样的信息说明验证成功，但是输入http://localhost:8768/eureka-client-hi/dept/list，也能正常显示，感觉不是很好，设置忽略微服务名登录,增加前缀

```yaml
zuul:
  ignored-services: "*" # 忽略所有微服务不能以原有微服务名访问
  prefix: /zhaowb   # 在访问前加前缀
  routes:
    myDept:
      serviceID: eureka-client-hi
      path: /myDept/**
```

这样微服务名就不能使用了，加入前缀，http://localhost:8768/zhaowb/myDept/dept/list才能正常使用。

[码云地址](https://gitee.com/JiShiMoWang/cloud-demo)

[GitHub地址](https://github.com/zhaowenbo1234/cloud-demo)