package com.jb.vmeeting.page.activity;

import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.presenter.RoomChatPresenter;
import com.jb.vmeeting.mvp.view.IRoomChatView;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.utils.ViewUtils;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatActivity extends BaseActivity implements IRoomChatView{

    RoomChatPresenter roomChatPresenter;
    private GLSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);

        mSurfaceView = (GLSurfaceView) findViewById(R.id.sv_chat);
        roomChatPresenter = new RoomChatPresenter(this);
        bindPresenterLifeTime(roomChatPresenter);
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_chat);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    @Override
    public GLSurfaceView getSurfaceView() {
        return mSurfaceView;
    }

    @Override
    public Point getDisplaySize() {
        return ViewUtils.getDisplaySize(this);
    }

    @Override
    public String getRoomName() {
        // TODO get room name
        return "android_test";
    }
}
