package com.bjpowernode.workbench.dao;


import com.bjpowernode.workbench.domain.ClueActivityRelation;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation clueActivityRelation);

}
