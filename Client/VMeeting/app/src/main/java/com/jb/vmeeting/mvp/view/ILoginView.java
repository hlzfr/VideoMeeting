package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * Created by Jianbin on 16/3/14.
 */
public interface ILoginView {
    void preLogin();

    void onLoginSuccess(User user);

    void onLoginFailed(String msg);

    String getPassword();

    String getUsername();
}
