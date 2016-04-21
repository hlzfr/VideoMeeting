package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class Page<T> extends BaseEntity {
    @Expose
    private PageInfo pageInfo; // 当前页面信息

    @Expose
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
