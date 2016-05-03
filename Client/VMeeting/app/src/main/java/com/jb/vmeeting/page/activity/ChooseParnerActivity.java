package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.page.fragment.ChooseParticipatorFragment;

import java.util.ArrayList;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class ChooseParnerActivity extends BaseActivity implements BaseActivity.CreateFragmentCallback {

    ArrayList<User> notInclude;

    ChooseParticipatorFragment fragment;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_frame);

        setupToolBar();
        setToolBarCanBack();
    }

    @Override
    protected void setupListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_complete) {
            if (fragment != null) {
                fragment.postCompleteEvent();
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if (bundle != null) {
            notInclude = (ArrayList<User>) bundle.getSerializable(IntentConstant.BUNDLE_KEY_USER_LIST);
        }
        switchFragment(R.id.frame_content, "ChooseParner", this);
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
        return fragment == null ? fragment = ChooseParticipatorFragment.newInstance(notInclude) : fragment;
    }
}
