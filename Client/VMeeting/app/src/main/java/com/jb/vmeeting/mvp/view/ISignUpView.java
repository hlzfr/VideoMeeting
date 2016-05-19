package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * Created by Jianbin on 16/3/14.
 */
public interface ISignUpView {
//    String getUsername();
//
    String getPassword();

    User getSignupUser();

    void preSignUp();

    void onSignUpSuccess();

    void onSignUpFailed(String msg);
}
