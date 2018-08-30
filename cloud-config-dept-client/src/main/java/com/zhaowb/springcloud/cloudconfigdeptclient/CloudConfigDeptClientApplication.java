package com.zhaowb.springcloud.cloudconfigdeptclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class CloudConfigDeptClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigDeptClientApplication.class, args);
    }
}
