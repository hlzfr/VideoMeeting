package com.jb.vmeeting.mvp.view;

/**
 * Created by Jianbin on 16/3/14.
 */
public interface ISignUpView {
    String getUsername();

    String getPassword();

    void preSignUp();

    void onSignUpSuccess();

    void onSignUpFailed(String msg);
}
