package com.jb.vmeeting.page.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jb.vmeeting.R;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.utils.ViewUtils;

/**
 * Created by Jianbin on 2016/5/20.
 */
public class DotNavView  extends RelativeLayout{

    LinearLayout dotContent;
    int mCount = 0;

    private int currentPos = 0;

    public DotNavView(Context context) {
        super(context);
        init(context);
    }

    public DotNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DotNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotNavView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.custom_dot_nav, this, true);
        dotContent = ViewUtils.find(this, R.id.dot_content);
    }

    public void setCurrentPosition(int position) {
        if (dotContent.getChildCount() != mCount || mCount <= position || position < 0) {
            L.e("position " + position + " out of size " + (mCount - 1));
            return;
        }
        currentPos = position;
        View child;
        for (int i = 0; i < mCount; i++) {
            child = dotContent.getChildAt(i);
            if (position == i) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }
        postInvalidate();
    }

    public void add() {
        setCount(getCount() + 1);
    }

    public void sub() {
        setCount(getCount() - 1);
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
        notifyDot();
    }

    public void notifyDot() {
        dotContent.removeAllViews();
        ImageView imageView;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20,20);
        layoutParams.setMargins(5,5,5,5);
        for (int i = 0; i < mCount; i++) {
            imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.selector_dot);
            imageView.setLayoutParams(layoutParams);
            dotContent.addView(imageView);
        }
        setCurrentPosition(currentPos);
    }
}
