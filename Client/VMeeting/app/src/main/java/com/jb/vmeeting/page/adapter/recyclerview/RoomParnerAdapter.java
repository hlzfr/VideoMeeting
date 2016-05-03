package com.jb.vmeeting.page.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.User;
import com.jb.vmeeting.mvp.model.eventbus.event.RemoveParticipatorEvent;
import com.jb.vmeeting.page.custom.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Jianbin on 2016/4/28.
 */
public class RoomParnerAdapter extends ArrayAdapter<User> {

    private boolean editable = true;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public RoomParnerAdapter() {
        setNotifyOnChange(false);
    }
    @Override
    public View onCreateItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_parner, parent, false);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        User user = getItem(position);
        TextView tvNickName = holder.find(R.id.tv_item_room_parnerr_nickname);
        CircleImageView imgAvatar = holder.find(R.id.img_item_room_parner_avatar);

        tvNickName.setText(user.getNickName());
        ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + user.getAvatar(), imgAvatar);

        if(editable) {
            holder.find(R.id.btn_item_room_parner_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<User> removed = new ArrayList<User>(1);
                    removed.add(getItem(position));
                    remove(getItem(position));
                    notifyItemRemoved(position);
                    EventBus.getDefault().post(new RemoveParticipatorEvent(removed));

                }
            });
        } else {
            holder.find(R.id.btn_item_room_parner_delete).setVisibility(View.GONE);
        }
    }
}
