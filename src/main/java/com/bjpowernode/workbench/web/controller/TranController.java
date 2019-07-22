package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.domain.TranHistory;
import com.bjpowernode.workbench.service.CustomerService;
import com.bjpowernode.workbench.service.TranService;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;
import com.bjpowernode.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/19 16:57
 * <p>
 * 功能描述：
 */
public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易控制器");

        String path = request.getServletPath();
        if ("/workbench/transaction/getUserList.do".equals(path)) {

            getUserList(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)){

            detail(request,response);
        }else if("/workbench/transaction/save.do".equals(path)){

            save(request,response);
        }else if("/workbench/tran/getTranHistory.do".equals(path)){

            getTranHistory(request,response);
        }
    }

    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("展现交易历史");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> tranHistoryList = ts.getTranHistory(tranId);

        //这里除了交易历史集合，还有可能性
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for(TranHistory th : tranHistoryList){

            String stage = th.getStage();

            String possibility = pMap.get(stage);

            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response, tranHistoryList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("[交易列表][创建][保存]这里发送的是传统请求");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//前端传递的姓名，需要转换成为id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        //创建交易对象
        Tran tran = new Tran();
        tran.setType(type);
        tran.setStage(stage);
        tran.setSource(source);
        tran.setOwner(owner);
        tran.setNextContactTime(nextContactTime);
        tran.setName(name);
        tran.setMoney(money);
        tran.setId(id);
        tran.setExpectedDate(expectedDate);
        tran.setDescription(description);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setContactSummary(contactSummary);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag = ts.save(tran,customerName);

        if(flag){
            //添加成功之后返回，添加成功页，前端需要刷新列表
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }


    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //还未完成
        System.out.println("[点击交易名称][根据交易id查单条]");

        String id = request.getParameter("id");
        System.out.println(id);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran tran = ts.detail(id);
        System.out.println(tran);

        if(tran != null){

            String stage = tran.getStage();

            //注意这里使用的是id
            ServletContext application = this.getServletContext();

            Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

            String possibility = pMap.get(stage);

            //将possibility 存放到对象中，传送到前端
            tran.setPossibility(possibility);

        }

        //前端交易的详细信息页
        request.setAttribute("tran", tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("自动补全，模糊查询[只查询姓名，并以集合的形式返回]");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> uListStr = cs.getCustomerName(name);

        //因为返回值是json
        PrintJson.printJsonObj(response, uListStr);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("得到用户列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList =  us.getUserList();

        request.setAttribute("userList", userList);
        System.out.println(userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}