package com.bjpowernode.settings.dao;

import com.bjpowernode.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/13 19:25
 * <p>
 * 功能描述：
 */
public interface UserDao {


    User login(Map<String, Object> map);

    List<User> getUserList();

}
