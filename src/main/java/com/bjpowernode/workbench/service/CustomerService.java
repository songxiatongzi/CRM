package com.bjpowernode.workbench.service;

import com.bjpowernode.vo.PaginationVo;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/19 20:27
 * <p>
 * 功能描述：
 */
public interface CustomerService {
    List<String> getCustomerName(String name);

    PaginationVo pageList(Map<String, Object> map);
}
