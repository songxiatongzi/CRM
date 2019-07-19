package com.bjpowernode.web.listener;

import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.settings.service.DicService;
import com.bjpowernode.settings.service.impl.DicServiceImpl;
import com.bjpowernode.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/18 8:45
 * <p>
 * 功能描述：
 */
public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("进入到服务器缓存中处理数据字典");
        /*
             先把字典的键和值查询出来存放到map中
             在从map中将键和值取出来存放到application 中
             当服务器启动的时候，系统会自动执行该方法
         */

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        //先根据ds查询所有的map集合，并得到所有的key,再根据key中获取value 中的值
        Map<String, List<DicValue>> map  = ds.getAll();

        //将map中的键取出来
        Set<String> set = map.keySet();

        //遍历set
        for(String dicType: set){

            //根据字典的属性值查询所在集合的 value 值
            application.setAttribute(dicType, map.get(dicType));

        }

        System.out.println("数据字典处理完成");


        //将阶段和可能性从properties中取出，封装到对象中
        Map<String,String> pMap = new HashMap<>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        
        Enumeration<String> keys = rb.getKeys();
        
        while (keys.hasMoreElements()){

            String stage = keys.nextElement();

            String possibility = rb.getString(stage);

            //将键和值封装到集合中map
            pMap.put(stage, possibility);
        }

        //封装完成，将map写入到上下文域对象中
        application.setAttribute("pMap", pMap);

    }
}
