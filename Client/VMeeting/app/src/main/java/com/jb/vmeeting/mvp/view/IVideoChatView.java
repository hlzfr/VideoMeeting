package com.jb.vmeeting.mvp.view;

/**
 * Created by Jianbin on 16/3/15.
 */
public interface IVideoChatView {

    void onReceive(byte[] data);

    void onUserJoined();

    void onUserLeave();

    void onJoinSuccess();

    void onJoinFailed();

    void onConnectionLost();
}
