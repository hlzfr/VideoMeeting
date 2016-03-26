package com.jb.vmeeting.tools.streaming;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Jianbin on 2016/3/25.
 */
public interface Stream {

    /**
     * 配置该流必要的设置
     * @throws IllegalStateException
     * @throws IOException
     */
    public void configure() throws IllegalStateException, IOException;

    public void start() throws IllegalStateException, IOException;

    public void stop();

    public String getSessionDescription() throws IllegalStateException;

    public boolean isStreaming();

    public void setDestinationAddress(InetAddress dest); // 设置要发送数据的地址

    public void setTimeToLive(int ttl);
}
