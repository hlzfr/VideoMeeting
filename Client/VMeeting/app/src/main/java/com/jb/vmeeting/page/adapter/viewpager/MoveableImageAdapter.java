package com.jb.vmeeting.page.adapter.viewpager;

import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.page.custom.MoveableImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jianbin on 2016/5/20.
 */
public class MoveableImageAdapter  extends PagerAdapter {

    List<String> imageUrls;

    OnItemLongClickListener onItemLongClickListener;
    OnItemClickListener onItemClickListener;

    public MoveableImageAdapter(List<String> imageUrls) {
        this.imageUrls = (imageUrls == null ? new ArrayList<String>(): imageUrls);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public String getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        int position = POSITION_NONE;
        if (object != null) {
            position = imageUrls.indexOf(object);
            if (position == -1) {
                position = POSITION_NONE;
            }
        }
        return position;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (imageUrls != null) {
            count = imageUrls.size();
        }
        return count;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        FrameLayout imageLayout = new FrameLayout(view.getContext());
        MoveableImageView imageView = new MoveableImageView(view.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageLayout.setLayoutParams(layoutParams);
        imageView.setLayoutParams(layoutParams);

        if(!TextUtils.isEmpty(imageUrls.get(position))) {
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + imageUrls.get(position),
                    imageView);
        }

        imageLayout.addView(imageView, 0);

        imageLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    return onItemLongClickListener.onItemLongClick(position);
                }
                return false;
            }
        });

        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {
        onItemLongClickListener = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
