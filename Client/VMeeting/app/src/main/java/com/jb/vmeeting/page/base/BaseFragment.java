package com.jb.vmeeting.page.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.mvp.presenter.PresenterLifeTime;

/**
 * BaseFragment
 * Created by Jianbin on 2015/12/7.
 */
public abstract class BaseFragment extends Fragment {

    private PresenterLifeTime pLife;
    private boolean pLifeHasStarted = false;

    private View rootView;

    /**
     * should call in {@link #onCreate(Bundle)} or {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)},
     * or life time {@link PresenterLifeTime#onCreate()} may be called in wrong time.
     * @param pLife
     */
    public void bindPresenterLifeTime(PresenterLifeTime pLife) {
        this.pLife = pLife;
        if (this.pLife != null) {
            this.pLife.onCreate();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // is this root view cache useful?
        if (rootView == null) {
            rootView = createView(inflater, container, savedInstanceState);
        }
        return rootView;
    }

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void onStart() {
        super.onStart();
        if (this.pLife != null) {
            if (pLifeHasStarted) {
                this.pLife.onRestart();
            }
            this.pLife.onStart();
            pLifeHasStarted = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.pLife != null) {
            this.pLife.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (this.pLife != null) {
            this.pLife.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (this.pLife != null) {
            this.pLife.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.pLife != null) {
            this.pLife.onDestroy();
        }
    }
}
