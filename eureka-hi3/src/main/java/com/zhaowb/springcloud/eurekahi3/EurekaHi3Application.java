package com.zhaowb.springcloud.eurekahi3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class EurekaHi3Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaHi3Application.class, args);
    }

}
