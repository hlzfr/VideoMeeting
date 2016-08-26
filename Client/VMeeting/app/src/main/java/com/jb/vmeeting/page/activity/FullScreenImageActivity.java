package com.jb.vmeeting.page.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.IntentConstant;
import com.jb.vmeeting.page.base.BaseActivity;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.page.fragment.FullScreenImageFragment;
import com.jb.vmeeting.tools.L;

import java.util.ArrayList;

/**
 * Created by Jianbin on 2016/5/20.
 */
public class FullScreenImageActivity extends BaseActivity {

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_frame_no_titlebar);
    }

    @Override
    protected void setupListener() {

    }

    @Override
    protected void onHandleIntent(Intent intent, Bundle bundle) {
        if(bundle!=null) {
            final ArrayList<String> imageList = bundle.getStringArrayList(IntentConstant.BUNDLE_KEY_IMAGE_URLS);
            final int position = bundle.getInt(IntentConstant.BUNDLE_KEY_IMAGE_POSITION, 0);
            L.d("imageList " + String.valueOf(imageList));
            L.d("position "+position);
            switchFragment(R.id.frame_content, "full_screen", new CreateFragmentCallback() {
                @Override
                public FragmentManager getFragmentManager(BaseActivity activity, String tag) {
                    return getSupportFragmentManager();
                }

                @Override
                public FragmentTransaction onGetTransaction(BaseActivity activity, String tag) {
                    return getSupportFragmentManager().beginTransaction();
                }

                @Override
                public BaseFragment onCreateFragment(String tag) {
                    return FullScreenImageFragment.newInstance(imageList, position);
                }
            });
        }
    }
}
