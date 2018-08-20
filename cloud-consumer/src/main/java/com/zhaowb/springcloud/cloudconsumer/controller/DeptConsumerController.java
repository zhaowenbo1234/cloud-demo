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
