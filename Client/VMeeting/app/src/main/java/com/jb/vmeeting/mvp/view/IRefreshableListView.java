package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.Page;

/**
 * Created by Jianbin on 2016/4/21.
 */
public interface IRefreshableListView<T> {

    void onRefreshSuccess(Page<T> page);

    void onRefreshFailed(int code, String message);

    void onRefreshFinish();

    void onLoadMoreSuccess(Page<T> page);

    void onLoadMoreFailed(int code, String message);

    void onLoadMoreFinish();
}
