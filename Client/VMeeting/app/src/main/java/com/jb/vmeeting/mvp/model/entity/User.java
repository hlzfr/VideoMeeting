package com.jb.vmeeting.mvp.model.entity;

import com.google.gson.annotations.Expose;
import com.jb.vmeeting.tools.L;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class User extends BaseEntity implements Cloneable{

    @Expose
    private String nickName; // 昵称，用于显示给其他用户看的

    @Expose
    private String username;// 用户名，用于登录验证的唯一标识

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User clone() {
        User user = null;
        try {
            user = (User) super.clone();
        } catch (CloneNotSupportedException e) {
            L.e(e);
        }
        return user;
    }
}
