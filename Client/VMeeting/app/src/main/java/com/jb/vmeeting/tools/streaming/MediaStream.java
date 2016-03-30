package com.jb.vmeeting.tools.streaming;

import android.support.annotation.CallSuper;

import com.jb.vmeeting.tools.streaming.rtp.packetizer.AbstractPacketizer;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by Jianbin on 2016/3/25.
 */
public abstract class MediaStream implements Stream {

    protected boolean mStreaming = false;

    protected AbstractPacketizer mPacketizer = null;

    protected InetAddress mDestination; // 用于传递给mPacketizer
    protected int mRtpPort = 0, mRtcpPort = 0; // 用于传递给mPacketizer
    private int mTTL = 64; // 用于传递给mPacketizer

    @Override
    public void configure() throws IllegalStateException, IOException {
        if (mStreaming) throw new IllegalStateException("Can't be called while streaming.");
    }

    @CallSuper
    @Override
    public synchronized void start() throws IllegalStateException, IOException {
        configure();
        if (mDestination==null)
            throw new IllegalStateException("No destination ip address set for the stream !");

        if (mRtpPort<=0 || mRtcpPort<=0)
            throw new IllegalStateException("No destination ports set for the stream !");

        mPacketizer.setTimeToLive(mTTL);
        encode();
    }

    @CallSuper
    @Override
    public void stop() {
        if (mStreaming) {
            mPacketizer.stop();
            mStreaming = false;
        }
    }

    protected abstract void encode() throws IOException;

    @Override
    public void setDestinationAddress(InetAddress dest) {
        // 或者直接在这里赋值给mPacketizer?
        mDestination = dest;
    }

    @Override
    public void setDestinationPorts(int dport) {
        // 或者直接在这里赋值给mPacketizer?
        if (dport % 2 == 1) {
            mRtpPort = dport-1;
            mRtcpPort = dport;
        } else {
            mRtpPort = dport;
            mRtcpPort = dport+1;
        }
    }

    @Override
    public void setDestinationPorts(int rtpPort, int rtcpPort) {
        this.mRtcpPort = rtcpPort;
    }

    @Override
    public void setTimeToLive(int ttl) {
        mTTL = ttl;
    }


    public int[] getDestinationPorts() {
        return new int[] {
                mRtpPort,
                mRtcpPort
        };
    }

    /**
     * 同步信源(SSRC)标识符：占32位，用于标识同步信源。
     * 该标识符是随机选择的，参加同一视频会议的两个同步信源不能有相同的SSRC。
     */
    public int getSSRC() {
        return mPacketizer.getSSRC();
    }

}
