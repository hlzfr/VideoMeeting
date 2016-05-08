package com.jb.vmeeting.tools.webrtc;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Created by Jianbin on 2016/5/3.
 */
public class TextMessage {
    protected static Gson sGson = new Gson();

    @Expose
    private String avatar;
    @Expose
    private String nickName;
    @Expose
    private String content;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return sGson.toJson(this);
    }
}
