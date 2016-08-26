package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.page.adapter.viewpager.MoveableImageAdapter;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.tools.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jianbin on 2016/5/20.
 */
public class FullScreenImageFragment extends BaseFragment {

    public static FullScreenImageFragment newInstance(ArrayList<String> imageUrls, int position) {
        FullScreenImageFragment fragment = new FullScreenImageFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IntentConstant.BUNDLE_KEY_IMAGE_URLS, imageUrls);
        bundle.putInt(IntentConstant.BUNDLE_KEY_IMAGE_POSITION, position);
        fragment.setArguments(bundle);
        L.d("imageUrls " + String.valueOf(imageUrls));
        L.d("position " + position);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_iamge, container,
                false);
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        List<String> imageList = getArguments().getStringArrayList(IntentConstant.BUNDLE_KEY_IMAGE_URLS);
        pager.setAdapter(new MoveableImageAdapter(imageList));
        pager.setCurrentItem(getArguments().getInt(IntentConstant.BUNDLE_KEY_IMAGE_POSITION, 0));

        L.d("imageList " + String.valueOf(imageList));
        L.d("position " + getArguments().getInt(IntentConstant.BUNDLE_KEY_IMAGE_POSITION, 0));
        return rootView;
    }
}
