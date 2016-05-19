package com.jb.vmeeting.mvp.model.helper;

/**
 * 响应体进度回调接口，比如用于文件下载中
 * Created by Jianbin on 2016/5/18.
 */
public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
    void onResponseFailed(Throwable throwable);
}