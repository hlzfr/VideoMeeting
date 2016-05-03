package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.eventbus.event.LoginEvent;
import com.jb.vmeeting.mvp.view.ILoginView;
import com.jb.vmeeting.tools.account.AccountManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jianbin on 16/3/14.
 */
public class LoginPresenter extends BasePresenter {
    ILoginView loginView;

    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    public void login() {
        if (loginView != null) {
            loginView.preLogin();
            AccountManager.getInstance().login(loginView.getUsername(), loginView.getPassword());
        }
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent loginEvent) {
        if (loginView != null) {
            if (loginEvent.success) {
                loginView.onLoginSuccess(loginEvent.user);
            } else {
                loginView.onLoginFailed(loginEvent.msg);
            }
        }
    }
}
