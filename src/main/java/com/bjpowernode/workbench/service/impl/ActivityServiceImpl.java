package com.bjpowernode.workbench.service.impl;


import com.bjpowernode.settings.domain.User;
import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 21:01
 * <p>
 * 功能描述：
 */
public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public boolean saveActivity(Activity activity) {

        boolean flag = true;
        //执行到数据层时返回的是0/1
        //在这里需要将布尔类型转换成为int 类型
        int count = activityDao.saveActivity(activity);

        if(count == 0){
            flag = false;
        }

        return flag;
    }
}
