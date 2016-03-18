package com.jb.vmeeting.mvp.model.eventbus.event;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * Created by Jianbin on 16/3/14.
 */
public class LoginEvent {
    public boolean success = false;
    public String msg;
    public User user;

    /**
     * @param success login success or not
     * @param msg shows the err message. not empty only when success is false.
     * @param user not null only when success is true.
     */
    public LoginEvent(boolean success, String msg, User user) {
        this.success = success;
        this.msg = msg;
        this.user = user;
    }
}
