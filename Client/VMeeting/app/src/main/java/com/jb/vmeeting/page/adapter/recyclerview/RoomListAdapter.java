package com.jb.vmeeting.page.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.page.custom.CircleImageView;
import com.jb.vmeeting.page.custom.DateTimePickDialogUtil;
import com.jb.vmeeting.utils.DateUtils;
import com.jb.vmeeting.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;

/**
 * Created by Jianbin on 2016/4/23.
 */
public class RoomListAdapter extends ArrayAdapter<Room> {

    private LayoutInflater mInflater;

    public RoomListAdapter(Context ctx) {
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public View onCreateItemView(ViewGroup parent, int viewType) {
        return mInflater.inflate(R.layout.item_room, parent, false);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        TextView tvOwnerName = holder.find(R.id.tv_room_item_owner_name);
        TextView tvRoomName = holder.find(R.id.tv_room_item_room_name);
        TextView tvDescribe = holder.find(R.id.tv_room_item_describe);
        TextView tvTime = holder.find(R.id.tv_room_item_start_time);
        CircleImageView imgAvatar = holder.find(R.id.tv_room_item_avatar);
        Room room = getItem(position);
        if (room.getOwner() != null) {
            tvOwnerName.setText(room.getOwner().getNickName());
            ImageLoader.getInstance().displayImage(App.getInstance().getString(R.string.file_base_url) + room.getOwner().getAvatar(), imgAvatar);
        } else {
            tvOwnerName.setText(room.getOwnerName());
            imgAvatar.setImageResource(R.mipmap.ic_launcher);
        }
        tvRoomName.setText(room.getName());
        tvTime.setText(DateUtils.toDate(new Date(room.getStartTime()),"yyyy.MM.dd HH:mm") + "-" + DateUtils.toDate(new Date(room.getEndTime()), "yyyy.MM.dd HH:mm"));
        tvDescribe.setText(room.getDescribe());


    }
}
