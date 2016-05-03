package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.app.constant.APIConstant;
import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.app.constant.NETCODE;
import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;
import com.jb.vmeeting.tools.account.AccountManager;

import retrofit2.Call;

/**
 * Created by Jianbin on 2016/4/24.
 */
public class MyRoomListPresenter extends BaseRefreshablePresenter<Room> {

    RoomService roomService = RetrofitHelper.createService(RoomService.class);
    User currentUser;

    Call<Result<Page<Room>>> roomListCall;

    public MyRoomListPresenter(IRefreshableListView<Room> view) {
        super(view);
        currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();
    }

    @Override
    public void refreshData() {
        if (currentUser == null) {
            // 未登录
            refreshFailed(NETCODE.STATUS.ERR_LOCAL, NETCODE.RESULT.CODE_UNAUTH, "请先登录");
            return;
        }
        roomListCall = roomService.getRoomList(1, APIConstant.LIMIT_DEFINED_BY_SERVLET, currentUser.getUsername());

        roomListCall.enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<Room>> result) {
                refreshSuccess(result.body);
                roomListCall = null;
            }

            @Override
            public void onFailed(int statusCode, Result<Page<Room>> result) {
                refreshFailed(statusCode, result.code, result.message);
                roomListCall = null;
            }
        });
    }

    @Override
    public void loadMoreData(int page, int limit) {
        if (currentUser == null) {
            // 未登录
            return;
        }
        roomListCall = roomService.getRoomList(page, limit, currentUser.getUsername());
        roomListCall.enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<Room>> result) {
                loadMoreSuccess(result.body);
                roomListCall = null;
            }

            @Override
            public void onFailed(int statusCode, Result<Page<Room>> result) {
                loadMoreFailed(statusCode, result.code, result.message);
                roomListCall = null;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (roomListCall != null) {
            roomListCall.cancel();
        }
        setRefreshableView(null);
    }
}
