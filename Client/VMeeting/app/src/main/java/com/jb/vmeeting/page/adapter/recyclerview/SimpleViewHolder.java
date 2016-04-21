package com.jb.vmeeting.page.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Jianbin on 2016/4/21.
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder{

    SparseArray<View> viewCache;
    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    return onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                }
                return false;
            }
        });
        viewCache = new SparseArray<>();
    }

    public <T extends View> T find(int id) {
        View v = viewCache.get(id);
        if (v == null) {
            v =  this.itemView.findViewById(id);
            viewCache.put(id, v);
        }
        return (T) v;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}
