package com.jb.vmeeting.page.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.mvp.presenter.PresenterLifeTime;
import com.jb.vmeeting.tools.L;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * BaseActivity
 * Created by Jianbin on 2015/12/7.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Bundle mBundle;
    private PresenterLifeTime pLife;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mBundle = intent.getBundleExtra(IntentConstant.INTENT_EXTRA_BUNDLE);
        initViews(savedInstanceState);
        setupListener();
        // 对intent的处理可能涉及到视图,所以要在initViews之后调用
        handleIntent(intent);
    }

    /**
     * 是否需要通过注释super.onSaveInstanceState来解决fragment的getActivity为空的问题？
     * 若使用{@link #switchFragment(int, String, CreateFragmentCallback)}，
     * 由于findFragmentByTag，所以似乎不会有该问题？不确定
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void setupListener();

    protected abstract void onHandleIntent(Intent intent, Bundle bundle);

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
        setIntent(intent);
        mBundle = intent.getBundleExtra(IntentConstant.INTENT_EXTRA_BUNDLE);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (null != intent) {
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

    protected Bundle getBundle() {
        return mBundle;
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) super.findViewById(id);
    }

    protected Fragment mCurFragment;

    public void switchFragment(@NonNull int fContentId,@NonNull String tag, @NonNull CreateFragmentCallback callback) {
        String newTag = makeFragmentName(fContentId, tag);
        Fragment to = callback.getFragmentManager(this, tag).findFragmentByTag(newTag);
        if (to == null) {
            L.d("create fragment " + tag);
            to = callback.onCreateFragment(tag);
        }
        if (mCurFragment != to) {
            FragmentTransaction transaction = callback.onGetTransaction(this, tag);
            if (mCurFragment == null) {
                List<Fragment> fragments = callback.getFragmentManager(this, tag).getFragments();
                if (fragments != null) { // 是内存重启
                    for (int i = 0; i < fragments.size(); i++) {
                        L.d("hide all fragments");
                        transaction.hide(fragments.get(i));
                    }
                }
            }
            if (!to.isAdded()) {    // 先判断是否被add过
                if (mCurFragment != null) {
                    transaction.hide(mCurFragment);
                }
                L.d("add fragment " + tag);
                transaction.add(fContentId, to, newTag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                if (mCurFragment != null) {
                    transaction.hide(mCurFragment);
                }
                L.d("show fragment " + tag);
                transaction.show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
//            callback.getFragmentManager(this, tag).executePendingTransactions();
            mCurFragment = to;
        } else {
            L.d("mCurFragment == to");
        }
    }

    public interface CreateFragmentCallback {
        FragmentManager getFragmentManager(BaseActivity activity, String tag);
        FragmentTransaction onGetTransaction(BaseActivity activity, String tag);
        BaseFragment onCreateFragment(String tag);
    }

    private static String makeFragmentName(int viewId, String tag) {
        return "base_activity:switcher:" + viewId + ":" + tag;
    }
}
