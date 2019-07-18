package com.bjpowernode.web.filter;

import com.bjpowernode.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 17:42
 * <p>
 * 功能描述：
 */
public class LoginFilter implements Filter {

    //这里实现了过滤器的接口
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //对地址栏中的输入的参数进行拦截
        System.out.println("进入到是否登录的过滤器");

        //将servletRequest 转换成为 请求作用域
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String path = request.getServletPath();
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            //对登录进行放行
            filterChain.doFilter(servletRequest, servletResponse);


        }else{
            //登录失败，不对登录进行放行
            User user = (User) request.getSession().getAttribute("user");

            //判断这个user 是不是为null
            if(user != null){
                //放行
                filterChain.doFilter(servletRequest, servletResponse);

            }else{
                //重定向到登陆页
                //项目名称/具体资源名称
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }



    }
}
