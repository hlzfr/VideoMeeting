package com.jb.vmeeting.mvp.model.biz;

/**
 * 弃用Biz模块，因为觉得无意义地增加了复杂度
 * Created by Jianbin on 16/3/15.
 */
@Deprecated
public interface IVideoChatBiz {
    void startStream(String channelName);
    void stopStream();
    boolean isStreaming();
}
