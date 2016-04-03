package com.jb.vmeeting.page.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.presenter.VideoChatPresenter;
import com.jb.vmeeting.mvp.view.IVideoChatView;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.ToastUtil;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatActivity extends BaseActivity implements IVideoChatView{

    VideoChatPresenter mVideoChatPresenter;

    Button btnToggleChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mVideoChatPresenter = new VideoChatPresenter(this);
        btnToggleChat = (Button) findViewById(R.id.btn_chat_toggle);
    }

    public void toggleChat(View view) {
        if (mVideoChatPresenter.isStreaming()) {
            mVideoChatPresenter.stopStream();
            btnToggleChat.setText("Start");
        } else {
            mVideoChatPresenter.startStream();
            btnToggleChat.setText("Stop");
        }
    }

    @Override
    public String getChannelName() {
        return "438";
    }

    @Override
    public void onStartStream() {
        ToastUtil.toast("onStartStream");
    }

    @Override
    public void onStopStream() {
        ToastUtil.toast("onStopStream");
    }

    @Override
    public void channelNameError(String message) {
        ToastUtil.toast("channelNameError " + message);
    }

    @Override
    public SurfaceView getSurfaceView() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.sv_chat);
        surfaceView.getHolder().setFixedSize(getResources().getDisplayMetrics().widthPixels,
                getResources().getDisplayMetrics().heightPixels);
        return surfaceView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoChatPresenter.destroy();
    }
}
