package com.bjpowernode.workbench.dao;


import com.bjpowernode.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation clueActivityRelation);

    List<ClueActivityRelation> getActivityListByClueId(String clueId);

    int delete(String clueId);

}
