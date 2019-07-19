package com.bjpowernode.workbench.dao;


import com.bjpowernode.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {


    int createClue(Clue c);

    Clue detail(String id);

    List<Clue> getClueList();

}
