package com.bjpowernode.settings.web.controller;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.settings.service.impl.UserServiceImpl;
import com.bjpowernode.utils.MD5Util;
import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/13 19:42
 * <p>
 * 功能描述：
 */
public class UserController extends HttpServlet {

    //用户控制层
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        //获取servlet路径名称
        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){
            //用户登陆页面

                login(request,response);


        }else if("/*.do".equals(path)){


        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)  {

        System.out.println("进入到用户登陆控制层");
        String loginAct = request.getParameter("loginAct");
        String loginPassword = request.getParameter("loginPwd");
        //将登陆密码转换成密文MD5
        String loginPwd = MD5Util.getMD5(loginPassword);
        String ip = request.getRemoteAddr();

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        //通过业务层来调取方法
        try {
            User user = userService.login(loginAct,loginPwd,ip);
            //将user放到全局域对象中
            request.getSession().setAttribute("user", user);
            //将json相应到前端
            PrintJson.printJsonFlag(response, true);

        } catch (LoginException e) {
            e.printStackTrace();

            //得到错误信息
            String msg = e.getMessage();

            //创建集合，将错误信息和false 写入到响应体中
            Map<String,Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg", msg);

            //将集合写入到响应体
            PrintJson.printJsonObj(response, map);

        }


    }
}
