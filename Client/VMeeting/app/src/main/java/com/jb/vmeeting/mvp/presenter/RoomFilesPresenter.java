package com.jb.vmeeting.mvp.presenter;

import com.jb.vmeeting.R;
import com.jb.vmeeting.app.App;
import com.jb.vmeeting.mvp.model.apiservice.RoomService;
import com.jb.vmeeting.mvp.model.entity.Result;
import com.jb.vmeeting.mvp.model.entity.Room;
import com.jb.vmeeting.mvp.model.entity.RoomFiles;
import com.jb.vmeeting.mvp.model.helper.ProgressRequestListener;
import com.jb.vmeeting.mvp.model.helper.ProgressResponseListener;
import com.jb.vmeeting.mvp.model.helper.RetrofitHelper;
import com.jb.vmeeting.mvp.model.helper.SimpleCallback;
import com.jb.vmeeting.mvp.view.IRoomFilesView;
import com.jb.vmeeting.page.utils.ToastUtil;
import com.jb.vmeeting.tools.netfile.DownloadManager;
import com.jb.vmeeting.tools.netfile.UploadManager;
import com.jb.vmeeting.tools.task.TaskExecutor;

import java.io.File;

/**
 * Created by Jianbin on 2016/5/18.
 */
public class RoomFilesPresenter extends BasePresenter {

    RoomService roomService;
    IRoomFilesView mView;

    boolean isStreamingFile = false;

    public RoomFilesPresenter(IRoomFilesView view) {
        mView = view;
        roomService = RetrofitHelper.createService(RoomService.class);
    }

    public void downloadFile(String fileName) {
        if (isStreamingFile) {
            return;
        }
        if (mView != null) {
            String[] split = fileName.split("_");
            mView.onDownloadStart(split[split.length - 1]);
        }
        isStreamingFile = true;
        DownloadManager.getInstance().download(App.getInstance().getString(R.string.file_base_url) + fileName, new ProgressResponseListener() {
            @Override
            public void onResponseProgress(final long bytesRead, final long contentLength, final boolean done) {
                if (mView != null) {
                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onDownloading(bytesRead, contentLength);
                            if (done) {
                                isStreamingFile = false;
                                mView.onDownloadSuccess();
                            }
                        }
                    });
                }
            }

            @Override
            public void onResponseFailed(Throwable throwable) {
                isStreamingFile = false;
                if (mView != null) {
                    mView.onDownloadFailed(-1, throwable.getMessage());
                }
            }
        });
    }
    public void uploadFile(File file) {
        if (isStreamingFile) {
            return;
        }
        if (mView != null) {
            mView.onUploadStart(file);
        }
        isStreamingFile = true;
        // 开始上传
        UploadManager.getInstance().upload(file, new SimpleCallback<String>() {
            @Override
            public void onSuccess(int statusCode, Result<String> result) {
                isStreamingFile = false;
                String url = result.body;
                if (mView != null) {
                    mView.onUploadSuccess(url);
                }
            }

            @Override
            public void onFailed(int statusCode, Result<String> result) {
                isStreamingFile = false;
                if (mView != null) {
                    mView.onUploadFailed(result.code, result.message);
                }
            }
        }, new ProgressRequestListener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength, boolean done) {
                if (mView != null) {
                    TaskExecutor.runTaskOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onUploading(bytesWritten, contentLength);
                        }
                    });
                }
            }
        });
    }

    public void updateRoomFile(RoomFiles roomFiles) {
        roomService.updateRoomFiles(roomFiles).enqueue(new SimpleCallback<Void>() {
            @Override
            public void onSuccess(int statusCode, Result<Void> result) {
                ToastUtil.toast("更新成功");
            }

            @Override
            public void onFailed(int statusCode, Result<Void> result) {
                ToastUtil.toast("更新失败(statusCode:" + statusCode + ";code:" + result.code + ";msg:" + result.message + ")");
            }
        });
    }

    public void getRoomFiles(Room room) {
        roomService.getRoomFiles(room).enqueue(new SimpleCallback<RoomFiles>() {
            @Override
            public void onSuccess(int statusCode, Result<RoomFiles> result) {
                RoomFiles files = result.body;
                if (mView != null) {
                    mView.onGetRoomFiles(files);
                }
            }

            @Override
            public void onFailed(int statusCode, Result<RoomFiles> result) {
                ToastUtil.toast("获取文件列表失败");
            }
        });
    }

    @Override
    public void onDestroy() {
        mView = null;
    }
}
