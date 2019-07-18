package com.bjpowernode.workbench.service;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 21:00
 * <p>
 * 功能描述：
 */
public interface ActivityService {


    boolean saveActivity(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delBySelId(String[] ids);

    Activity detail(String id);

    List<ActivityRemark> showRemarkListById(String activityId);

    boolean deleteRemarkById(String id);

    boolean updateRemark(ActivityRemark ar);

    boolean saveRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);
}
