package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int saveTran(Tran tran);

    Tran detail(String id);

    int getTotal();

    List<Map<String, Object>> getTranChartsList();

}
