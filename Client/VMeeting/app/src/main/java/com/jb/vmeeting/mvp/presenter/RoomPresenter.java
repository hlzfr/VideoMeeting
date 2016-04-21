package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class RoomPresenter extends BaseRefreshablePresenter<Room> {

    RoomService roomService = RetrofitHelper.createService(RoomService.class);

    public RoomPresenter(IRefreshableListView<Room> view) {
        super(view);
    }

    @Override
    public void refreshData() {
        roomService.getRoomList(0, RoomService.LIMIT_DEFINED_BY_SERVLET).enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int code, Page<Room> result) {
                refreshSuccess(result);
            }

            @Override
            public void onFailed(int code, String message) {
                refreshFailed(code, message);
            }
        });
    }

    @Override
    public void loadMoreData(int skip, int limit) {
        roomService.getRoomList(skip, limit).enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int code, Page<Room> result) {
                loadMoreSuccess(result);
            }

            @Override
            public void onFailed(int code, String message) {
                loadMoreFailed(code, message);
            }
        });
    }

}
