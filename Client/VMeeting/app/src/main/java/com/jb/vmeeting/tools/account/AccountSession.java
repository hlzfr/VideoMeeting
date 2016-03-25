package com.jb.vmeeting.tools.account;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * 帐号会话, 用于保存帐号相关信息
 *
 * Created by Jianbin on 2016/3/25.
 */
public class AccountSession {

    private User mCurrentUser;

    public static AccountSession getAccountSession() {
        return SingletonHolder.session;
    }

    /**
     * 设置已登录的用户,不对外开放,
     * 只能通过AccountManager执行相关操作后设置
     */
    void setCurrentUser(User user) {
        // TODO save user in local
        mCurrentUser = user;
    }

    /**
     * 返回拷贝的user, 保证不被外部拿走后随意修改, 影响到正确的用户值
     * @return
     */
    public User getCurrentUser() {
        return mCurrentUser == null ? null : mCurrentUser.clone();
    }

    private final static class SingletonHolder {
        private static AccountSession session = new AccountSession();
    }
}
