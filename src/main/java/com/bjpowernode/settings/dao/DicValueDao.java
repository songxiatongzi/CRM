package com.bjpowernode.settings.dao;


import com.bjpowernode.settings.domain.DicValue;

import java.util.List;

/**
 * Author: 动力节点
 * 2019/7/17
 */
public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
