package com.bjpowernode.settings.service.impl;

import com.bjpowernode.settings.dao.DicTypeDao;
import com.bjpowernode.settings.dao.DicValueDao;
import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.settings.service.DicService;
import com.bjpowernode.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/18 10:08
 * <p>
 * 功能描述：
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);

    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map = new HashMap<>();

        //查询出所有的字典类型
        List<DicType> typeList = dicTypeDao.getTypeList();

        //遍历集合查询所有的dic_value
        for(DicType dicType : typeList){

            //通过每条key来获取code的属性
            String code = dicType.getCode();

            List<DicValue> DVList = dicValueDao.getValueListByCode(code);

            //将遍历出来的值封装到集合中
            map.put(code, DVList);

        }
        return map;
    }

    //一个字典业务可以调用多个dao层处理业务，监听器是通过监听器调用使用






}
