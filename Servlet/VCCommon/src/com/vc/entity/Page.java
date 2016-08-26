package com.vc.entity;

import java.util.List;

public class Page<T> {
    private PageInfo pageInfo; // 当前页面信息

    private List<T> data; // 当前页面的数据列表

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
