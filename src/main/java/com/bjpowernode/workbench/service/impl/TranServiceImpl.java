package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.utils.DateTimeUtil;
import com.bjpowernode.utils.SqlSessionUtil;
import com.bjpowernode.utils.UUIDUtil;
import com.bjpowernode.workbench.dao.CustomerDao;
import com.bjpowernode.workbench.dao.CustomerRemarkDao;
import com.bjpowernode.workbench.dao.TranDao;
import com.bjpowernode.workbench.dao.TranHistoryDao;
import com.bjpowernode.workbench.domain.Customer;
import com.bjpowernode.workbench.domain.Tran;
import com.bjpowernode.workbench.domain.TranHistory;
import com.bjpowernode.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/22 11:58
 * <p>
 * 功能描述：
 */
public class TranServiceImpl implements TranService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public Tran detail(String id) {
        //暂时还没做

        Tran tran = tranDao.detail(id);

        return tran;
    }

    @Override
    public boolean save(Tran tran, String customerName) {

        boolean flag = true;
        //创建交易的同时，需要判断客户是不是存在
        Customer c = customerDao.getCustomerByName(customerName);

        //如果客户不存在，创建一个客户，在添加一笔交易
        if(c == null){
            //创建一个客户
            c = new Customer();
            c.setId(UUIDUtil.getUUID());
            c.setName(customerName);
            c.setCreateTime(DateTimeUtil.getSysTime());
            c.setCreateBy(tran.getCreateBy());

            //保存客户
            int count  = customerDao.saveCustomer(c);
            if(count == 0){
                flag = false;
            }

        }

        //创建一笔交易
        tran.setCustomerId(c.getId());
        int count1 =  tranDao.saveTran(tran);

        if(count1 == 0){
            flag = false;
        }

        //创建完交易，在创建一个交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());

        //添加交易历史
        int count2 =  tranHistoryDao.saveTranHistory(tranHistory);
        if(count2 == 0 ){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<TranHistory> getTranHistory(String tranId) {

        List<TranHistory> tranHistoryList = tranHistoryDao.getTranHistory(tranId);

        return tranHistoryList;

    }

    @Override
    public Map<String, Object> getTranCharts() {

        //获得交易的总条数
        int total = tranDao.getTotal();
        //获得交易的列表
        //通过stage分组， 得到每个stage 下的数量和对应的stage
        List<Map<String,Object>> dataList = tranDao.getTranChartsList();

        Map<String,Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList",dataList);

        return map;
    }
}
