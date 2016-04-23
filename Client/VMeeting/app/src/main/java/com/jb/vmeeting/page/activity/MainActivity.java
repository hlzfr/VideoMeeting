package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jb.vmeeting.R;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.utils.PageNavigator;

/**
 * 首页
 * Created by Jianbin on 2016/3/13.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {

    }

    public void toSignUpActivity(View view) {
        PageNavigator.getInstance().toSignUpActivity(this);
    }

    public void toLoginActivity(View view) {
        PageNavigator.getInstance().toLoginActivity(this);
    }

    public void toVideoChatActivity(View view) {
        PageNavigator.getInstance().toChatActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_room) {
            PageNavigator.getInstance().toRoomCreateActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
