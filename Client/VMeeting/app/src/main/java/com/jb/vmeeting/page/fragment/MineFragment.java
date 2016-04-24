package com.jb.vmeeting.page.fragment;

import android.view.View;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.RoomListAdapter;

/**
 * Created by Jianbin on 2016/4/24.
 */
public class MineFragment extends SimpleListFragment<Room> {
    @Override
    public BaseRefreshablePresenter<Room> createPresenter(View contentView) {
        return null;
    }

    @Override
    public ArrayAdapter<Room> createAdapter(View contentView) {
        return new RoomListAdapter(getActivity());
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }
}
