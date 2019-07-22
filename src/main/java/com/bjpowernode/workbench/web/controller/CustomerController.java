package com.bjpowernode.workbench.web.controller;

import com.bjpowernode.utils.PrintJson;
import com.bjpowernode.utils.ServiceFactory;
import com.bjpowernode.vo.PaginationVo;
import com.bjpowernode.workbench.service.CustomerService;
import com.bjpowernode.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/21 17:47
 * <p>
 * 功能描述：
 */
public class CustomerController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getServletPath();

        if("/workbench/customer/pageList.do".equals(path)){

            pageList(request,response);
        }
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行customer的分页操作");

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String owner = request.getParameter("owner");
        String website = request.getParameter("website");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        int skipCount = (pageNo - 1) * pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("phone",phone);
        map.put("owner",owner);
        map.put("websize",website);
        map.put("skipCount",skipCount);
        map.put("pageSize", pageSize);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        PaginationVo vo = cs.pageList(map);

        PrintJson.printJsonObj(response, vo);

    }
}
