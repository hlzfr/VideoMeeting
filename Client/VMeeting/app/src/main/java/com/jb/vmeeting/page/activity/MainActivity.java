package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.base.BaseActivity.CreateFragmentCallback;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.page.fragment.RoomListFragment;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.utils.ViewUtils;

/**
 * 首页
 * Created by Jianbin on 2016/3/13.
 */
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, CreateFragmentCallback {

    BottomNavigationBar mBottomBar;
    String[] mNavTabs;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavTabs = new String[]{App.getInstance().getString(R.string.nav_main_rooms),
                App.getInstance().getString(R.string.nav_main_mine),
                App.getInstance().getString(R.string.nav_main_person)};

        mBottomBar = findView(R.id.bnb_main);

        for (int i = 0; i < mNavTabs.length; i++) {
            mBottomBar.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, mNavTabs[i]));
        }
        mBottomBar.setFirstSelectedPosition(0).initialise();
        switchFragment(R.id.frame_main_content, mNavTabs[0], this);
    }

    @Override
    protected void setupListener() {
        mBottomBar.setTabSelectedListener(this);
    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {

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

    @Override
    public void onTabSelected(int position) {
        switchFragment(R.id.frame_main_content, mNavTabs[position], this);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public FragmentManager getFragmentManager(BaseActivity activity, String tag) {
        return getSupportFragmentManager();
    }

    @Override
    public FragmentTransaction onGetTransaction(BaseActivity activity, String tag) {
        return getSupportFragmentManager().beginTransaction();
    }

    @Override
    public BaseFragment onCreateFragment(String tag) {
        if(tag.equals(mNavTabs[0])) { // rooms
            return RoomListFragment.newInstance();
        } else if(tag.equals(mNavTabs[1])) { // mine
            return RoomListFragment.newInstance();
        } else if(tag.equals(mNavTabs[2])) { // person
            return RoomListFragment.newInstance();
        }
        return null;
    }
}
