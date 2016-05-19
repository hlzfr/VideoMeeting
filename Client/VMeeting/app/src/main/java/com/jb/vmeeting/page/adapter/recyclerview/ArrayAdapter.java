package com.jb.vmeeting.page.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * RecyclerView的ArrayAdapter
 * Created by Jianbin on 2016/4/21.
 */
public abstract class ArrayAdapter<T> extends RecyclerView.Adapter<SimpleViewHolder> implements SimpleViewHolder.OnItemClickListener, SimpleViewHolder.OnItemLongClickListener {

    private SimpleViewHolder.OnItemClickListener onItemClickListener;
    private SimpleViewHolder.OnItemLongClickListener onItemLongClickListener;

    private List<T> mObjects;

    /**
     * 对mObjects的操作进行加锁保护
     */
    private final Object mLock = new Object();

    private boolean mNotifyOnChange = true;


    public ArrayAdapter() {
        this(new ArrayList<T>());
    }

    public ArrayAdapter(T[] objects) {
        this(Arrays.asList(objects));
    }

    public ArrayAdapter(List<T> objects) {
        mObjects = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder(onCreateItemView(parent, viewType), viewType);
        simpleViewHolder.setOnItemClickListener(this);
        simpleViewHolder.setOnItemLongClickListener(this);
        return simpleViewHolder;
    }

    public abstract View onCreateItemView(ViewGroup parent, int viewType);

    public void add(T object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void add(int pos, T object) {
        synchronized (mLock) {
            mObjects.add(pos, object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            mObjects.addAll(collection);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addAll(T... items) {
        synchronized (mLock) {
            Collections.addAll(mObjects, items);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        synchronized (mLock) {
            mObjects.add(index, object);
        }
        if (mNotifyOnChange) notifyItemInserted(index);
    }

    public void remove(int position) {
        synchronized (mLock) {
            mObjects.remove(position);
        }
        if (mNotifyOnChange) notifyItemRemoved(position);
    }

    public void remove(T object) {
        synchronized (mLock) {
            mObjects.remove(object);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
                Collections.sort(mObjects, comparator);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * 设置调用其他方法时是否自动notifyDataSetChanged
     * @param notifyOnChange
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public List<T> getData() {
        return mObjects;
    }

    public int getCount() {
        return mObjects.size();
    }

    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(SimpleViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(SimpleViewHolder.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        if (onItemLongClickListener != null) {
            return onItemLongClickListener.onItemLongClick(view, position);
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(view, position);
        }
    }
}
