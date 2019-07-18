package com.bjpowernode.workbench.dao;


import com.bjpowernode.workbench.domain.Clue;

public interface ClueDao {


    int createClue(Clue c);

    Clue detail(String id);
}
