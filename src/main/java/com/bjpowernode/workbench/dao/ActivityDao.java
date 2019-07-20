package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 19:27
 * <p>
 * 功能描述：
 */
public interface ActivityDao {
    int saveActivity(Activity activity);


    int selectActivityTotal(Map<String, Object> map);

    List<Activity> selectActivityList(Map<String, Object> map);

    int delBySelId(String[] ids);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameNoByCLueId(Map<String, String> map);

    List<Activity> getActivityListByName(String activityName);
}
