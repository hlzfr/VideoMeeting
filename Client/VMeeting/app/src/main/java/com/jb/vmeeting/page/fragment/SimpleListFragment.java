package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.page.base.BaseListFragment;

/**
 * TODO complete it
 * Created by Jianbin on 2016/4/21.
 */
public abstract class SimpleListFragment<T> extends BaseListFragment<T> {

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public RecyclerView createRecyclerView(View contentView) {
        return null;
    }

    @Override
    public SwipeRefreshLayout createSwipeRefreshLayout(View contentView) {
        return null;
    }
}
