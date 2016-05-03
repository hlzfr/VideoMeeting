package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.page.adapter.recyclerview.RoomParnerAdapter;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.custom.DateTimePickDialogUtil;
import com.jb.vmeeting.page.utils.ToastUtil;

/**
 * 房间详情介绍页面
 * Created by Jianbin on 2016/4/27.
 */
public class RoomDetailActivity extends BaseActivity {

    TextView tvRoomName;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvRoomDes;
    RecyclerView rvParner;
    View emptyParnerTip;

    RoomParnerAdapter adapter;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_room_detail);

        setupToolBar("房间详情");
        setToolBarCanBack();

        tvRoomName = findView(R.id.tv_room_detail_name);
        tvStartTime = findView(R.id.tv_room_detail_start_time);
        tvEndTime = findView(R.id.tv_room_detail_end_time);
        tvRoomDes = findView(R.id.tv_room_detail_describe);
        rvParner = findView(R.id.listview_room_detail_parner);
        emptyParnerTip = findView(R.id.tv_room_detail_parner_empty);

        rvParner.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomParnerAdapter();
        adapter.setEditable(false);

        rvParner.setAdapter(adapter);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        Room room = null;
        if (bundle != null) {
            room = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
        }
//        if (room == null) {
//            room = new Room();
//        }
        updateRoomUI(room);
    }

    public void updateRoomUI(Room room) {
        if (room == null) {
            ToastUtil.toast("房间无效");
            finish();
            return ;
        }
        if(!TextUtils.isEmpty(room.getName())) {
            tvRoomName.setText(room.getName());
        }
        tvStartTime.setText(DateTimePickDialogUtil.long2StringTime(room.getStartTime()));
        tvEndTime.setText(DateTimePickDialogUtil.long2StringTime(room.getEndTime()));
        if(!TextUtils.isEmpty(room.getDescribe())) {
            tvRoomDes.setText(room.getDescribe());
        }

        updateParnerUI(room);
    }

    public void updateParnerUI(Room room) {
        if (room.getParticipator() != null && room.getParticipator().size() > 0) {
            emptyParnerTip.setVisibility(View.GONE);
            rvParner.setVisibility(View.VISIBLE);
            adapter.clear();
            adapter.addAll(room.getParticipator());
            adapter.notifyDataSetChanged();
        } else {
            emptyParnerTip.setVisibility(View.VISIBLE);
            rvParner.setVisibility(View.GONE);
        }
    }
}
