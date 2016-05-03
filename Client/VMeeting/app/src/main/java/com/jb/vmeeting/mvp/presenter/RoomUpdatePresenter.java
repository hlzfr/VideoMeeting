package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.view.IUpdateRoomView;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * Created by Jianbin on 2016/4/23.
 */
public class RoomUpdatePresenter extends BasePresenter implements PresenterLifeTime{
    IUpdateRoomView view;
    RoomService roomService = RetrofitHelper.createService(RoomService.class);

    public RoomUpdatePresenter(IUpdateRoomView view) {
        this.view = view;
    }

    public void updateRoom(Room room) {
        if (view == null) {
            return;
        }
        User user = AccountManager.getInstance().getAccountSession().getCurrentUser();

        if (user != null) {
            room.setOwnerName(user.getUsername());
            room.setOwner(user);
        }

        roomService.createRoom(room).enqueue(new SimpleCallback<Room>() {
            @Override
            public void onSuccess(int statusCode, Result<Room> result) {
                if (view == null) {
                    return;
                }
                view.onUpdateSuccess(result.body);
            }

            @Override
            public void onFailed(int statusCode,  Result<Room> result) {
                if (view == null) {
                    return;
                }
                view.onUpdateFailed(statusCode, result.message);
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
