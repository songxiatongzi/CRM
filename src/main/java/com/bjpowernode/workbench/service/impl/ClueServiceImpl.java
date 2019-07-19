package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.settings.dao.UserDao;
import com.bjpowernode.settings.domain.User;
import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.workbench.dao.ClueDao;
import com.bjpowernode.workbench.dao.ClueRemarkDao;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.service.ClueService;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/17 22:52
 * <p>
 * 功能描述：
 */
public class ClueServiceImpl implements ClueService {

    //线索业务层
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);


    @Override
    public boolean createClue(Clue c) {

        boolean flag = true;

        int count = clueDao.createClue(c);

        if(count == 0){

            flag = false;

        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        //根据线索id 查询单条
        Clue c = clueDao.detail(id);

        return c;
    }

    @Override
    public boolean unbund(String id) {
        //根据关联关系表的id,解除关联关系

        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if(count == 0){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Clue> getClueList() {

        //查询所有的线索列表
        List<Clue> clueList = clueDao.getClueList();

        return clueList;
    }
}
