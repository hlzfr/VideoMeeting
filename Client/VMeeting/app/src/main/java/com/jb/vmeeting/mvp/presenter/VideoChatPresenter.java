package com.jb.vmeeting.mvp.presenter;

import android.text.TextUtils;

import com.jb.vmeeting.app.App;
import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.mvp.model.biz.IVideoChatBiz;
import com.jb.vmeeting.mvp.model.biz.impl.VideoChatBiz;
import com.jb.vmeeting.mvp.view.IVideoChatView;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.Session;
import com.jb.vmeeting.tools.streaming.SessionBuilder;
import com.jb.vmeeting.tools.streaming.audio.AACStream;
import com.jb.vmeeting.tools.streaming.rtsp.RtspClient;
import com.jb.vmeeting.tools.streaming.video.H264Stream;
import com.jb.vmeeting.utils.SystemUtils;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatPresenter implements Session.SessionCallBack, RtspClient.Callback{
    IVideoChatBiz videoChatBiz;
    IVideoChatView videoChatView;

    public VideoChatPresenter(IVideoChatView videoChatView) {
        this.videoChatView = videoChatView;
        this.videoChatBiz = new VideoChatBiz(initClient(), this, this);
    }

    private RtspClient initClient() {
        // TODO setOrigin
//        SessionBuilder.getInstance().setOrigin(SystemUtils.getWifiIpAddress(App.getInstance()));
        SessionBuilder.getInstance().setAudioStream(new AACStream());
        SessionBuilder.getInstance().setVideoStream(new H264Stream());
        SessionBuilder.getInstance().setSurfaceView(videoChatView.getSurfaceView());
        Session session = SessionBuilder.getInstance().build();
        RtspClient rtspClient = new RtspClient();
        rtspClient.setServerAddress(AppConstant.STREAM_SERVER_HOST, AppConstant.STREAM_SERVER_PORT);
        rtspClient.setSession(session);
        return rtspClient;
    }

    public void startStream() {
        String channelName = videoChatView.getChannelName();
        if (checkChannelName(channelName)) {
            videoChatBiz.startStream(channelName);
        } else {
            videoChatView.channelNameError("channel name is empty!");
        }
    }

    public void stopStream() {
        videoChatBiz.stopStream();
    }

    public boolean isStreaming() {
        return videoChatBiz.isStreaming();
    }

    private boolean checkChannelName(String channelName) {
        return !TextUtils.isEmpty(channelName);
    }

    @Override
    public void onBitrareUpdate(long bitrate) {

    }

    @Override
    public void onSessionError(int reason, int streamType, Exception e) {
        L.e("onSessionError error " + reason + ";type is " + streamType + "; message: " + e.getMessage());
    }

    @Override
    public void onPreviewStarted() {

    }

    @Override
    public void onSessionConfigured() {
        L.d("onSessionConfigured");
    }

    @Override
    public void onSessionStarted() {
        videoChatView.onStartStream();
    }

    @Override
    public void onSessionStopped() {
        videoChatView.onStopStream();
    }

    /**
     * rtsp连接服务端的结果回调
     */
    @Override
    public void onRtspUpdate(int message, Exception exception) {
        if (exception != null) {
            L.e("onRtspUpdate error" + message + ": " + exception.getMessage());
        }
    }

    public void destroy() {
        videoChatBiz.stopStream();
    }
}
