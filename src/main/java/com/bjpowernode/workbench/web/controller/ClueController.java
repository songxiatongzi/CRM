package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Activity;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.service.ActivityService;
import com.bjpowernode.workbench.service.ClueService;
import com.bjpowernode.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;
import com.bjpowernode.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/17 22:58
 * <p>
 * 功能描述：
 */
public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");

        String path = request.getServletPath();
        if("/workbench/clue/getUserList.do".equals(path)){

            //查询所有用户
            getUserList(request,response);
        } else if("/workbench/clue/createClue.do".equals(path)){

            createClue(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){

            detail(request,response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){

            getActivityListByClueId(request,response);
        }else if("/workbench/clue/unbund.do".equals(path)){

            unbund(request,response);
        }else if("/workbench/clue/getActivityListByNameNoByCLueId.do".equals(path)){

            getActivityListByNameNoByCLueId(request, response);
        }else if("/workbench/clue/getClueList.do".equals(path)){

            getClueList(request,response);
        }
    }

    private void getClueList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询线索列表中");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        List<Clue> clueList = cs.getClueList();

        PrintJson.printJsonObj(response, clueList);
    }

    private void getActivityListByNameNoByCLueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("管理市场活动模态窗口进行查询");

        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<>();
        map.put("name", name);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList = as.getActivityListByNameNoByCLueId(map);

        PrintJson.printJsonObj(response, aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("解除市场关联关系");

        //删除第三张表
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据线索id查询市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService ac = (ActivityService) ServiceFactory.getService( new ActivityServiceImpl());

        List<Activity> aList = ac.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response, aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到线索的详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void createClue(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("创建线索");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setWebsite(website);
        c.setState(state);
        c.setSource(source);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setId(id);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setCompany(company);
        c.setAppellation(appellation);
        c.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.createClue(c);

        Map<String,Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("c", c);

        PrintJson.printJsonObj(response, map);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("线索创建模块，返回userList填充到用户的下拉框中");

        UserService cs = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = cs.getUserList();

        PrintJson.printJsonObj(response, userList);
    }
}
