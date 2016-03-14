package com.jb.vmeeting.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.presenter.SignUpPresenter;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.ui.base.BaseActivity;
import com.jb.vmeeting.mvp.view.ISignUpView;
import com.jb.vmeeting.ui.utils.ToastUtil;

/**
 * 注册页面
 * Created by Jianbin on 16/3/14.
 */
public class SignUpActivity extends BaseActivity implements ISignUpView{

    SignUpPresenter mSignUpPresenter;

    EditText edtPassword;
    EditText edtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignUpPresenter = new SignUpPresenter(this);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtUsername = findView(R.id.edt_signUp_username);
        edtPassword = findView(R.id.edt_signUp_password);
    }

    public void onSignUpBtnClicked(View view) {
        mSignUpPresenter.signUp();
    }

    @Override
    public String getUsername() {
        return edtUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return edtPassword.getText().toString();
    }

    @Override
    public void preSignUp() {
        //TODO show waiting
        L.d("pre sign up");
        ToastUtil.toast("pre sign up");
    }

    @Override
    public void onSignUpSuccess() {
        ToastUtil.toast("onSignUpSuccess");
        L.d("onSignUpSuccess");
        //TODO cancel login waiting view
    }

    @Override
    public void onSignUpFailed(String msg) {
        ToastUtil.toast("onSignUpFailed");
        L.e("onSignUpFailed " + msg);
        //TODO cancel login waiting view
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSignUpPresenter.destroy();
    }
}
