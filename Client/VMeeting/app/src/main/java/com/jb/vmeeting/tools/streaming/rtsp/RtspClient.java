package com.jb.vmeeting.tools.streaming.rtsp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Rtsp客户端，和EasyDarwin流媒体服务器对应
 * 连接服务器，并通过 {@link Session} 进行流传输
 *
 * Created by Jianbin on 2016/3/25.
 */
public class RtspClient {
    // ------ 当前客户端的连接状态以及媒体流的传输行为 ------- //
    private final static int STATE_STARTED  = 0x00;  // 开始传输媒体流
    private final static int STATE_STARTING = 0x01;  // 正在连接服务端
    private final static int STATE_STOPPING = 0x02;  // 正在断开服务端
    private final static int STATE_STOPPED  = 0x03;  // 已经停止流传输
    // ------------------------------------------------------- //


    private int mState = STATE_STARTED; // 当前客户端与服务端的连接状态

    private Socket mSocket; // 用于发送resp协议的各个 OPTIONS，如 ANNOUNCE, SETUP，PLAY，TEARDOWN, OPTION
    private BufferedReader mBufferedReader; // mSocket对应的BufferedReader
    private OutputStream mOutputStream; // mSocket对应的OutputStream
    private int mCSeq = 0; // 标识事务先后顺序

    private Handler mMainHandler;
    private Handler mAsyHandler;
    private Callback mCallback;

    private Parameters mTmpParameters; // 做为参数的中间存储用,防止对Parameters的修改影响到正在进行的传输
    private Parameters mParameters; // 真正使用时被赋值为mTmpParameters的clone，保证其只读，不可修改。要修改只修改mTmpParameters


    private String mSessionID; // 和服务端保持的session id

    public RtspClient() {
        mCSeq = 0;
        mTmpParameters = new Parameters();
        mTmpParameters.port = 1935;
        mTmpParameters.path = "/";
//        mAuthorization = null;
        mCallback = null;
        mMainHandler = new Handler(Looper.getMainLooper());
        mState = STATE_STOPPED;

        final Semaphore signal = new Semaphore(0);
        // 异步线程的Handler
        new HandlerThread("com.jb.streaming.RtspClient"){
            @Override
            protected void onLooperPrepared() {
                mAsyHandler = new Handler();
                signal.release();
            }
        }.start();
        signal.acquireUninterruptibly();
    }

    public void setCallback(Callback cb) {
        mCallback = cb;
    }

    public void setSession(Session session) {
        mTmpParameters.session = session;
    }

    public Session getSession() {
        return mTmpParameters.session;
    }
    public void setServerAddress(String host, int port) {
        mTmpParameters.port = port;
        mTmpParameters.host = host;
    }

    public void setStreamPath(String path) {
        mTmpParameters.path = path;
    }

    public void startStream() {
        if (mTmpParameters.host == null) throw new IllegalStateException("setServerAddress(String,int) has not been called !");
        if (mTmpParameters.session == null) throw new IllegalStateException("setSession() has not been called !");
        mAsyHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mState != STATE_STOPPED) return;
                mState = STATE_STARTING;

                L.d("Connecting to RTSP server...");

                // 防止在进行传输时使用者调用方法修改了客户端参数信息
                // 只有重新开始传输，新的 Parameters 才能生效
                mParameters = mTmpParameters.clone();
                mParameters.session.setDestination(mTmpParameters.host);

                try {
                    // 配置流的必要设置
                    mParameters.session.syncConfigure();
                } catch (Exception e) {
                    mParameters.session = null;
                    mState = STATE_STOPPED;
                    return;
                }

                try {
                    tryConnection();
                } catch (Exception e) {
                    //TODO 发送连接失败消息
//                    postError(ERROR_CONNECTION_FAILED, e);
                    abord();
                    return;
                }

                try {
                    // 开始传输流
                    mParameters.session.syncStart();
                    mState = STATE_STARTED;
                    mAsyHandler.post(mConnectionMonitor);
                } catch (Exception e) {
                    abord();
                }

            }
        });
    }

    /**
     * 停止流传输并中断连接
     */
    public void stopStream() {
        if (mParameters != null && mParameters.session != null) {
            // 停止流传输
            mParameters.session.stop();
        }
        if (mState != STATE_STOPPED) {
            mState = STATE_STOPPING;
            // 中断连接
            abord();
        }
    }

    private void tryConnection() throws IOException {
        mCSeq = 0;
        mSocket = new Socket(mParameters.host, mParameters.port);
        mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mOutputStream = mSocket.getOutputStream();
        sendRequestAnnounce();
        sendRequestSetUp();
        sendRequestPlay();
    }

    private void sendRequestAnnounce() {
        // TODO
    }

    private void sendRequestSetUp() {
        // TODO
    }

    private void sendRequestPlay() {
        // TODO
    }

    private void sendRequestOption() throws IOException{
        // TODO
    }

    private void sendRequestTeardown() {
        // TODO
    }
    public boolean isStreaming() {
        return mState==STATE_STARTED|mState==STATE_STARTING;
    }

    /**
     * 向服务端发起teardown命令，中断连接
     */
    private void abord() {
        try {
            sendRequestTeardown();
        } catch (Exception ignore) {}
        try {
            mSocket.close();
        } catch (Exception ignore) {}
        mAsyHandler.removeCallbacks(mConnectionMonitor);
        mAsyHandler.removeCallbacks(mRetryConnection);
        mState = STATE_STOPPED;
    }

    private Runnable mConnectionMonitor = new Runnable() {
        @Override
        public void run() {
            if (mState == STATE_STARTED) {
                try {
                    // 用option命令轮询rtsp服务器
                    sendRequestOption();
                    mAsyHandler.postDelayed(mConnectionMonitor, 6000);
                } catch (IOException e) {
                    // OPTION 请求失败
                    //TODO　发送连接丢失消息
//                    postMessage(ERROR_CONNECTION_LOST);
                    L.e("Connection lost with the server...");
                    // 停止流传输
                    mParameters.session.stop();
                    mAsyHandler.post(mRetryConnection);
                }
            }
        }
    };

    private Runnable mRetryConnection = new Runnable() {
        @Override
        public void run() {
            if (mState == STATE_STARTED) {
                try {
                    L.e("Trying to reconnect...");
                    tryConnection();
                    try {
                        // 开始流传输
                        mParameters.session.start();
                        mAsyHandler.post(mConnectionMonitor);
                        // TODO 发送连接恢复消息
//                        postMessage(MESSAGE_CONNECTION_RECOVERED);
                    } catch (Exception e) {
                        abord();
                    }
                } catch (IOException e) {
                    mAsyHandler.postDelayed(mRetryConnection, 1000);
                }
            }
        }
    };

    private void postMessage(final int message) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onRtspUpdate(message, null);
                }
            }
        });
    }

    private void postError(final int message, final Exception e) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onRtspUpdate(message, e);
                }
            }
        });
    }

    /**
     * 该客户端的参数
     */
    private class Parameters {
        public String host; // 域名(或IP地址)
//        public String username;
//        public String password;
        public String path; // 房间名
        public Session session;
        public int port; // 端口

        public Parameters clone() {
            Parameters params = new Parameters();
            params.host = host;
//            params.username = username;
//            params.password = password;
            params.path = path;
            params.session = session;
            params.port = port;
            return params;
        }
    }

    /**
     * rtsp连接服务端的结果回调
     */
    public interface Callback {
        public void onRtspUpdate(int message, Exception exception);
    }
}
