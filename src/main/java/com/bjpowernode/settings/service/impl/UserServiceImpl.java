package com.bjpowernode.settings.service.impl;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.settings.service.UserService;
import com.bjpowernode.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/13 19:35
 * <p>
 * 功能描述：
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        //创建集合，将集合作为参数传递到dao层
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        //如果user为空
        if(user == null){
            //抛出自定义异常
            throw new LoginException("账号或密码不正确");
        }

        String lockState = user.getLockState();
        if("0".equals(lockState)){
            //处于锁定状态
            throw new LoginException("账号已锁定");

        }

        //IP地址受限
        String allowsIps = user.getAllowIps();
        if(!allowsIps.contains(ip)){

            throw new LoginException("ip地址受限异常");
        }


        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDao.getUserList();

        return userList;
    }


}
