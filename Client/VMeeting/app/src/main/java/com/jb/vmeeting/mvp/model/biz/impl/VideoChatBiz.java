package com.jb.vmeeting.mvp.model.biz.impl;

import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.mvp.model.biz.IVideoChatBiz;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.Session;
import com.jb.vmeeting.tools.streaming.SessionBuilder;
import com.jb.vmeeting.tools.streaming.audio.AACStream;
import com.jb.vmeeting.tools.streaming.rtsp.RtspClient;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatBiz implements IVideoChatBiz{

    RtspClient rtspClient;
    Session.SessionCallBack sessionCallBack;
    RtspClient.Callback clientCallback;

    public VideoChatBiz(RtspClient.Callback clientCallback,Session.SessionCallBack sessionCallBack) {
        this.clientCallback = clientCallback;
        this.sessionCallBack = sessionCallBack;

        initClient();
    }

    private void initClient() {
        SessionBuilder.getInstance().setCallback(sessionCallBack);
        SessionBuilder.getInstance().setAudioStream(new AACStream());
        // TODO set video stream
        Session session = SessionBuilder.getInstance().build();
        rtspClient = new RtspClient();
        rtspClient.setServerAddress(AppConstant.STREAM_SERVER_HOST, AppConstant.STREAM_SERVER_PORT);
        rtspClient.setSession(session);
        rtspClient.setCallback(clientCallback);
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
