package com.jb.vmeeting.mvp.presenter;

/**
 * Created by Jianbin on 2016/4/18.
 */
public interface PresenterLifeTime {
    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onDestroy();
}
