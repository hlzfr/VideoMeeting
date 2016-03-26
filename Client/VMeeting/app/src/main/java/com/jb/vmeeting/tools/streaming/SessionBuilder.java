package com.jb.vmeeting.tools.streaming;

import android.hardware.Camera;
import android.view.SurfaceView;

import com.jb.vmeeting.tools.streaming.audio.AudioStream;
import com.jb.vmeeting.tools.streaming.video.VideoStream;

/**
 * Session建造者，设置参数, 用于构建 {@link Session}
 * 使用单例模式
 *
 * Created by Jianbin on 2016/3/25.
 */
public class SessionBuilder {

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private int mOrientation = 0;
    private SurfaceView mSurfaceView = null;

    private String mOrigin = null;
    private String mDestination = null;
    private int mTimeToLive = 64;

    private AudioStream mAudioStream;
    private VideoStream mVideoStream;

    private Session.SessionCallBack mCallback = null;

    public SessionBuilder setDestination(String destination) {
        mDestination = destination;
        return this;
    }

    public SessionBuilder setOrigin(String origin) {
        mOrigin = origin;
        return this;
    }

    public SessionBuilder setSurfaceView(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        return this;
    }

    public SessionBuilder setCamera(int cameraId) {
        mCameraId = cameraId;
        return this;
    }

    public SessionBuilder setCallback(Session.SessionCallBack callback) {
        mCallback = callback;
        return this;
    }

    public SessionBuilder setOrientation(int orientation) {
        mOrientation = orientation;
        return this;
    }

    public SessionBuilder setAudioStream(AudioStream audioStream) {
        mAudioStream = audioStream;
        return this;
    }

    public SessionBuilder setVideoStream(VideoStream videoStream) {
        mVideoStream = videoStream;
        return this;
    }

    public SessionBuilder setTimeToLive(int ttl) {
        mTimeToLive = ttl;
        return this;
    }

    public Session build() {
        Session session = new Session();
        session.setOrigin(mOrigin);
        session.setDestination(mDestination);
        session.setTimeToLive(mTimeToLive);

        session.addAudioTrack(mAudioStream);
        session.addVideoTrack(mVideoStream);

        session.setCallback(mCallback);

        if (session.getVideoTrack() != null) {
            // TODO 完善
            VideoStream video = session.getVideoTrack();
            video.setSurfaceView(mSurfaceView);
            video.setPreviewOrientation(mOrientation);
            video.setCamera(mCameraId);
            // TODO if set destination port necessary ?
//            video.setDestinationPorts(5006);
        }

        if (session.getAudioTrack() != null) {
            AudioStream audio = session.getAudioTrack();
            // TODO if set destination port necessary ?
//            audio.setDestinationPorts(5004);
        }

        return session;
    }

    public SessionBuilder clone() {
        //TODO clone
        return this;
    }

    private static class SingletonHoler {
        private static final SessionBuilder sSessionBuilder = new SessionBuilder();
    }

    /**
     * 使用private防止外部新建该单例类。
     * 请使用{@link #getInstance()} 获取
     */
    private SessionBuilder(){}

    public static SessionBuilder getInstance() {
        return SingletonHoler.sSessionBuilder;
    }

}
