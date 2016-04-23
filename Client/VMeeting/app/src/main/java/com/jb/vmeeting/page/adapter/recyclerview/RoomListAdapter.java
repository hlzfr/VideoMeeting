package com.jb.vmeeting.page.adapter.recyclerview;

import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.mvp.model.entity.Room;

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
        TextView tvOwnerName = holder.find(R.id.tv_listitem_owner_name);
        TextView tvRoomName = holder.find(R.id.tv_listitem_room_name);
        Room room = getItem(position);
        tvOwnerName.setText(room.getOwnerName());
        tvRoomName.setText(room.getName());
    }
}
