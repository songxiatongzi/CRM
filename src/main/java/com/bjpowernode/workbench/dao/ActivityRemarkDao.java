package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 19:27
 * <p>
 * 功能描述：
 */
public interface ActivityRemarkDao {

    int getCountByIds(String[] ids);

    int deleteRemark(String[] ids);

    List<ActivityRemark> showRemarkListById(String activityId);

    boolean deleteRemarkById(String id);

    int updateRemark(ActivityRemark ar);

    int saveRemark(ActivityRemark ar);
}
