package com.jb.vmeeting.mvp.presenter.refreshlist;

import com.jb.vmeeting.app.constant.NETCODE;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.PageInfo;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.presenter.BasePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;

/**
 * Created by Jianbin on 2016/4/21.
 */
public abstract class BaseRefreshablePresenter<T> extends BasePresenter implements RefreshablePresenter {
    IRefreshableListView<T> mView;
    PageInfo curPage;

    boolean isLoading = false;

    public BaseRefreshablePresenter(IRefreshableListView<T> view) {
        setRefreshableView(view);
    }

    public void setRefreshableView(IRefreshableListView<T> view) {
        mView = view;
    }

    public IRefreshableListView<T> getView() {
        return mView;
    }

    @Override
    public void refresh() {
        if (isLoading) {
            return;
        }
        isLoading = true;
        refreshData();
    }

    @Override
    public void loadMore(int page, int limit) {
        if (!canLoadMore()) {
            if (mView != null) {
                mView.onLoadMoreFailed(NETCODE.STATUS.ERR_LOCAL, NETCODE.RESULT.ERR_LOCAL, "没有更多");
                mView.onLoadMoreFinish();
            }
            return;
        }
        isLoading = true;
        loadMoreData(page, limit);
    }

    public abstract void refreshData();
    public abstract void loadMoreData(int page, int limit);

    public void refreshSuccess(Page<T> page) {
        isLoading = false;
        curPage = page.getPageInfo();
        if (mView != null) {
            mView.onRefreshSuccess(page);
            mView.onRefreshFinish();
        }
    }

    public void refreshFailed(int statusCode, int code, String message) {
        isLoading = false;
        if (mView != null) {
            mView.onRefreshFailed(statusCode, code, message);
            mView.onRefreshFinish();
        }
    }

    public void loadMoreSuccess(Page<T> page) {
        isLoading = false;
        curPage = page.getPageInfo();
        if (mView != null) {
            mView.onLoadMoreSuccess(page);
            mView.onLoadMoreFinish();
        }
    }

    public void loadMoreFailed(int statusCode, int code, String message) {
        isLoading = false;
        if (mView != null) {
            mView.onLoadMoreFailed(statusCode, code, message);
            mView.onLoadMoreFinish();
        }
    }

    public boolean canLoadMore() {
        return curPage != null && curPage.isHasNext() && !isLoading;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
