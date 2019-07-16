package com.bjpowernode.vo;

import java.util.List;

/**
 * Auther: 董怀宾_bjpowernode
 * Date: 2019/7/16 13:16
 * <p>
 * 功能描述：
 */
public class PaginationVo<T> {

    /**
     * tatal:根据后台查询出来的总数
     * dataList:返回的所有的查询信息
     *
     */
    private int total;
    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public PaginationVo<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public PaginationVo<T> setDataList(List<T> dataList) {
        this.dataList = dataList;
        return this;
    }

    @Override
    public String toString() {
        return "PaginationVo{" +
                "total=" + total +
                ", dataList=" + dataList +
                '}';
    }
}
