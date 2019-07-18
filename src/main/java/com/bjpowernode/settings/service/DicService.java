package com.bjpowernode.settings.service;

import com.bjpowernode.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/18 10:08
 * <p>
 * 功能描述：
 */
public interface DicService {

    Map<String, List<DicValue>> getAll();


}
