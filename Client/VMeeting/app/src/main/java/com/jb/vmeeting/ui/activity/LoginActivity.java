package com.jb.vmeeting.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.presenter.LoginPresenter;
import com.jb.vmeeting.mvp.view.ILoginView;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.ui.base.BaseActivity;
import com.jb.vmeeting.ui.utils.ToastUtil;

/**
 * 登录页面
 * Created by Jianbin on 2016/3/13.
 */
public class LoginActivity extends BaseActivity implements ILoginView {

    LoginPresenter mLoginPresenter;

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginPresenter = new LoginPresenter(this);
    }

    protected void initViews() {
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username = findView(R.id.edt_login_username);
        password  = findView(R.id.edt_login_password);
    }

    @Override
    protected void setupListener() {
    }

    // has been registered in xml layout
    public void onLoginButtonClicked(View v) {
        mLoginPresenter.login();
    }

    @Override
    public void preLogin() {
        //TODO show login waiting view
        ToastUtil.toast("start login...");
        L.d("start login...");
    }

    @Override
    public void onLoginSuccess(User user) {
        //TODO cancel login waiting view
//        PageJumper.getInstance().toMainActivity(this);
//        finish();
        ToastUtil.toast("login success!");
        L.d("login success.");
    }

    @Override
    public void onLoginFailed(String msg) {
        ToastUtil.toast("login failed." + msg);
        L.e("login failed. " + msg);
        //TODO cancel login waiting view
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public String getUsername() {
        return username.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.destroy();
    }
}
