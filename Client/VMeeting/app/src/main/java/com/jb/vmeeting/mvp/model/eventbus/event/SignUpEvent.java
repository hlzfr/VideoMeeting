package com.jb.vmeeting.mvp.model.eventbus.event;

/**
 * Created by Jianbin on 16/3/14.
 */
public class SignUpEvent {
    public boolean success = false;
    public String msg;

    public SignUpEvent(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
}
