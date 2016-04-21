package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;
import com.jb.vmeeting.tools.L;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class PageInfo extends BaseEntity implements Cloneable{
    @Expose
    private boolean hasNext = false; // 是否有下一页

    @Expose
    private int curPage = 0; // 当前页码

    @Expose
    private int perPageDataNum = 0; // 每页数据的数量

    @Expose
    private int totalDataNum = 0; // 总共的数据数量

    @Expose
    private int totalPageNum = 0; // 总共有多少页

    @Expose
    private int totalDataNumCurPage = 0; // 到此页为止(包括此页)一共的数据数量

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getPerPageDataNum() {
        return perPageDataNum;
    }

    public void setPerPageDataNum(int perPageDataNum) {
        this.perPageDataNum = perPageDataNum;
    }

    public int getTotalDataNum() {
        return totalDataNum;
    }

    public void setTotalDataNum(int totalDataNum) {
        this.totalDataNum = totalDataNum;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public int getTotalDataNumCurPage() {
        return totalDataNumCurPage;
    }

    public void setTotalDataNumCurPage(int totalDataNumCurPage) {
        this.totalDataNumCurPage = totalDataNumCurPage;
    }

    public PageInfo clone() {
        PageInfo pageInfo = null;
        try {
            pageInfo = (PageInfo) super.clone();
        } catch (CloneNotSupportedException e) {
            L.e(e);
        }
        return pageInfo;
    }
}