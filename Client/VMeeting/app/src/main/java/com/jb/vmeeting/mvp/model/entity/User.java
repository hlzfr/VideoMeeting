package com.jb.vmeeting.mvp.model.entity;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.jb.vmeeting.tools.L;

import java.io.Serializable;

/**
 * Created by Jianbin on 2016/3/13.
 */
public class User extends BaseEntity implements Cloneable, Serializable{

    @Expose
    private String id;

    @Expose
    private String nickname; // 昵称，用于显示给其他用户看的

    @Expose
    private String username;// 用户名，用于登录验证的唯一标识

    @Expose
    private String avatar;// 头像Url

    @Expose
    private String phoneNumber;

    public String getId() {
        return id;
    }

    public String getNickName() {
        return TextUtils.isEmpty(nickname) ? username : nickname;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof User && !TextUtils.isEmpty(((User) o).getId()) && ((User) o).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        //TODO had better override
        return super.hashCode();
    }
}
