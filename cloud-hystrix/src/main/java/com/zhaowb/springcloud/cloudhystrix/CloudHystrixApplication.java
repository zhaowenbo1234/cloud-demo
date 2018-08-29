package com.zhaowb.springcloud.cloudhystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
@EnableHystrix
@EnableCircuitBreaker
public class CloudHystrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudHystrixApplication.class, args);
    }

    // 解决查找不到actuator/hystrix.stream 解决办法，可以通过配置 yml 解决，
//    @Bean
//    public ServletRegistrationBean getServlet(){
//
//        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
//
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
//
//        registrationBean.setLoadOnStartup(1);
//
//        registrationBean.addUrlMappings("/actuator/hystrix.stream");
//
//        registrationBean.setName("HystrixMetricsStreamServlet");
//
//
//        return registrationBean;
//    }

}