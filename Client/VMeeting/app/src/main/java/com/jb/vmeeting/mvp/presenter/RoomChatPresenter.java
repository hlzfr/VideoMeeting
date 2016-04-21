package com.jb.vmeeting.mvp.presenter;

import android.graphics.Point;
import android.opengl.GLSurfaceView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.view.IRoomChatView;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.task.TaskExecutor;
import com.jb.vmeeting.tools.webrtc.PeerConnectionParameters;
import com.jb.vmeeting.tools.webrtc.WebRtcClient;

import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

/**
 * 房间聊天
 * Created by Jianbin on 2016/4/16.
 */
public class RoomChatPresenter implements WebRtcClient.RtcListener, PresenterLifeTime{
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";

    private WebRtcClient mWebRtcClient;
    private IRoomChatView mView;

    private VideoRenderer.Callbacks localRender;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;

    private String mSocketAddress;

    public RoomChatPresenter(IRoomChatView view) {
        mView = view;
        mSocketAddress = "http://" + App.getInstance().getString(R.string.stream_host);
        mSocketAddress += (":" + App.getInstance().getString(R.string.stream_port) + "/");

        GLSurfaceView surfaceView = mView.getSurfaceView();
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setKeepScreenOn(true);
        VideoRendererGui.setView(surfaceView, new Runnable() {
            @Override
            public void run() {
                // SurfaceView 准备完毕
                L.d("eglContextReadyCallback");
                init();
            }
        });

        localRender = VideoRendererGui.create(
                0, 0,
                50, 50, scalingType, true);
    }


    private void init() {
        if (mView == null) {
            L.w("view is null, it may be destroyed");
            return;
        }
        Point displaySize = mView.getDisplaySize();
        // TODO make sure video height and width supported.确保手机摄像头支持displaySize的分辨率
        PeerConnectionParameters params = new PeerConnectionParameters(
                true, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);

        mWebRtcClient = new WebRtcClient(this, mSocketAddress, params, VideoRendererGui.getEGLContext());
    }

    /**
     * 从服务端获取到了id
     * @param callId
     */
    @Override
    public void onCallReady(String callId) {
        L.d("onCallReady my callId is" + callId);
        if (mView == null) {
            L.w("view is null, it may be destroyed");
            return;
        }
        mWebRtcClient.start(mView.getRoomName());
    }

    /**
     * 状态改变正在连接或断开连接
     * @param newStatus
     */
    @Override
    public void onStatusChanged(final String newStatus) {
        L.d("onStatusChanged newStatus is " + newStatus);
        TaskExecutor.runTaskOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toast("onStatusChanged newStatus is " + newStatus);
            }
        });
    }

    /**
     * 本地摄像头采集到的数据
     * @param localStream
     */
    @Override
    public void onLocalStream(MediaStream localStream) {
        L.d("onLocalStream label is " + localStream.label());
        addRender(localStream, 0);
    }

    /**
     * 新的端连接到了本机
     * @param remoteStream
     * @param endPoint
     */
    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        L.d("onAddRemoteStream endPoint " + endPoint);
        addRender(remoteStream, endPoint);
    }

    /**
     * 端断开了连接
     * @param endPoint
     */
    @Override
    public void onRemoveRemoteStream(int endPoint) {
        L.d("onRemoveRemoteStream endPoint " + endPoint);
        removeRender(endPoint);
    }


    private void addRender(MediaStream stream, int position) {
        VideoRenderer.Callbacks render;
        L.d("addRender position is " + position);
        if (position == 0) {
            render = localRender;
        } else {
            render = VideoRendererGui.create(position % 2 == 0 ? 0 : 50,
                    position / 2 * 50,
                    50, 50,
                    scalingType, false);
        }
        stream.videoTracks.get(0).addRenderer(new VideoRenderer(render));
    }

    private void removeRender(int position) {
        //TODO dispose unnecessary render.
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
        if (mView != null) {
            mView.getSurfaceView().onResume();
        }
        if (mWebRtcClient != null) {
            mWebRtcClient.onResume();
        }
    }

    @Override
    public void onPause() {
        if (mView != null) {
            mView.getSurfaceView().onPause();
        }
        if (mWebRtcClient != null) {
            mWebRtcClient.onPause();
        }
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onDestroy() {
        if (mWebRtcClient != null) {
            mWebRtcClient.onDestroy();
        }
        if (mView != null) {
            // release view. 释放视图以防止内存泄漏
            mView = null;
        }
    }
}
