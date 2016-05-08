package com.jb.vmeeting.mvp.view;

import android.graphics.Point;
import android.opengl.GLSurfaceView;

import com.jb.vmeeting.tools.webrtc.TextMessage;

/**
 * Created by Jianbin on 2016/4/18.
 */
public interface IRoomChatView {
    GLSurfaceView getSurfaceView();

    Point getDisplaySize();

    String getRoomName();

    void onReceiveTextMessage(TextMessage message);
}
