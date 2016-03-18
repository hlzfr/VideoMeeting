package com.jb.vmeeting.mvp.model.biz;

/**
 * Created by Jianbin on 16/3/15.
 */
public interface IVideoChatBiz {
    void send(byte[] data);
    void joinChannel(String channelId);
    void quit();
}
