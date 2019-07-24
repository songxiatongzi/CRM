package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int deleteClueRemarkByClueId(String clueId);

}
