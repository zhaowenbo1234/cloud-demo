package com.zhaowb.springcloud.eurekahi2.service;

import com.zhaowb.springcloud.cloudapi.entities.Dept;

import java.util.List;

public interface DeptService {


    boolean add(Dept dept);

    Dept get(Long id);

    List<Dept> list();
}
