package com.jb.vmeeting.mvp.view;

import android.graphics.Point;
import android.opengl.GLSurfaceView;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.RoomPpt;
import com.jb.vmeeting.tools.webrtc.TextMessage;

import java.io.File;

/**
 * Created by Jianbin on 2016/4/18.
 */
public interface IRoomChatView {
    GLSurfaceView getSurfaceView();

    Point getDisplaySize();

    String getRoomName();

    void onReceiveTextMessage(TextMessage message);

    void onGetPpts(RoomPpt ppts);

    Room getRoom();

    void onUploadStart(File file);
    void onUploading(long current, long total);
    void onUploadSuccess(String url);
    void onUploadFailed(int code, String reason);

    void onPptDelete(String url);
    void onPptAdd(String url);
    void onPptMoved(int toPosition);
}
