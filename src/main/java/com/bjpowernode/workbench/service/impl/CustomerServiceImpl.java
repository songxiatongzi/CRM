package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.workbench.domain.Customer;
import com.bjpowernode.workbench.service.CustomerService;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/19 20:27
 * <p>
 * 功能描述：
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);


    @Override
    public List<String> getCustomerName(String name) {

        List<String> userListStr = customerDao.getCustomerName(name);

        return userListStr;
    }

    @Override
    public PaginationVo pageList(Map<String, Object> map) {

        //查询总条数
        int total = customerDao.getCustomerTotal(map);
        //查询列表
        List<Customer> customerList = customerDao.getCustomerList(map);
        //封装对象
        PaginationVo<Customer> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(customerList);
        //将封装好的对象进行返回
        return vo;
    }
}
