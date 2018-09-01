package com.zhaowb.springcloud.cloudconfigeurekaserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaServer
@RestController
public class CloudConfigEurekaServerApplication {

    @Value("${file.name:mojhap}")
    private String fileName;

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigEurekaServerApplication.class, args);
    }

    @RequestMapping(value = "/getFileName", method = RequestMethod.GET)
    public String getFileName() {
        return fileName;
    }


}
