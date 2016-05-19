package com.jb.vmeeting.mvp.model.helper;

/**
 * 请求体进度回调接口，比如用于文件上传中
 * Created by Jianbin on 2016/5/18.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
