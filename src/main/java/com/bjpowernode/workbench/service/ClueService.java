package com.bjpowernode.workbench.service;

import com.bjpowernode.settings.domain.User;
import com.bjpowernode.workbench.domain.Clue;
import com.bjpowernode.workbench.domain.Tran;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/17 22:52
 * <p>
 * 功能描述：
 */
public interface ClueService {

    boolean createClue(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    List<Clue> getClueList();

    boolean bund(String clueId, String[] activityIds);

    boolean convent(String clueId, Tran tran, String createBy);
}
