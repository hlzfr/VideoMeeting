package com.jb.vmeeting.tools.account;

import android.content.Context;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.apiservice.AccountService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.LoginEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.LogoutEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.SignUpEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.UserUpdateEvent;
import com.jb.vmeeting.mvp.model.helper.AuthCookie;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 帐号相关管理，用于执行帐号相关操作
 *
 * Created by Jianbin on 2016/3/13.
 */
public class AccountManager {

    // 帐号相关的网络请求
    private AccountService mAccountService;

    public static AccountManager getInstance() {
        return SingleHolder.sAccountManager;
    }

    private AccountManager() {
        mAccountService = RetrofitHelper.createService(AccountService.class);
    }

    public AccountSession getAccountSession() {
        return AccountSession.getAccountSession();
    }

    public void logout() {
        AccountSession.getAccountSession().setCurrentUser(null);
        AuthCookie.clear();
        mAccountService.logout().enqueue(new Callback<Result<Void>>() {
            @Override
            public void onResponse(Call<Result<Void>> call, Response<Result<Void>> response) {
                // do nothing
            }

            @Override
            public void onFailure(Call<Result<Void>> call, Throwable throwable) {
                // do nothing
            }
        });
        EventBus.getDefault().post(new LogoutEvent());
    }

    /**
     * 发起登录行为
     * 登录后会使用EventBus发送一个LoginEvent
     * @param username
     * @param password
     */
    public void login(String username, String password) {
        mAccountService.login(username, password).enqueue(new SimpleCallback<User>() {
            @Override
            public void onSuccess(int statusCode, Result<User> result) {
                AccountSession.getAccountSession().setCurrentUser(result.body);
                // post login success event
                EventBus.getDefault().post(new LoginEvent(true, "login success", AccountSession.getAccountSession().getCurrentUser()));
            }

            @Override
            public void onFailed(int statusCode, Result<User> result) {
                EventBus.getDefault().post(new LoginEvent(false, result.message, null));
            }
        });
    }

    /**
     * 发起一个注册行为
     * 注册后会利用EventBus发起一个SignUpEvent
     * @param username
     * @param password
     */
    public void signUp(String username, String password) {
        mAccountService.signUp(username, password).enqueue(new SimpleCallback<Void>() {
            @Override
            public void onSuccess(int statusCode, Result<Void> result) {
                EventBus.getDefault().post(new SignUpEvent(true, "sign up success! "));
            }

            @Override
            public void onFailed(int statusCode, Result<Void> result) {
                EventBus.getDefault().post(new SignUpEvent(false, "sign up failed." + result.message));
            }
        });
    }

    public void updateUserInfo(User user) {
        mAccountService.update(user).enqueue(new SimpleCallback<User>() {
            @Override
            public void onSuccess(int statusCode, Result<User> result) {
                AccountSession.getAccountSession().setCurrentUser(result.body);
                EventBus.getDefault().post(new UserUpdateEvent(result.success, result.message, AccountSession.getAccountSession().getCurrentUser()));
            }

            @Override
            public void onFailed(int statusCode, Result<User> result) {
                EventBus.getDefault().post(new UserUpdateEvent(result.success, result.message, result.body));
            }
        });
    }

    /**
     *
     * @return true has login
     */
    public boolean checkLogin() {
        return getAccountSession().hasLogin();
    }

    /**
     *
     * @return true has login
     */
    public boolean checkLogin(Context ctx, boolean toLoginPageIfNotLogin, boolean toastMessageIfNotLogin) {
        boolean hasLogin = checkLogin();
        if (!hasLogin) {
            if (toastMessageIfNotLogin) {
                ToastUtil.toast(R.string.no_access);
            }
            if (toLoginPageIfNotLogin) {
                PageNavigator.getInstance().toLoginActivity(ctx, true);
            }
        }
        return hasLogin;
    }

    private static final class SingleHolder {
        private static final AccountManager sAccountManager = new AccountManager();
    }
}
