package com.jb.vmeeting.tools.streaming;

import java.io.IOException;

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
}
