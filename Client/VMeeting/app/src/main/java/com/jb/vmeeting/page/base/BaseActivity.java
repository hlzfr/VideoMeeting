package com.jb.vmeeting.page.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.presenter.PresenterLifeTime;


/**
 * BaseActivity
 * Created by Jianbin on 2015/12/7.
 */
public class BaseActivity extends AppCompatActivity {

    private Bundle mBundle;
    private PresenterLifeTime pLife;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
        initViews();
        setupListener();
    }

    /**
     * should call in {@link #onCreate(Bundle)},
     * or life time {@link PresenterLifeTime#onCreate()} may be called in wrong time.
     * @param pLife
     */
    protected void bindPresenterLifeTime(PresenterLifeTime pLife) {
        this.pLife = pLife;
        if (this.pLife != null) {
            this.pLife.onCreate();
        }
    }

    @CallSuper
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (null != intent) {
            mBundle = intent.getBundleExtra(IntentConstant.INTENT_EXTRA_BUNDLE);
            onHandleIntent(intent, mBundle);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.pLife != null) {
            this.pLife.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (this.pLife != null) {
            this.pLife.onRestart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.pLife != null) {
            this.pLife.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.pLife != null) {
            this.pLife.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.pLife != null) {
            this.pLife.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.pLife != null) {
            this.pLife.onDestroy();
            this.pLife = null;
        }
    }

    protected void onHandleIntent(Intent intent, Bundle bundle) {

    }

    protected Bundle getBundle() {
        return mBundle;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) super.findViewById(id);
    }

    protected void initViews() {

    }

    protected void setupListener() {

    }
}
