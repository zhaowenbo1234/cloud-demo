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