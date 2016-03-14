package com.jb.vmeeting.tools.account;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.apiservice.AccountService;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.LoginEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.SignUpEvent;
import com.jb.vmeeting.mvp.model.helper.AuthCookie;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.page.utils.PageJumper;
import com.jb.vmeeting.page.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 帐号相关管理
 * Created by Jianbin on 2016/3/13.
 */
public class AccountManager {
    private static User sCurrentUser;

    // 帐号相关的网络请求
    private AccountService mAccountService;

    public static AccountManager getInstance() {
        return SingleHolder.sAccountManager;
    }

    public AccountManager() {
        mAccountService = RetrofitHelper.createService(AccountService.class);
    }

    public User currentUser() {
        // TODO return current user in memory or storage
        return sCurrentUser;
    }

    public void logout() {
        sCurrentUser = null;
        AuthCookie.clear();
        mAccountService.logout().enqueue(null);
    }

    public void login(String username, String password) {
        mAccountService.login(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    sCurrentUser = user;
                    // TODO save user in local

                    // post login success event
                    EventBus.getDefault().post(new LoginEvent(true, "login success", user));
                } else {
                    EventBus.getDefault().post(new LoginEvent(false, "empty user", null));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                EventBus.getDefault().post(new LoginEvent(false, t.getMessage(), null));
            }
        });
    }

    public void signUp(String username, String password) {
        mAccountService.signUp(username, password).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //TODO check success or not
                EventBus.getDefault().post(new SignUpEvent(true, "sign up success!"));

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                EventBus.getDefault().post(new SignUpEvent(false, "sign up failed." + t.getMessage()));
            }
        });
    }

    public boolean checkLogin() {
        return currentUser() != null;
    }

    public boolean checkLogin(boolean toLoginPageIfNotLogin, boolean toastMessageIfNotLogin) {
        boolean hasLogin = checkLogin();
        if (!hasLogin) {
            if (toastMessageIfNotLogin) {
                ToastUtil.toast(R.string.no_access);
            }
            if (toLoginPageIfNotLogin) {
                PageJumper.getInstance().toLoginActivity(App.getInstance());
            }
        }
        return hasLogin;
    }

    private static final class SingleHolder {
        private static AccountManager sAccountManager = new AccountManager();
    }
}
