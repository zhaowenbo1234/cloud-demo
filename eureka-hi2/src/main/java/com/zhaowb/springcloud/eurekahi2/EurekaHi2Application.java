package com.zhaowb.springcloud.eurekahi2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaHi2Application {

    public static void main(String[] args) {
        SpringApplication.run(EurekaHi2Application.class, args);
    }

}
