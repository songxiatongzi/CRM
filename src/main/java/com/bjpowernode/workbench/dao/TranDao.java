package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Tran;

public interface TranDao {

    int saveTran(Tran tran);

    Tran detail(String id);

}
