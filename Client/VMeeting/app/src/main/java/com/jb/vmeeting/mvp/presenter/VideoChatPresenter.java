package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.biz.IVideoChatBiz;
import com.jb.vmeeting.mvp.model.biz.impl.VideoChatBiz;
import com.jb.vmeeting.mvp.view.IVideoChatView;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatPresenter {
    IVideoChatBiz videoChatBiz;
    IVideoChatView videoChatView;

    public VideoChatPresenter(IVideoChatView videoChatView) {
        this.videoChatView = videoChatView;
        this.videoChatBiz = new VideoChatBiz();
    }

    public void joinChannel(String channelId) {

    }

    public void send(byte[] data) {

    }

    public void quit() {

    }

}
