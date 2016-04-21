package com.jb.vmeeting.mvp.presenter.refreshlist;

/**
 * Created by Jianbin on 2016/4/21.
 */
public interface RefreshablePresenter {

//    void setRefreshableView(IRefreshableListView<T> view);

    void refresh();

    void loadMore(int skip, int limit);
}
