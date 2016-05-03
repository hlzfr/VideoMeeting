package com.jb.vmeeting.page.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.ChooseUserItem;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.page.custom.CircleImageView;
import com.jb.vmeeting.tools.L;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class UserListAdapter extends ArrayAdapter<ChooseUserItem> {

    private LayoutInflater mInflater;

    public UserListAdapter(@NonNull Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public View onCreateItemView(ViewGroup parent, int viewType) {
        return mInflater.inflate(R.layout.item_user, parent, false);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        final ChooseUserItem item = getItem(position);
        User user = item.getUser();
        boolean isChoose = item.isChoose();
        String nickName = user.getNickName();
        String avatar = user.getAvatar();

        CircleImageView imgAvatar = holder.find(R.id.img_item_user_avatar);
        TextView tvNickName = holder.find(R.id.tv_item_user_nickname);
        CheckBox checkBox = holder.find(R.id.cb_item_user_choose);

        checkBox.setChecked(isChoose);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setIsChoose(isChecked);
            }
        });

        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + avatar, imgAvatar);
        } else {
            imgAvatar.setImageResource(R.mipmap.ic_launcher);
        }
        tvNickName.setText(nickName);
    }
}
