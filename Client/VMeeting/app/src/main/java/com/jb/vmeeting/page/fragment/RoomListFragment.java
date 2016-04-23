package com.jb.vmeeting.page.fragment;

import android.view.View;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.presenter.RoomListPresenter;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.RoomListAdapter;
import com.jb.vmeeting.page.utils.ToastUtil;

/**
 * Created by Jianbin on 2016/4/20.
 */
public class RoomListFragment extends SimpleListFragment<Room> {

    @Override
    public BaseRefreshablePresenter<Room> createPresenter(View contentView) {
        return new RoomListPresenter(this);
    }

    @Override
    public ArrayAdapter<Room> createAdapter(View contentView) {
        return new RoomListAdapter(getActivity());
    }

    @Override
    public void onItemClick(View view, int position) {
        ToastUtil.toast("onItemClick " + position);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        ToastUtil.toast("onItemLongClick " + position);
        return false;
    }
}
