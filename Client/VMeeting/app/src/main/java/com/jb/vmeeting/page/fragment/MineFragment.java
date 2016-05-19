package com.jb.vmeeting.page.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.NETCODE;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.RefreshEvent;
import com.jb.vmeeting.mvp.presenter.MyRoomListPresenter;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.RoomListAdapter;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Jianbin on 2016/4/24.
 */
public class MineFragment extends SimpleListFragment<Room> {

    User currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.createView(inflater, container, savedInstanceState);
        Toolbar toolbar = ViewUtils.find(v, R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.nav_main_person);
        }
        return v;
    }

    @Override
    public BaseRefreshablePresenter<Room> createPresenter(View contentView) {
        return new MyRoomListPresenter(this);
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

//    private View.OnClickListener click2Login = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            PageNavigator.getInstance().toLoginActivity(getActivity());
//        }
//    };
}
