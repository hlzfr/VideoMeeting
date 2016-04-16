package com.jb.vmeeting.mvp.model.biz.impl;

import com.jb.vmeeting.mvp.model.biz.ILoginBiz;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * Created by Jianbin on 16/3/14.
 */
@Deprecated
public class LoginBiz implements ILoginBiz {

    @Override
    public void login(String username, String password) {
        AccountManager.getInstance().login(username, password);
    }
}
