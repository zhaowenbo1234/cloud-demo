package com.zhaowb.springcloud.servicefeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.zhaowb.springcloud.cloudapi")//扫描带@FeignClient(value = "EUREKA-CLIENT-HI") 注解的包
@ComponentScan(basePackages = {"com.zhaowb.springcloud.servicefeign","com.zhaowb.springcloud.cloudapi.service"}) // 参考https://my.oschina.net/u/2948566/blog/1591147
public class ServiceFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFeignApplication.class, args);
    }
}
