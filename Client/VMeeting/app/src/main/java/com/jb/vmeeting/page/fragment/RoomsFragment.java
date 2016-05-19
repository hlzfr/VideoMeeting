package com.jb.vmeeting.page.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.RefreshEvent;
import com.jb.vmeeting.mvp.presenter.RoomListPresenter;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.RoomListAdapter;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.account.AccountManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jianbin on 2016/4/20.
 */
public class RoomsFragment extends SimpleListFragment<Room> {
    User currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();

    public static RoomsFragment newInstance() {
        return new RoomsFragment();
    }

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
        Room room = getAdapter().getItem(position);
        if (room != null && !TextUtils.isEmpty(room.getId())) {
            PageNavigator.getInstance().toChatActivity(getActivity(), room);
        }
    }

    @Override
    public boolean onItemLongClick(View view, final int position) {
        String[] choice;
        if (currentUser.getUsername().equals(getAdapter().getItem(position).getOwnerName())) {
            choice = new String[]{"查看详情", "文件列表", "编辑"};
        } else {
            choice = new String[]{"查看详情", "文件列表"};
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setIcon(R.drawable.ic_launcher);
//        builder.setTitle("");
        builder.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        PageNavigator.getInstance().toRoomDetailActivity(getActivity(), getAdapter().getItem(position));
                        break;
                    case 1:
                        // 前往文件列表管理页面
                        PageNavigator.getInstance().toRoomFilesActivity(getActivity(), getAdapter().getItem(position));
                        break;
                    case 2:
                        PageNavigator.getInstance().toRoomCreateActivity(getActivity(), getAdapter().getItem(position));
                        break;
                }
            }
        });
        builder.show();
        return false;
    }
}
