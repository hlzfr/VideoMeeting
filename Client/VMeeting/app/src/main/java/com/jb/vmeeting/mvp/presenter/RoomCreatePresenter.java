package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.view.ICreateRoomView;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * Created by Jianbin on 2016/4/23.
 */
public class RoomCreatePresenter implements PresenterLifeTime{
    ICreateRoomView view;
    RoomService roomService = RetrofitHelper.createService(RoomService.class);

    public RoomCreatePresenter(ICreateRoomView view) {
        this.view = view;
    }

    public void createRoom() {
        if (view == null) {
            return;
        }
        Room room = new Room();
        room.setName(view.getRoomName());
        room.setOwnerName(AccountManager.getInstance().getAccountSession().getCurrentUser().getUsername());
        roomService.createRoom(room).enqueue(new SimpleCallback<Room>() {
            @Override
            public void onSuccess(int statusCode, Result<Room> result) {
                if (view == null) {
                    return;
                }
                view.onCreateSuccess(result.body);
            }

            @Override
            public void onFailed(int statusCode,  Result<Room> result) {
                if (view == null) {
                    return;
                }
                view.onCreateFailed(statusCode, result.message);
            }
        });
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
