package com.jb.vmeeting.page.custom;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 自适应子View高度的ListView
 * 嵌套在
 * Created by Jianbin on 2016/4/27.
 */
public class WrapHeightListView extends ListView {

    private boolean mIsSetHeight = false;

    public WrapHeightListView(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
//        setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    // 根据item重置ListView高度
    public void setHeightBasedOnChildren() {
        mIsSetHeight = true;
        ListAdapter listAdapter = getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = getPaddingTop() + getPaddingBottom();
        final int itemCount = listAdapter.getCount();
        for (int i = 0; i < itemCount; i++) {
            View listItem = listAdapter.getView(i, null, this);
            if (listItem != null) {
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = totalHeight + (getDividerHeight() * (itemCount - 1));
        setLayoutParams(params);
    }

    public boolean isSetHeight() {
        return mIsSetHeight;
    }
}