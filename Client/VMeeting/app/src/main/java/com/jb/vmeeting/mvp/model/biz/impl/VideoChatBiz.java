package com.jb.vmeeting.mvp.model.biz.impl;

import com.jb.vmeeting.mvp.model.biz.IVideoChatBiz;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.Session;
import com.jb.vmeeting.tools.streaming.SessionBuilder;
import com.jb.vmeeting.tools.streaming.rtsp.RtspClient;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatBiz implements IVideoChatBiz{

    RtspClient rtspClient;
    Session.SessionCallBack sessionCallBack;
    RtspClient.Callback clientCallback;

    public VideoChatBiz(RtspClient client, RtspClient.Callback clientCallback,Session.SessionCallBack sessionCallBack) {
        this.clientCallback = clientCallback;
        this.sessionCallBack = sessionCallBack;
        SessionBuilder.getInstance().setCallback(sessionCallBack);
        setRtspClient(client);
        rtspClient.setCallback(clientCallback);
    }

    public void setRtspClient(RtspClient client) {
        this.rtspClient = client;
    }

    @Override
    public void startStream(String channelName) {
        if(rtspClient.isStreaming()) {
            L.e("client is streaming now");
            return;
        }

        rtspClient.setStreamPath("/" + channelName + ".sdp");
        rtspClient.startStream();
    }

    @Override
    public void stopStream() {
        if (rtspClient != null && rtspClient.isStreaming()) {
            rtspClient.stopStream();
        }
    }

    @Override
    public boolean isStreaming() {
        return rtspClient.isStreaming();
    }
}
