package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.workbench.service.CustomerService;
import com.bjpowernode.workbench.service.impl.ClueServiceImpl;
import com.bjpowernode.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

            save(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(request,response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("自动补全，模糊查询[只查询姓名，并以集合的形式返回]");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> uListStr = cs.getCustomerName(name);

        //因为返回值是json
        PrintJson.printJsonObj(response, uListStr);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("得到用户列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList =  us.getUserList();

        request.setAttribute("userList", userList);
        System.out.println(userList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}