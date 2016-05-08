package com.jb.vmeeting.page.adapter.recyclerview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.page.custom.CircleImageView;
import com.jb.vmeeting.tools.webrtc.TextMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jianbin on 2016/5/3.
 */
public class TextMessageAdapter extends ArrayAdapter<TextMessage> {

    @Override
    public View onCreateItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        TextMessage message = getItem(position);

        TextView tvContent = holder.find(R.id.tv_item_chat_content);
        CircleImageView imgAvatar = holder.find(R.id.img_item_chat_avatar);

        if(!TextUtils.isEmpty(message.getAvatar())) {
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + message.getAvatar(), imgAvatar);
        } else {
            imgAvatar.setImageResource(R.mipmap.ic_launcher);
        }

        tvContent.setText(message.getContent());
    }
}
