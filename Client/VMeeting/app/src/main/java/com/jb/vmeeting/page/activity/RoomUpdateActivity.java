package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.eventbus.event.AddParticipatorEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.RefreshEvent;
import com.jb.vmeeting.mvp.model.eventbus.event.RemoveParticipatorEvent;
import com.jb.vmeeting.mvp.presenter.RoomUpdatePresenter;
import com.jb.vmeeting.mvp.view.IUpdateRoomView;
import com.jb.vmeeting.page.adapter.recyclerview.RoomParnerAdapter;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.custom.DateTimePickDialogUtil;
import com.jb.vmeeting.page.fragment.MineFragment;
import com.jb.vmeeting.page.fragment.RoomsFragment;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

/**
 * Created by Jianbin on 2016/4/23.
 */
public class RoomUpdateActivity extends BaseActivity implements IUpdateRoomView {

    RoomUpdatePresenter presenter;
    EditText edtRoomName;
    EditText edtRoomStartTime;
    EditText edtRoomEndTime;
    EditText edtRoomDescribe;
    RecyclerView listViewRoomParner;

    RoomParnerAdapter adapter;

    private Room room;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RoomUpdatePresenter(this);
        bindPresenterLifeTime(presenter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_room_update);
        setupToolBar();
        setToolBarCanBack();

        edtRoomName = findView(R.id.edt_create_room_name);
        edtRoomStartTime = findView(R.id.edt_create_room_start_time);
        edtRoomEndTime = findView(R.id.edt_create_room_end_time);
        edtRoomDescribe = findView(R.id.edt_create_room_describe);
        listViewRoomParner = findView(R.id.listview_create_room_parner);
        listViewRoomParner.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoomParnerAdapter();

        listViewRoomParner.setAdapter(adapter);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if (bundle != null) {
            room = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
        }
        if (room == null) {
            room = new Room();
        }
        updateRoomInfoUI(room);
    }

    public void updateRoomInfoUI(Room room) {
        if (room == null) {
            return;
        }
        this.room = room;
        if(!TextUtils.isEmpty(room.getName())) {
            edtRoomName.setText(room.getName());
        }
        edtRoomStartTime.setText(DateTimePickDialogUtil.long2StringTime(room.getStartTime()));
        edtRoomEndTime.setText(DateTimePickDialogUtil.long2StringTime(room.getEndTime()));
        if(!TextUtils.isEmpty(room.getDescribe())) {
            edtRoomDescribe.setText(room.getDescribe());
        }

        updateParnerUI(room);
    }

    public void updateParnerUI(Room room) {
        this.room = room;
        if (room.getParticipator() != null && room.getParticipator().size() > 0) {
            findView(R.id.tv_create_room_parner_empty).setVisibility(View.GONE);
            listViewRoomParner.setVisibility(View.VISIBLE);
            adapter.clear();
            adapter.addAll(room.getParticipator());
            adapter.notifyDataSetChanged();
        } else {
            findView(R.id.tv_create_room_parner_empty).setVisibility(View.VISIBLE);
            listViewRoomParner.setVisibility(View.GONE);
        }
    }

    public String getRoomName() {
        return edtRoomName.getText().toString();
    }

    @Override
    public void onUpdateSuccess(Room room) {
        ToastUtil.toast("update success!");
        EventBus.getDefault().post(new RefreshEvent(new String[]{RoomsFragment.class.getSimpleName(), MineFragment.class.getSimpleName()}));
        this.room = room;
        PageNavigator.getInstance().toMainActivity(this);
        finish();
    }

    @Override
    public void onUpdateFailed(int code, String message) {
        ToastUtil.toast("update failed!" + message);
//        ((TextView) findView(R.id.tv_create_room_result)).setText("failed. code:" + code + "; message:" + message);
    }

    public void updateRoom(View view) {
        if (!AccountManager.getInstance().checkLogin(this, true, true)) {
            return;
        }
        if (TextUtils.isEmpty(room.getName())) {
            ToastUtil.toast("请输入房间名");
        }
        room.setName(getRoomName());
        room.setDescribe(edtRoomDescribe.getText().toString());
        presenter.updateRoom(room);
    }

    public void chooseStartTime(View view) {
        new DateTimePickDialogUtil(this, edtRoomStartTime.getText().toString()).setOnClickListener(new DateTimePickDialogUtil.OnClickListener() {
            @Override
            public void onCancelListener() {

            }

            @Override
            public void onOkListener(Date time) {
                room.setStartTime(time.getTime());
                edtRoomStartTime.setText(DateTimePickDialogUtil.long2StringTime(time.getTime()));
            }
        }).show();
    }

    public void chooseEndTime(View view) {
        new DateTimePickDialogUtil(this, edtRoomEndTime.getText().toString()).setOnClickListener(new DateTimePickDialogUtil.OnClickListener() {
            @Override
            public void onCancelListener() {

            }

            @Override
            public void onOkListener(Date time) {
                room.setEndTime(time.getTime());
                edtRoomEndTime.setText(DateTimePickDialogUtil.long2StringTime(time.getTime()));
            }
        }).show();
    }

    /**
     * 添加参与者
     *
     * @param view
     */
    public void addParticipator(View view) {
        // 前往成员选择页面
        PageNavigator.getInstance().toChooseParnerActivity(this, room.getParticipator());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParnerAdd(AddParticipatorEvent event) {
        L.d("onParnerAdd "+event.participator.toString());
        room.getParticipator().addAll(event.participator);
        updateParnerUI(room);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onParnerRemove(RemoveParticipatorEvent event) {
        L.d("onParnerRemove "+event.participator.toString());
        room.getParticipator().removeAll(event.participator);
        updateParnerUI(room);
    }
}
