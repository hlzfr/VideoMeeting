package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.presenter.RoomChatPresenter;
import com.jb.vmeeting.mvp.view.IRoomChatView;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.utils.ViewUtils;

/**
 * Created by Jianbin on 16/3/15.
 */
public class VideoChatActivity extends BaseActivity implements IRoomChatView{

    RoomChatPresenter roomChatPresenter;
    private GLSurfaceView mSurfaceView;

    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                /*WindowManager.LayoutParams.FLAG_FULLSCREEN
                        |*/ WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        roomChatPresenter = new RoomChatPresenter(this);
        bindPresenterLifeTime(roomChatPresenter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chat);
        mSurfaceView = (GLSurfaceView) findViewById(R.id.sv_chat);

        setupToolBar();
        setToolBarCanBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_room_detail) {
            PageNavigator.getInstance().toRoomDetailActivity(this, room);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if (bundle != null) {
            room = (Room) bundle.getSerializable(IntentConstant.BUNDLE_KEY_ROOM);
        }
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
        return room.getId();
    }
}
