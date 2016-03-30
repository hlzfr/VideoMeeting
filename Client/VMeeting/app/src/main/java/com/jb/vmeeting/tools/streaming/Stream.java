package com.jb.vmeeting.tools.streaming;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Jianbin on 2016/3/25.
 */
public interface Stream {

    /**
     * 配置该流必要的设置，需要在{@link #getSessionDescription()}之前调用
     * 在{@link #start()}前被调用
     *
     * @throws IllegalStateException
     * @throws IOException
     */
    public void configure() throws IllegalStateException, IOException;

    /**
     * 开始该流的传输
     * 需要在 {@link Stream#configure()}之后调用
     */
    public void start() throws IllegalStateException, IOException;

    /**
     * 停止流传输
     */
    public void stop();

    /**
     * 设置packets的time to live
     * @param ttl The time to live
     * @throws IOException
     */
    public void setTimeToLive(int ttl) throws IOException;

    /**
     * 设置该流的目的ip地址
     * @param dest
     */
    public void setDestinationAddress(InetAddress dest);

    /**
     * 设置流的目标端口
     * RTP偶数  RTCP奇数
     * 如果传入奇数odd，RTP端口被设为odd-1，RTCP端口设为odd
     * 如果传入偶数even，RTP端口设为even，RTCP端口设为even+1
     * @param dport The destination port
     */
    public void setDestinationPorts(int dport);

    /**
     * Sets the destination ports of the stream.
     * @param rtpPort Destination port that will be used for RTP
     * @param rtcpPort Destination port that will be used for RTCP
     */
    public void setDestinationPorts(int rtpPort, int rtcpPort);

//    public int[] getLocalPorts();

    public int[] getDestinationPorts();

    public int getSSRC();

//    public long getBitrate();

    /**
     * 返回流的sdp描述
     * 要在 {@link Stream#configure()}之后执行
     * @throws IllegalStateException 当 {@link Stream#configure()} 没有被执行
     */
    public String getSessionDescription() throws IllegalStateException;

    public boolean isStreaming();
}
