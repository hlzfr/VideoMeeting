package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class RoomListPresenter extends BaseRefreshablePresenter<Room> {

    RoomService roomService = RetrofitHelper.createService(RoomService.class);

    public RoomListPresenter(IRefreshableListView<Room> view) {
        super(view);
    }

    @Override
    public void refreshData() {
        roomService.getRoomList(1, RoomService.LIMIT_DEFINED_BY_SERVLET).enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<Room>> resultt) {
                refreshSuccess(resultt.body);
            }

            @Override
            public void onFailed(int statusCode, Result<Page<Room>> result) {
                refreshFailed(statusCode, result.message);
            }
        });
    }

    @Override
    public void loadMoreData(int page, int limit) {
        roomService.getRoomList(page, limit).enqueue(new SimpleCallback<Page<Room>>() {
            @Override
            public void onSuccess(int statusCode, Result<Page<Room>> result) {
                loadMoreSuccess(result.body);
            }

            @Override
            public void onFailed(int statusCode, Result<Page<Room>> result) {
                loadMoreFailed(statusCode, result.message);
            }
        });
    }
}
