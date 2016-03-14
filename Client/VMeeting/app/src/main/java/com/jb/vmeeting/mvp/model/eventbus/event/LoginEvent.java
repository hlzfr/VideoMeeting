package com.jb.vmeeting.mvp.model.eventbus.event;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * Created by Jianbin on 16/3/14.
 */
public class LoginEvent {
    public boolean success = false;
    public String msg;
    public User user;

    public LoginEvent(boolean success, String msg, User user) {
        this.success = success;
        this.msg = msg;
        this.user = user;
    }
}
