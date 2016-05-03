package com.jb.vmeeting.tools.account;

import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.tools.storage.ACache;
import com.jb.vmeeting.tools.task.TaskExecutor;

/**
 * 帐号会话, 用于保存帐号相关信息
 *
 * Created by Jianbin on 2016/3/25.
 */
public class AccountSession {

    private User mCurrentUser;

    private AccountSession() {}

    public static AccountSession getAccountSession() {
        return SingletonHolder.session;
    }

    /**
     * 设置已登录的用户,不对外开放,
     * 只能通过AccountManager执行相关操作后设置
     */
    void setCurrentUser(User user) {
        mCurrentUser = user;
        // 文件操作在子线程执行
        TaskExecutor.executeTask(new Runnable() {
            @Override
            public void run() {
                ACache.get(App.getInstance()).put("user", mCurrentUser);
            }
        });
    }

    /**
     * 返回拷贝的user, 保证不被外部拿走后随意修改, 影响到正确的用户值
     * @return
     */
    public User getCurrentUser() {
        User user = getUser();
        return user == null ? null : user.clone();
    }

    /**
     * 获取已登录的用户，不对外开放
     * @return
     */
    private User getUser() {
        if (mCurrentUser == null) {
            mCurrentUser = (User) ACache.get(App.getInstance()).getAsObject("user");
        }
        return mCurrentUser;
    }

    public boolean hasLogin() {
        return getUser() != null;
    }

    private final static class SingletonHolder {
        private static final AccountSession session = new AccountSession();
    }
}
