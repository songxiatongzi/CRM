package com.bjpowernode.workbench.service.impl;


import com.bjpowernode.settings.domain.User;
import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.dao.ActivityDao;
import com.bjpowernode.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;
import jdk.management.resource.internal.TotalResourceContext;

import java.util.List;
import java.util.Map;

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

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {

        //业务层有两项任务
        //1.查询总条数  需要两张表联查(返回总条数)
        int total = activityDao.selectActivityTotal(map);
        System.out.println("total = " + total);
        //2.查询所有的市场活动列标 (两张表联查，返回数据列表)
        List<Activity> dataList = activityDao.selectActivityList(map);
        //3.将总条数和市场活动列表封装到vo对象中
        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        //4.返回vo对象
        return vo;
    }

    @Override
    public boolean delBySelId(String[] ids) {
        System.out.println("进入到删除市场活动业务层进行操作");
        boolean flag = true;

        //查询出需要删除的备注的数量
        //通过外键进行删除
        int count1 =activityRemarkDao.getCountByIds(ids);

        //实际删除的数量
        int count2 = activityRemarkDao.deleteRemark(ids);

        if(count1 != count2 ){
            flag = false;

        }

        //删除完成remark备注，在删除市场活动【一对多的关系，要先删除多，在删除一】
        int count3 = activityDao.delBySelId(ids);

        if(count3 != ids.length){
            flag = false;
        }

        return flag ;
    }
}















