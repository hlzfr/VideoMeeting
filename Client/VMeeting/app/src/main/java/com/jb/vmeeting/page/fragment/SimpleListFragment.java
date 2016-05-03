package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.page.base.BaseListFragment;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.utils.ViewUtils;

/**
 * Created by Jianbin on 2016/4/21.
 */
public abstract class SimpleListFragment<T> extends BaseListFragment<T> {

    TextView tip;

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        tip = ViewUtils.find(v, R.id.tv_refresh_list_tip);
        return v;
    }

    @Override
    public RecyclerView createRecyclerView(View contentView) {
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return recyclerView;
    }

    @Override
    public SwipeRefreshLayout createSwipeRefreshLayout(View contentView) {
        return (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_layout);
    }

    @Override
    public void onRefreshSuccess(Page<T> page) {
        super.onRefreshSuccess(page);
        if (page.getData() == null || page.getData().size() == 0) {
            showTip("没有房间");
        } else {
            hideTip();
        }
    }

    @Override
    public void onRefreshFailed(int statusCode, int code, String message) {
        super.onRefreshFailed(statusCode, code, message);
        if (getAdapter().getCount() == 0) {
            showTip("没有房间");
        } else {
            hideTip();
        }
    }

    public void showTip(int msgId) {
        showTip(App.getInstance().getString(msgId));
    }

    public void showTip(String msg) {
        if (tip.getVisibility() == View.GONE) {
            tip.setVisibility(View.VISIBLE);
        }
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.INVISIBLE);
        }
        tip.setText(msg);
    }

    public void hideTip() {
        if (tip.getVisibility() == View.VISIBLE) {
            tip.setVisibility(View.GONE);
            tip.setText("");
        }
        if (recyclerView.getVisibility() == View.INVISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void setTipClickListener(View.OnClickListener clickListener) {
        tip.setOnClickListener(clickListener);
    }
}
