package com.jb.vmeeting.mvp.presenter;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.RoomFiles;
import com.jb.vmeeting.mvp.model.entity.RoomPpt;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.helper.ProgressRequestListener;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.view.IRoomChatView;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.tools.netfile.UploadManager;
import com.jb.vmeeting.tools.task.TaskExecutor;
import com.jb.vmeeting.tools.webrtc.PeerConnectionParameters;
import com.jb.vmeeting.tools.webrtc.PptControlMsg;
import com.jb.vmeeting.tools.webrtc.TextMessage;
import com.jb.vmeeting.tools.webrtc.WebRtcClient;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;

import java.io.File;

/**
 * 房间聊天
 * Created by Jianbin on 2016/4/16.
 */
public class RoomChatPresenter extends BasePresenter implements WebRtcClient.RtcListener, PresenterLifeTime{
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";

    private WebRtcClient mWebRtcClient;
    private IRoomChatView mView;

    private RoomPpt roomPpt;

    RoomService roomService = RetrofitHelper.createService(RoomService.class);

    private VideoRenderer.Callbacks localRender;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;

    private String mSocketAddress;

    private User currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();

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

    public void getPpt() {
        if (mView == null || mView.getRoom() == null) {
            return;
        }
        roomService.getRoomPpts(mView.getRoom()).enqueue(new SimpleCallback<RoomPpt>() {
            @Override
            public void onSuccess(int statusCode, Result<RoomPpt> result) {
                roomPpt = result.body;
                if (mView != null && result.body != null) {
                    mView.onGetPpts(result.body);
                }
            }

            @Override
            public void onFailed(int statusCode, Result<RoomPpt> result) {
                ToastUtil.toast("获取ppt失败:" + result.message);
            }
        });
    }

    boolean isStreamingFile = false;
    public void addPpt(File file) {
        if (isStreamingFile) {
            return ;
        }
        if (mView != null) {
            mView.onUploadStart(file);
        }
        isStreamingFile = true;
        // 开始上传
        UploadManager.getInstance().upload(file, new SimpleCallback<String>() {
            @Override
            public void onSuccess(int statusCode, Result<String> result) {
                isStreamingFile = false;
                String url = result.body;
                roomPpt.getPpts().add(url);
                updateRoomPpt(roomPpt); // 更新远端数据
                if (mView != null) {
                    PptControlMsg controlMsg = new PptControlMsg();
                    controlMsg.type = PptControlMsg.TYPE_ADD;
                    controlMsg.url = url;
                    try {
                        mWebRtcClient.sendPptControlMsg2Room(mView.getRoomName(), controlMsg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mView.onPptAdd(url);
                    mView.onUploadSuccess(url);
                }
            }

            @Override
            public void onFailed(int statusCode, Result<String> result) {
                isStreamingFile = false;
                if (mView != null) {
                    mView.onUploadFailed(result.code, result.message);
                }
            }
        }, new ProgressRequestListener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength, boolean done) {
                if (mView != null) {
                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onUploading(bytesWritten, contentLength);
                        }
                    });
                }
            }
        });
    }

    public void movePpt(int toPosition) {
        if (mView != null) {
            PptControlMsg controlMsg = new PptControlMsg();
            controlMsg.type = PptControlMsg.TYPE_MOVE;
            controlMsg.position = toPosition;
            try {
                mWebRtcClient.sendPptControlMsg2Room(mView.getRoomName(), controlMsg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletePpt(String url) {
        roomPpt.getPpts().remove(url);
        updateRoomPpt(roomPpt);
        PptControlMsg controlMsg = new PptControlMsg();
        controlMsg.type = PptControlMsg.TYPE_DELETE;
        controlMsg.url = url;
        try {
            mWebRtcClient.sendPptControlMsg2Room(mView.getRoomName(), controlMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mView != null) {
            mView.onPptDelete(url);
        }
    }

    public void updateRoomPpt(RoomPpt roomPpt) {
        roomService.updateRoomPpts(roomPpt).enqueue(new SimpleCallback<Void>() {
            @Override
            public void onSuccess(int statusCode, Result<Void> result) {
                ToastUtil.toast("更新成功");
            }

            @Override
            public void onFailed(int statusCode, Result<Void> result) {
                ToastUtil.toast("更新失败:" + result.message);
            }
        });
    }

    public boolean sendTextMessage(@NonNull String content) {
        boolean sendSuccess = false;
        if (TextUtils.isEmpty(content.trim())) {
            sendSuccess = false;
            ToastUtil.toast("消息不能为空");
            return sendSuccess;
        }
        if (mWebRtcClient != null && mView != null) {
            TextMessage message = new TextMessage();
            message.setAvatar(currentUser.getAvatar());
            message.setNickName(currentUser.getNickName());
            message.setContent(content);
            try {
                mWebRtcClient.sendTextMessage2Room(mView.getRoomName(), message);
            } catch (JSONException e) {
                ToastUtil.toast("发送失败");
                L.e(e);
            }
            mView.onReceiveTextMessage(message);
            sendSuccess = true;
        } else {
            ToastUtil.toast("初始化中...");
        }
        return sendSuccess;
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

    @Override
    public void onTextReceived(final TextMessage message) {
        TaskExecutor.runTaskOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.onReceiveTextMessage(message);
                }
            }
        });
    }

    @Override
    public void onPptChanged(PptControlMsg pptControlMsg) {
        if (mView == null) {
            return;
        }
        if (pptControlMsg.type == PptControlMsg.TYPE_ADD) {
            mView.onPptAdd(pptControlMsg.url);
        } else if (pptControlMsg.type == PptControlMsg.TYPE_MOVE) {
            mView.onPptMoved(pptControlMsg.position);
        } else if (pptControlMsg.type == PptControlMsg.TYPE_DELETE) {
            mView.onPptDelete(pptControlMsg.url);
        }
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
