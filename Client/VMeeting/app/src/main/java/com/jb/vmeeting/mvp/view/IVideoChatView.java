package com.jb.vmeeting.mvp.view;

/**
 * Created by Jianbin on 16/3/15.
 */
public interface IVideoChatView {
    String getChannelName();
    void onStartStream(); // 开始向服务端传输流
    void onStopStream(); // 停止流传输
    void channelNameError(String message); // channel name不符合要求
}
