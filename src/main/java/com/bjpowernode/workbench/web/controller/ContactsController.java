package com.bjpowernode.workbench.web.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/21 17:55
 * <p>
 * 功能描述：
 */
public class ContactsController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String contactsPath = request.getServletPath();

        if("".equals(contactsPath)){

        }

    }
}
