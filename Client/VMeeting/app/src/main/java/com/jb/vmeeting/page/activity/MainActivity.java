package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.eventbus.event.LogoutEvent;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.base.BaseActivity.CreateFragmentCallback;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.page.fragment.MineFragment;
import com.jb.vmeeting.page.fragment.PersonFragment;
import com.jb.vmeeting.page.fragment.RoomsFragment;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.tools.account.AccountManager;

/**
 * 首页
 * Created by Jianbin on 2016/3/13.
 */
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener, CreateFragmentCallback {

    BottomNavigationBar mBottomBar;
    String[] mNavTabs;
    int[] mIcons;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountManager.getInstance().checkLogin(this, true, false);

        mNavTabs = new String[]{App.getInstance().getString(R.string.nav_main_rooms),
                App.getInstance().getString(R.string.nav_main_mine),
                App.getInstance().getString(R.string.nav_main_person)};

        mIcons = new int[]{R.drawable.ic_home, R.drawable.ic_mine, R.drawable.ic_person};

        mBottomBar = findView(R.id.bnb_main);

        for (int i = 0; i < mNavTabs.length; i++) {
            mBottomBar.addItem(new BottomNavigationItem(mIcons[i], mNavTabs[i]).setActiveColor(R.color.bottom_bar_active));
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
    public void onLogoutEvent(LogoutEvent logoutEvent) {
        PageNavigator.getInstance().toLoginActivity(this, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
            return RoomsFragment.newInstance();
        } else if(tag.equals(mNavTabs[1])) { // mine
            return MineFragment.newInstance();
        } else if(tag.equals(mNavTabs[2])) { // person
            return PersonFragment.newInstance();
        }
        return null;
    }

    @Override
    public void switchFragment(@NonNull int fContentId, @NonNull String tag, @NonNull CreateFragmentCallback callback) {
        super.switchFragment(fContentId, tag, callback);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(tag);
        }
        if (tag.equals(mNavTabs[0])) { // rooms
            if (actionBar != null) {
                actionBar.show();
            }
        } else if (tag.equals(mNavTabs[1])) { // mine
            if (actionBar != null) {
                actionBar.show();
            }
        } else if (tag.equals(mNavTabs[2])) { // person
            if (actionBar != null) {
                actionBar.hide();
            }
        }
    }
}
