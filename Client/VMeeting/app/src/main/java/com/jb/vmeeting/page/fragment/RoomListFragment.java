package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;

/**
 * Created by Jianbin on 2016/4/20.
 */
public class RoomListFragment extends SimpleListFragment<Room> {

    @Override
    public BaseRefreshablePresenter<Room> createPresenter(View contentView) {
        return null;
    }

    @Override
    public ArrayAdapter<Room> createAdapter(View contentView) {
        return null;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

}
