package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.service.CustomerService;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/19 20:27
 * <p>
 * 功能描述：
 */
public class CustomerServiceImpl implements CustomerService {

    CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {

        List<String> userListStr = customerDao.getCustomerName(name);

        return userListStr;
    }
}
