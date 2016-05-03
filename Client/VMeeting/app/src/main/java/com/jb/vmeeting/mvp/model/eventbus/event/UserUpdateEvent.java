package com.jb.vmeeting.mvp.model.eventbus.event;

import com.jb.vmeeting.mvp.model.entity.User;

/**
 * Created by Jianbin on 2016/4/26.
 */
public class UserUpdateEvent {
    public boolean success = false;
    public String message;
    public User user;

    public UserUpdateEvent(boolean success, String message, User user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }
}
