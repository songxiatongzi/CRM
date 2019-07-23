package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/22 11:58
 * <p>
 * 功能描述：
 */
public interface TranService {
    Tran detail(String id);

    boolean save(Tran tran, String customerName);

    List<TranHistory> getTranHistory(String tranId);

    Map<String, Object> getTranCharts();


}
