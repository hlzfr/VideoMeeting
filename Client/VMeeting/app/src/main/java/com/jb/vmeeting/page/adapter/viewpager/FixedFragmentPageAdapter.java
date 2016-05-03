package com.jb.vmeeting.page.adapter.viewpager;

import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jb.vmeeting.utils.ViewUtils;

/**
 * 修复 Android4.1.x 上的点击穿透问题
 * TODO to test 还未测试
 * Created by Jianbin on 2016/4/24.
 */
public abstract class FixedFragmentPageAdapter extends FragmentPagerAdapter {

    public static final boolean NEED_FIX = android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN; // sdk int 16

    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;

    public FixedFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (NEED_FIX) {
            return instantiateItemFixed(container, position);
        } else {
            return super.instantiateItem(container, position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (NEED_FIX) {
            destroyItemFixed(container, position, object);
        } else {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (NEED_FIX) {
            // do nothing
        } else {
            super.setPrimaryItem(container, position, object);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (NEED_FIX) {
            finishUpdateFixed(container);
        } else {
            super.finishUpdate(container);
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (NEED_FIX) {
            return isViewFromObjectFixed(view, object);
        } else {
            return super.isViewFromObject(view, object);
        }
    }

    @Override
    public Parcelable saveState() {
        if (NEED_FIX) {
            return saveStateFixed();
        } else {
            return super.saveState();
        }
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (NEED_FIX) {
            restoreStateFixed(state, loader);
        } else {
            super.restoreState(state, loader);
        }
    }

    /* ---- fix functions ---- */

    public Object instantiateItemFixed(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        FrameLayout layout = new FrameLayout(container.getContext());
//        View layout = new View(container.getContext());
        layout.setId(ViewUtils.generateViewId());

        final long itemId = getItemId(position);

        // Do we already have this fragment?
        String name = makeFragmentName(layout.getId(), itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        if (fragment != null) {
            mCurTransaction.attach(fragment);
        } else {
            fragment = getItem(position);
            mCurTransaction.add(layout.getId(), fragment,
                    makeFragmentName(layout.getId(), itemId));
        }
        container.addView(layout, 0);
        return layout;
    }

    public void destroyItemFixed(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void finishUpdateFixed(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    public boolean isViewFromObjectFixed(View view, Object object) {
        return view.equals(object);
    }

    public Parcelable saveStateFixed() {
        return null;
    }

    public void restoreStateFixed(Parcelable state, ClassLoader loader) {
        // do nothing
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
