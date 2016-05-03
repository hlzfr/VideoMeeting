package com.jb.vmeeting.page.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.app.App;
import com.jb.vmeeting.app.constant.APIConstant;
import com.jb.vmeeting.app.constant.NETCODE;
import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Page;
import com.jb.vmeeting.mvp.model.entity.PageInfo;
import com.jb.vmeeting.mvp.model.eventbus.event.RefreshEvent;
import com.jb.vmeeting.mvp.presenter.refreshlist.BaseRefreshablePresenter;
import com.jb.vmeeting.mvp.view.IRefreshableListView;
import com.jb.vmeeting.page.adapter.recyclerview.ArrayAdapter;
import com.jb.vmeeting.page.adapter.recyclerview.SimpleViewHolder;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 封装有下拉刷新功能的list fragment
 * 具体的view由子类确定
 *
 * Created by Jianbin on 2016/4/20.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IRefreshableListView<T>, SimpleViewHolder.OnItemClickListener, SimpleViewHolder.OnItemLongClickListener {

    protected BaseRefreshablePresenter<T> presenter;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected ArrayAdapter<T> adapter;

    protected PageInfo curPage;

    private boolean isRefreshing = false;
    private boolean isLoadMore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = createContentView(inflater, container, savedInstanceState);
        setRefreshPresenter(createPresenter(v));
        setSwipeRefreshLayout(createSwipeRefreshLayout(v));
        setRecyclerView(createRecyclerView(v), createAdapter(v));
        refresh();
        return v;
    }

    public void refresh() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    public abstract View createContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract BaseRefreshablePresenter<T> createPresenter(View contentView);

    public abstract ArrayAdapter<T> createAdapter(View contentView);

    public abstract RecyclerView createRecyclerView(View contentView);

    public abstract SwipeRefreshLayout createSwipeRefreshLayout(View contentView);

    public void setRefreshPresenter(BaseRefreshablePresenter<T> presenter) {
        this.presenter = presenter;
        bindPresenterLifeTime(this.presenter);
//        this.presenter.setRefreshableView(this);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout refreshLayout) {
        this.swipeRefreshLayout = refreshLayout;
        int colorBlue = ContextCompat.getColor(App.getInstance(), android.R.color.holo_blue_light);
        this.swipeRefreshLayout.setColorSchemeColors(colorBlue, colorBlue, colorBlue);
        this.swipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setRecyclerView(RecyclerView recyclerView, ArrayAdapter<T> adapter) {
        this.recyclerView = recyclerView;
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        }
        bindLoadMoreListener(recyclerView);
        recyclerView.setAdapter(adapter);
        setAdapter(adapter);
    }

    public void setAdapter(ArrayAdapter<T> adapter) {
        this.adapter = adapter;
        this.adapter.setOnItemLongClickListener(this);
        this.adapter.setOnItemClickListener(this);
    }

    public ArrayAdapter<T> getAdapter() {
        return adapter;
    }
    private void bindLoadMoreListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isRefreshing && !isLoadMore && dy > 0 && curPage != null && curPage.isHasNext()) {
                    int lastVisiblePosition = ViewUtils.getLastVisiblePosition(recyclerView);
                    if (lastVisiblePosition + getAheadCountLoadMore() >= adapter.getItemCount()) { // 提前10个数据就开始自动加载更多
                        isLoadMore = true;
                        //TODO show tips about load more显示加载更多的页面提示
                        presenter.loadMore(curPage.getCurPage() + 1, APIConstant.LIMIT_DEFINED_BY_SERVLET);
                    }
                }
            }
        });
    }

    /**
     * 返回提前多少个数据开始进行加载更多
     * @return
     */
    public int getAheadCountLoadMore() {
        return 10;
    }

    @Override
    public void onRefresh() {
        if (presenter == null) {
            throw new RuntimeException("please call setRefreshPresenter in onCreate or onCreateView");
        }
        if (isRefreshing) {
            return;
        }
        if (isLoadMore) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        isRefreshing = true;
        presenter.refresh();
    }

    @CallSuper
    @Override
    public void onRefreshSuccess(Page<T> page) {
        if (adapter == null) {
            throw new RuntimeException("please call setAdapter in onCreate or onCreateView");
        }
        curPage = page.getPageInfo();
        adapter.clear();
        adapter.addAll(page.getData());
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshFailed(int statusCode, int code, String message) {
        ToastUtil.toast("refresh failed. code:" + code + "; msg:" + message);
    }

    @Override
    public void onRefreshFinish() {
        if (swipeRefreshLayout == null) {
            throw new RuntimeException("Please call setSwipeRefreshLayout when setup views");
        }
        isRefreshing = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreSuccess(Page<T> page) {
        if (adapter == null) {
            throw new RuntimeException("please call setAdapter in onCreate or onCreateView");
        }
        curPage = page.getPageInfo();
        adapter.addAll(page.getData());
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreFailed(int statusCode, int code, String message) {
        ToastUtil.toast("refresh failed. code:" + code + "; msg:" + message);
    }

    @Override
    public void onLoadMoreFinish() {
        if (swipeRefreshLayout == null) {
            throw new RuntimeException("Please call setSwipeRefreshLayout when setup views");
        }
        isLoadMore = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNeedRefresh(RefreshEvent refreshEvent) {
        if (refreshEvent.needRefresh != null && refreshEvent.needRefresh.contains(this.getClass().getSimpleName())) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.setRefreshableView(null);
    }
}
