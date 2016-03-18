package com.jb.vmeeting.page.activity;

import android.os.Bundle;

import com.jb.vmeeting.mvp.view.IVideoChatView;
import com.jb.vmeeting.page.base.BaseActivity;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatActivity extends BaseActivity implements IVideoChatView {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onReceive(byte[] data) {
        //TODO
    }

    @Override
    public void onUserJoined() {
        //TODO
    }

    @Override
    public void onUserLeave() {
        //TODO
    }

    @Override
    public void onJoinSuccess() {
        //TODO
    }

    @Override
    public void onJoinFailed() {
        //TODO
    }

    @Override
    public void onConnectionLost() {
        //TODO
    }
}
