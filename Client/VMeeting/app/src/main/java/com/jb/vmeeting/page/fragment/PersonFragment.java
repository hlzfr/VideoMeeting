package com.jb.vmeeting.page.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.UserUpdateEvent;
import com.jb.vmeeting.mvp.model.helper.ProgressResponseListener;
import com.jb.vmeeting.page.base.BaseFragment;
import com.jb.vmeeting.page.custom.CircleImageView;
import com.jb.vmeeting.page.utils.PageNavigator;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.account.AccountManager;
import com.jb.vmeeting.tools.netfile.DownloadManager;
import com.jb.vmeeting.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 个人中心
 * Created by Jianbin on 2016/4/24.
 */
public class PersonFragment extends BaseFragment implements View.OnClickListener{

    public static PersonFragment newInstance() {
        return new PersonFragment();
    }

    private CircleImageView imgAvatar; // 用户头像
    private TextView tvNickName; // 用户昵称

    private View infoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        ViewUtils.find(v, R.id.btn_person_logout).setOnClickListener(this);
        tvNickName = ViewUtils.find(v, R.id.tv_person_nickname);
        imgAvatar = ViewUtils.find(v, R.id.img_person_avatar);
        infoView = ViewUtils.find(v, R.id.rl_person_info);
        User currentUser = AccountManager.getInstance().getAccountSession().getCurrentUser();
        updateUserInfo(currentUser);
        setupListener();
        return v;
    }

    private void setupListener() {
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageNavigator.getInstance().toUserModifyActivity(getActivity());
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserUpdate(UserUpdateEvent updateEvent) {
        if (updateEvent.success && updateEvent.user != null) {
            updateUserInfo(updateEvent.user);
        }
    }

    public void updateUserInfo(User user) {
        String nickName = user.getNickName();
        if (TextUtils.isEmpty(nickName)) {
            nickName = user.getUsername();
        }
        tvNickName.setText(nickName);
        if (!TextUtils.isEmpty(user.getAvatar())) {
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + user.getAvatar(), imgAvatar);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_person_logout:
                AccountManager.getInstance().logout();
                break;
        }
    }
}
