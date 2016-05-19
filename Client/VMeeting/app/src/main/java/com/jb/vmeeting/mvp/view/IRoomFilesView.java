package com.jb.vmeeting.mvp.view;

import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.RoomFiles;

import java.io.File;

/**
 * Created by Jianbin on 2016/5/18.
 */
public interface IRoomFilesView {
    void onUploadStart(File file);
    void onUploading(long current, long total);
    void onUploadSuccess(String url);
    void onUploadFailed(int code, String reason);

    void onDownloadStart(String fileName);
    void onDownloading(long current, long total);
    void onDownloadSuccess();
    void onDownloadFailed(int code, String reason);

    void onGetRoomFiles(RoomFiles files);

}
