package com.jb.vmeeting.page.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.jb.vmeeting.R;
import com.jb.vmeeting.page.adapter.viewpager.ImageAdapter;
import com.jb.vmeeting.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Jianbin on 2016/5/19.
 */
public class PptViewPager extends RelativeLayout {

    List<String> ppts = new ArrayList<>();
    ViewPager mViewPager;
    DotNavView dotNavView;
    ImageAdapter imageAdapter;

    public PptViewPager(Context context) {
        super(context);
        init();
    }

    public PptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PptViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PptViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_ppt_viewpager, this, true);
        mViewPager = ViewUtils.find(this, R.id.vp_ppt_viewpager);
        dotNavView = ViewUtils.find(this, R.id.dot_ppt_viewpager);
        imageAdapter = new ImageAdapter(ppts);
        mViewPager.setAdapter(imageAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotNavView.setCurrentPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public ImageAdapter getAdapter() {
        return imageAdapter;
    }

    public void setOnItemLongClickListener(ImageAdapter.OnItemLongClickListener l) {
        imageAdapter.setOnItemLongClickListener(l);
    }

    public void setOnItemClickListener(ImageAdapter.OnItemClickListener l) {
        imageAdapter.setOnItemClickListener(l);
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, true);
    }

    public void add(String url) {
        ppts.add(url);
        dotNavView.add();
        imageAdapter.notifyDataSetChanged();
    }

    public void delete(String url) {
        ppts.remove(url);
        dotNavView.sub();
        imageAdapter.notifyDataSetChanged();
    }

    public List<String> getPpts() {
        return ppts;
    }

    public void addAll(List<String> urls) {
        ppts.addAll(urls);
        dotNavView.setCount(urls.size());
        imageAdapter.notifyDataSetChanged();
    }

    public void clear() {
        ppts.clear();
        dotNavView.setCount(0);
        imageAdapter.notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.addOnPageChangeListener(listener);
    }
}
