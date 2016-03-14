package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.biz.ISignUpBiz;
import com.jb.vmeeting.mvp.model.biz.impl.SignUpBiz;
import com.jb.vmeeting.mvp.model.eventbus.event.SignUpEvent;
import com.jb.vmeeting.mvp.view.ISignUpView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jianbin on 16/3/14.
 */
public class SignUpPresenter {
    ISignUpView signUpView;
    ISignUpBiz signUpBiz;

    public SignUpPresenter(ISignUpView signUpView) {
        this.signUpView = signUpView;
        this.signUpBiz = new SignUpBiz();
        EventBus.getDefault().register(this);
    }

    public void signUp() {
        signUpView.preSignUp();
        signUpBiz.signUp(signUpView.getUsername(), signUpView.getPassword());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSignUpResult(SignUpEvent signUpEvent) {
        if (signUpEvent.success) {
            signUpView.onSignUpSuccess();
        } else {
            signUpView.onSignUpFailed(signUpEvent.msg);
        }
    }

    /**
     * should call it when activity is about to be destroyed,
     * or memory leak may happens.
     *
     * Release obj here if necessary.
     */
    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

}
