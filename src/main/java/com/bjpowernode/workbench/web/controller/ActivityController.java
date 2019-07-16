package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 20:52
 * <p>
 * 功能描述：
 */
public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();
        if("/workbench/activity/getUserList.do".equals(path)){

            //查询所有用户
            getUserList(request,response);
        }else if ("/workbench/activity/saveActivity.do".equals(path)){

            //添加市场活动
            saveActivity(request,response);
        }

    }

    private void saveActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到添加市场活动模块");

        //通过前端得到所有信息
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        //从当前域对象中获取
        String createBy = ((User) request.getSession().getAttribute("user")).getName();

        //将所有数据封装到对象中
        Activity activity = new Activity();
        activity.setStartDate(startDate);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setId(id);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        activity.setCost(cost);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = activityService.saveActivity(activity);

        //写入到json对象中
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询所有用户");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList =  userService.getUserList();

        //将userList 写入到json中
        PrintJson.printJsonObj(response, userList);
    }
}
