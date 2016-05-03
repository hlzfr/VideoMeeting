package com.jb.vmeeting.page.listener;

/**
 * Created by Jianbin on 2016/4/24.
 */
public interface OnListStateChangedListener {
    void onEmpty();
    void onFailed(int code);
    void onNotEmpty();
}
