package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int saveTranHistory(TranHistory tranHistory);

    List<TranHistory> getTranHistory(String tranId);

}
