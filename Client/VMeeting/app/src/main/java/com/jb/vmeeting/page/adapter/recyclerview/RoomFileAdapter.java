package com.jb.vmeeting.page.adapter.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.constant.AppConstant;
import com.jb.vmeeting.page.activity.RoomFilesActivity;
import com.jb.vmeeting.utils.FileUtils;

/**
 * Created by Jianbin on 2016/5/18.
 */
public class RoomFileAdapter extends ArrayAdapter<String> {
    private int mode = RoomFilesActivity.MODE_VIEW;

    public RoomFileAdapter(int mode) {
        super();
        this.mode = mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateItemView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_file, parent, false);
    }

    OnItemButtonClickListener buttonListener;

    public void setOnButtonClick(OnItemButtonClickListener l) {
        buttonListener = l;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        TextView tvFileName = holder.find(R.id.tv_item_room_file_name);
        Button btnDownload = holder.find(R.id.btn_item_room_download);
        final String fileName = getItem(position);
        String[] split = fileName.split("_");
        tvFileName.setText(split[split.length - 1]);
        final boolean fileExists = FileUtils.checkFileExists(AppConstant.DOWNLOAD_FILE_FOLDER, fileName);
        if (mode == RoomFilesActivity.MODE_VIEW) {
            if (fileExists) {
                btnDownload.setText("查看");
            } else {
                btnDownload.setText("下载");
            }
        } else {
            btnDownload.setText("删除");
        }
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListener.onItemClick(fileExists, fileName, position);
            }
        });
    }

    public static interface OnItemButtonClickListener {
        void onItemClick(boolean exist, String fileName, int position);
    }
}
