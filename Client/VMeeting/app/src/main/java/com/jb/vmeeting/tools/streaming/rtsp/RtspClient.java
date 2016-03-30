package com.jb.vmeeting.tools.streaming.rtsp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.Session;
import com.jb.vmeeting.tools.streaming.Stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rtsp客户端，和EasyDarwin流媒体服务器对应
 * 连接服务器，并通过 {@link Session} 进行流传输
 *
 * Created by Jianbin on 2016/3/25.
 */
public class RtspClient {

    // ------ 当前客户端的连接状态以及媒体流的传输行为 -------
    private final static int STATE_STARTED  = 0x00;  // 开始传输媒体流
    private final static int STATE_STARTING = 0x01;  // 正在连接服务端
    private final static int STATE_STOPPING = 0x02;  // 正在断开服务端
    private final static int STATE_STOPPED  = 0x03;  // 已经停止流传输

    // --------------------- 结果回调消息通知 ----------------
    public final static int ERROR_CONNECTION_FAILED = 0x01; //  与服务端连接失败
    public final static int ERROR_WRONG_CREDENTIALS = 0x03; // 错误的身份验证(没有权限)
    public final static int ERROR_CONNECTION_LOST   = 0x04; // 与服务端丢失连接

    public final static int MESSAGE_CONNECTION_RECOVERED = 0x05; // 与服务端连接恢复
    // -------------------------------------------------------


    private int mState = STATE_STARTED; // 当前客户端与服务端的连接状态

    private Socket mSocket; // 用于发送resp协议的各个 OPTIONS，如 ANNOUNCE, SETUP，PLAY，TEARDOWN, OPTION
    private BufferedReader mBufferedReader; // mSocket对应的BufferedReader
    private OutputStream mOutputStream; // mSocket对应的OutputStream
    private int mCSeq = 0; // 标识事务先后顺序

    private Handler mMainHandler;
    private Handler mAsyncHandler;
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
                mAsyncHandler = new Handler();
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
        mAsyncHandler.post(new Runnable() {
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
                    // 发送连接失败消息
                    postError(ERROR_CONNECTION_FAILED, e);
                    abord();
                    return;
                }

                try {
                    // 开始传输流
                    mParameters.session.syncStart();
                    mState = STATE_STARTED;
                    mAsyncHandler.post(mConnectionMonitor);
                } catch (Exception e) {
                    abord();
                }

            }
        });
    }

    /**
     * 停止流传输并中断连接
     * 运行在异步线程
     */
    public void stopStream() {
//        mAsyncHandler.post(new Runnable() {
//            @Override
                // can't achieve here? 好像执行不到 run 这里面
//            public void run() {
                if (mParameters != null && mParameters.session != null) {
                    // 停止流传输
                    mParameters.session.stop();
                }
                if (mState != STATE_STOPPED) {
                    mState = STATE_STOPPING;
                    // 中断连接
                    abord();
                }
//            }
//        });
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

    private void sendRequestAnnounce() throws IOException, IllegalStateException, SocketException{
        String body = mParameters.session.getSessionDescription();
        String request = "ANNOUNCE rtsp://"+mParameters.host+":"+mParameters.port+mParameters.path+" RTSP/1.0\r\n" +
                "CSeq: " + (++mCSeq) + "\r\n" +
                "Content-Length: " + body.length() + "\r\n" +
                "Content-Type: application/sdp \r\n\r\n" +
                body;
        L.i(request.substring(0, request.indexOf("\r\n")));

        mOutputStream.write(request.getBytes("UTF-8"));
        Response response = Response.parseResponse(mBufferedReader);

        if (response.headers.containsKey("server")) {
            L.v("RTSP server name:" + response.headers.get("server"));
        } else {
            L.v("RTSP server name unknown");
        }

        if (response.status == 401) {
            throw new RuntimeException("Bad credentials !");
        } else if (response.status == 403) {
            throw new RuntimeException("Access forbidden !");
        }
    }

    private void sendRequestSetUp() throws IOException, IllegalStateException, SocketException{
        for (int i=0;i<2;i++) {
            Stream stream = mParameters.session.getTrack(i);
            if (stream != null) {
                String request = "SETUP rtsp://" + mParameters.host + ":" + mParameters.port + mParameters.path + "/trackID=" + i + " RTSP/1.0\r\n" +
                        "CSeq: " + (++mCSeq) + "\r\n" +
                        "User-Agent: Android\r\n"+
                        "Transport: RTP/AVP/TCP;unicast;mode=record;interleaved=" + (i) + "-" + (i + 1) + "\r\n\r\n";

                L.i(request.substring(0, request.indexOf("\r\n")));

                mOutputStream.write(request.getBytes("UTF-8"));
                Response response = Response.parseResponse(mBufferedReader);
                try {
                    Matcher sessionMatcher = Response.rexegSession.matcher(response.headers.get("session"));
                    sessionMatcher.find();
                    mSessionID = sessionMatcher.group(1);
                    L.d("getSession id " + mSessionID);
                } catch (Exception e) {
                    throw new IOException("Invalid response from server. Session id: " + mSessionID);
                }
            }
        }
    }

    private void sendRequestPlay() throws IOException, IllegalStateException, SocketException{
        String request = "PLAY rtsp://"+mParameters.host+":"+mParameters.port+mParameters.path+" RTSP/1.0\r\n" +
                "CSeq: " + (++mCSeq) + "\r\n"+
                "User-Agent: Android\r\n"+
                "Session: "+mSessionID+"\r\n"+
                "Range: npt=0.000-\r\n\r\n";
        L.i(request.substring(0, request.indexOf("\r\n")));
        mOutputStream.write(request.getBytes("UTF-8"));
        Response.parseResponse(mBufferedReader);
    }

    private void sendRequestOption() throws IOException{
        String request = "OPTIONS rtsp://"+mParameters.host+":"+mParameters.port+mParameters.path+" RTSP/1.0\r\n" +
                "CSeq: " + (++mCSeq) + "\r\n" +
                "Content-Length: 0\r\n" +
                "Session: " + mSessionID + "\r\n";
        L.i(request.substring(0, request.indexOf("\r\n")));
        mOutputStream.write(request.getBytes("UTF-8"));
        Response.parseResponse(mBufferedReader);
    }

    private void sendRequestTeardown() throws IOException {
        String request = "TEARDOWN rtsp://"+mParameters.host+":"+mParameters.port+mParameters.path+" RTSP/1.0\r\n" +
                "CSeq: " + (++mCSeq) + "\r\n" +
                "Content-Length: 0\r\n" +
                "Session: " + mSessionID + "\r\n";
        L.i(request.substring(0, request.indexOf("\r\n")));
        mOutputStream.write(request.getBytes("UTF-8"));
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
        mAsyncHandler.removeCallbacks(mConnectionMonitor);
        mAsyncHandler.removeCallbacks(mRetryConnection);
        mState = STATE_STOPPED;
    }

    private Runnable mConnectionMonitor = new Runnable() {
        @Override
        public void run() {
            if (mState == STATE_STARTED) {
                try {
                    // 用option命令轮询rtsp服务器
                    sendRequestOption();
                    mAsyncHandler.postDelayed(mConnectionMonitor, 6000);
                } catch (IOException e) {
                    // OPTION 请求失败
                    // 发送连接丢失消息
                    postMessage(ERROR_CONNECTION_LOST);
                    L.e("Connection lost with the server...");
                    // 停止流传输
                    mParameters.session.stop();
                    mAsyncHandler.post(mRetryConnection);
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
                        mAsyncHandler.post(mConnectionMonitor);
                        // 发送连接恢复消息
                        postMessage(MESSAGE_CONNECTION_RECOVERED);
                    } catch (Exception e) {
                        abord();
                    }
                } catch (IOException e) {
                    // 1s后进行重连
                    mAsyncHandler.postDelayed(mRetryConnection, 1000);
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
        public String host; // 服务端域名(或IP地址)
//        public String username;
//        public String password;
        public String path; // 房间名
        public Session session;
        public int port; // 服务端监听端口

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

    public interface Callback {
        /**
         * rtsp连接服务端的结果回调
         */
        public void onRtspUpdate(int message, Exception exception);
    }

    static class Response {

        // Parses method & uri
        public static final Pattern regexStatus = Pattern.compile("RTSP/\\d.\\d (\\d+) (\\w+)",Pattern.CASE_INSENSITIVE);
        // Parses a request header
        public static final Pattern rexegHeader = Pattern.compile("(\\S+):(.+)",Pattern.CASE_INSENSITIVE);
        // Parses a WWW-Authenticate header
        public static final Pattern rexegAuthenticate = Pattern.compile("realm=\"(.+)\",\\s+nonce=\"(\\w+)\"",Pattern.CASE_INSENSITIVE);
        // Parses a Session header
        public static final Pattern rexegSession = Pattern.compile("(\\d+)",Pattern.CASE_INSENSITIVE);
        // Parses a Transport header
        public static final Pattern rexegTransport = Pattern.compile("client_port=(\\d+)-(\\d+).+server_port=(\\d+)-(\\d+)",Pattern.CASE_INSENSITIVE);


        public int status;
        public HashMap<String,String> headers = new HashMap<String,String>();

        /** 解析Rtsp服务器返回的请求 */
        public static Response parseResponse(BufferedReader input) throws IOException, IllegalStateException, SocketException {
            Response response = new Response();
            String line;
            Matcher matcher;
            // Parsing request method & uri
            if ((line = input.readLine())==null) throw new SocketException("Connection lost");
            matcher = regexStatus.matcher(line);
            matcher.find();
            response.status = Integer.parseInt(matcher.group(1));

            // 解析消息头
            while ( (line = input.readLine()) != null) {
                //Log.e(TAG,"l: "+line.length()+"c: "+line);
                if (line.length()>3) {
                    matcher = rexegHeader.matcher(line);
                    matcher.find();
                    response.headers.put(matcher.group(1).toLowerCase(Locale.US),matcher.group(2));
                } else {
                    break;
                }
            }
            if (line==null) throw new SocketException("Connection lost");
            L.d("Response from server: "+response.status);
            return response;
        }
    }
}
