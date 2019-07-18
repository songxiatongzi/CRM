package com.bjpowernode.web.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/15 16:53
 * <p>
 * 功能描述：
 */
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("进入到字符编码过滤器");

        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html;charset=utf-8");

        //将请求和响应写入到链条对象中
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
