package com.bjpowernode.settings.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/13 19:42
 * <p>
 * 功能描述：
 */
public class UserController extends HttpServlet {

    //用户控制层
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        if("/settings/user/xxx.do".equals(path)){

            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到用户登陆控制层");


    }
}
