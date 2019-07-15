package com.bjpowernode.settings.service;

import com.bjpowernode.exception.LoginException;
import com.bjpowernode.settings.domain.User;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/13 19:36
 * <p>
 * 功能描述：
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    //用户业务层
}
