package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.Room;

/**
 * Created by Jianbin on 2016/4/23.
 */
public interface ICreateRoomView {
    String getRoomName();

    void onCreateSuccess(Room room);

    void onCreateFailed(int code, String message);
}
