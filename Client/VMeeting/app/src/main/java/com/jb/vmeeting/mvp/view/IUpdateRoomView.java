package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.Room;

/**
 * Created by Jianbin on 2016/4/23.
 */
public interface IUpdateRoomView {
//    String getRoomName();

    void onUpdateSuccess(Room room);

    void onUpdateFailed(int code, String message);
}
