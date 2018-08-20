package com.zhawb.springcloud.eurekahi.service.impl;

import com.zhaowb.springcloud.cloudapi.entities.Dept;
import com.zhawb.springcloud.eurekahi.dao.DeptDao;
import com.zhawb.springcloud.eurekahi.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao dao;

    @Override
    public boolean add(Dept dept) {
        return dao.addDept(dept);
    }

    @Override
    public Dept get(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Dept> list() {
        return dao.findAll();
    }

}
