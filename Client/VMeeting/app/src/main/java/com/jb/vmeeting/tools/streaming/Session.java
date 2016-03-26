package com.jb.vmeeting.tools.streaming;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.SurfaceView;

import com.jb.vmeeting.tools.streaming.audio.AudioStream;
import com.jb.vmeeting.tools.streaming.video.VideoStream;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 使用{@link SessionBuilder} 来构建Session
 * 通过该类配置传输流 {@link AudioStream} 和 {@link VideoStream}
 * 并控制流的传输
 *
 * Created by Jianbin on 2016/3/25.
 */
public class Session {

    private AudioStream mAudioStream = null;
    private VideoStream mVideoStream = null;

    private String mOrigin;
    private String mDestination;
    private long mTimestamp;

    private SessionCallBack mCallback;
    private Handler mMainHandler; // 用于发布到UI线程

    private static CountDownLatch sSignal;
    private static Handler sHandler;

    static {
        // 创建异步线程, 用于执行Session发起的异步方法
        sSignal = new CountDownLatch(1);
        new HandlerThread("com.jb.test.streaming.Session"){
            @Override
            protected void onLooperPrepared() {
                sHandler = new Handler();
                sSignal.countDown();
            }
        }.start();
    }

    public Session() {
        long uptime = System.currentTimeMillis();
        mMainHandler = new Handler(Looper.getMainLooper());
        mTimestamp = (uptime/1000)<<32 & (((uptime-((uptime/1000)*1000))>>32)/1000); // NTP 时间戳

        // 等待sHandler创建完成
        try {
            sSignal.await();
        } catch (InterruptedException e) {}
    }

    /**
     * 使用{@link SessionBuilder}来设置本机地址
     * @param origin
     */
    public void setOrigin(String origin) {
        mOrigin = origin;
    }

    /**
     * 使用{@link SessionBuilder}来设置目标地址
     * @param destination
     */
    public void setDestination(String destination) {
        mDestination = destination;
    }

    /**
     * 使用{@link SessionBuilder}来设置
     * @param track
     */
    void addAudioTrack(AudioStream track) {
        removeAudioTrack();
        mAudioStream = track;
    }

    /**
     * 使用{@link SessionBuilder}来设置
     * @param track
     */
    void addVideoTrack(VideoStream track) {
        removeVideoTrack();
        mVideoStream = track;
    }

    void removeAudioTrack() {
        if (mAudioStream != null) {
            mAudioStream.stop();
            mAudioStream = null;
        }
    }

    void removeVideoTrack() {
        if (mVideoStream != null) {
            mVideoStream.stopPreview();
            mVideoStream = null;
        }
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getDestination() {
        return mDestination;
    }

    public AudioStream getAudioTrack() {
        return mAudioStream;
    }

    public VideoStream getVideoTrack() {
        return mVideoStream;
    }

    public void setCallback(SessionCallBack callback) {
        mCallback = callback;
    }

    /**
     * 设置录制视频时候的预览窗口(SurfaceView)
     * 在下次执行{@link #start()} 或者 {@link #startPreview()}的时候生效
     */
    public void setSurfaceView(final SurfaceView view) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mVideoStream != null) {
                    mVideoStream.setSurfaceView(view);
                }
            }
        });
    }

    /**
     * 设置预览窗口的旋转角
     * 在下次执行{@link #configure()}时生效
     * @param orientation
     */
    public void setPreviewOrientation(int orientation) {
        if (mVideoStream != null) {
//            mVideoStream.setPreviewOrientation(orientation);
        }
    }

    public SessionCallBack getCallback() {
        return mCallback;
    }

    public void configure() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    syncConfigure();
                } catch (Exception e) {};
            }
        });
    }

    public void syncConfigure() throws RuntimeException, IOException {

        for (int id=0;id<2;id++) {
            Stream stream = id==0 ? mAudioStream : mVideoStream;
            if (stream!=null && !stream.isStreaming()) {
                // 配置音视频流
                stream.configure();
                // TODO 若配置失败，发送失败消息
            }
        }
        //TODO 发送session配置成功消息
    }


    /**
     * Returns a Session Description that can be stored in a file or sent to a client with RTSP.
     * @return 发送给服务端的Session描述信息.
     * @throws IllegalStateException 如果{@link #setDestination(String)} 没有被设置过，抛出异常
     */
    public String getSessionDescription() {
        StringBuilder sessionDescription = new StringBuilder();
        if (mDestination==null) {
            throw new IllegalStateException("setDestination() has not been called !");
        }
        sessionDescription.append("v=0\r\n");
        // TODO Add IPV6 support
        sessionDescription.append("o=- "+mTimestamp+" "+mTimestamp+" IN IP4 "+mOrigin+"\r\n");
        sessionDescription.append("s=Unnamed\r\n");
        sessionDescription.append("i=N/A\r\n");
        sessionDescription.append("c=IN IP4 "+mDestination+"\r\n");
        // t=0 0 代表 session 是永久的 (不知道什么时候停止)
        sessionDescription.append("t=0 0\r\n");
        sessionDescription.append("a=recvonly\r\n");

        if (mAudioStream != null) {
            sessionDescription.append(mAudioStream.getSessionDescription());
            // audio stream trackID = 0
            sessionDescription.append("a=control:trackID="+0+"\r\n");
        }
        if (mVideoStream != null) {
            sessionDescription.append(mVideoStream.getSessionDescription());
            // video stream trackID = 1
            sessionDescription.append("a=control:trackID=" + 1 + "\r\n");
        }
        return sessionDescription.toString();
    }


    /**
     * 异步开启流的传输
     */
    public void start() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    syncStart();
                } catch (Exception e) {}
            }
        });
    }

    public void syncStart(){
        syncStart(1);
        syncStart(0);
        //TODO 若syncStart(0)开始失败，开启失败的处理 syncStop(1)
    }

    /**
     * 同步线程开始流传输
     * @param id 流id，0代表音频，1代表视频
     */
    public void syncStart(int id) {
        // TODO 同步开始流的传输
    }

    /**
     * 停止流传输
     */
    public void stop() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                syncStop();
            }
        });
    }

    private void syncStop(final int id) {
        Stream stream = id==0 ? mAudioStream : mVideoStream;
        if (stream!=null) {
            // 停止流传输
            stream.stop();
        }
    }

    /** 同步线程停止流传输 */
    public void syncStop() {
        syncStop(0);
        syncStop(1);
        // TODO 发送session停止消息
//        postSessionStopped();
    }

    public void startPreview() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mVideoStream != null) {
                    //TODO 配置mVideoStream并开始预览
                    //TODO 发送开始预览消息
                    //TODO 若开启预览失败发送预览失败消息
                }
            }
        });
    }

    public void stopPreview() {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mVideoStream != null) {
                    mVideoStream.stopPreview();
                }
            }
        });
    }

    public interface SessionCallBack {
        public void onBitrareUpdate(long bitrate);
        public void onSessionError(int reason, int streamType, Exception e);
        public void onPreviewStarted();
        public void onSessionConfigured();
        public void onSessionStarted();
        public void onSessionStopped();
    }
}
