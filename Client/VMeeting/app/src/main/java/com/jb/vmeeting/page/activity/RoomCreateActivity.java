package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.presenter.RoomCreatePresenter;
import com.jb.vmeeting.mvp.view.ICreateRoomView;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * Created by Jianbin on 2016/4/23.
 */
public class RoomCreateActivity extends BaseActivity implements ICreateRoomView{

    RoomCreatePresenter presenter;
    EditText edtRoomName;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_room);
        presenter = new RoomCreatePresenter(this);
        bindPresenterLifeTime(presenter);

        edtRoomName = findView(R.id.edt_create_room_name);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {

    }

    @Override
    public String getRoomName() {
        return edtRoomName.getText().toString();
    }

    @Override
    public void onCreateSuccess(Room room) {
        ToastUtil.toast("create success!");
        ((TextView)findView(R.id.tv_create_room_result)).setText(room.toString());
    }

    @Override
    public void onCreateFailed(int code, String message) {
        ToastUtil.toast("create failed!");
        ((TextView) findView(R.id.tv_create_room_result)).setText("failed. code:" + code + "; message:" + message);
    }

    public void createRoom(View view) {
        if (!AccountManager.getInstance().checkLogin(this, true, true)) {
            return;
        }
        presenter.createRoom();
    }
}
