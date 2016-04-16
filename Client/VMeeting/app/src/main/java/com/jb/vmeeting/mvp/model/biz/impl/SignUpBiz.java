package com.jb.vmeeting.mvp.model.biz.impl;

import com.jb.vmeeting.mvp.model.biz.ISignUpBiz;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * Created by Jianbin on 16/3/14.
 */
@Deprecated
public class SignUpBiz implements ISignUpBiz{

    @Override
    public void signUp(String username, String password) {
        AccountManager.getInstance().signUp(username, password);
    }
}
