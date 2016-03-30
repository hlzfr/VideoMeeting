package com.jb.vmeeting.tools.streaming.audio;

import android.annotation.TargetApi;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.rtp.MediaCodecInputStream;
import com.jb.vmeeting.tools.streaming.rtp.packetizer.AACLATMPacketizer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Jianbin on 2016/3/26.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AACStream extends AudioStream {
    protected MediaCodec mMediaCodec;

    private String mSessionDescription = null;
    private AudioRecord mAudioRecord = null;
    private Thread mThread = null;

    public AACStream() {
        super();

        //TODO check aac stream supported or not
//        if (!AACStreamingSupported()) {
//            L.e("AAC not supported on this phone");
//            throw new RuntimeException("AAC not supported by this phone !");
//        } else {
//            L.d("AAC supported on this phone");
//        }

    }

    @Override
    public void configure() throws IllegalStateException, IOException {
        super.configure();
        // 新建对应的Packetizer，并初始化SessionDescription
        // 或者采用设置Packetizer的方式？
        mPacketizer = new AACLATMPacketizer();

        int mProfile = 2; // AAC LC
        int mChannel = 1;
        int mSamplingRateIndex =  11; // 8000 rate
        int mConfig = mProfile<<11 | mSamplingRateIndex<<7 | mChannel<<3;

        mSessionDescription = "m=audio "+String.valueOf(getDestinationPorts()[0])+" RTP/AVP 96\r\n" +
                "a=rtpmap:96 mpeg4-generic/"+8000/*mQuality.samplingRate*/+"\r\n"+
                "a=fmtp:96 streamtype=5; profile-level-id=15; mode=AAC-hbr; config="+Integer.toHexString(mConfig)+"; SizeLength=13; IndexLength=3; IndexDeltaLength=3;\r\n";

    }

    @Override
    public synchronized void start() throws IllegalStateException, IOException {
        if (!mStreaming) {
            super.start();
        }
    }

    @Override
    protected void encode() throws IOException {
        // 获取音频数据，并对其进行编码，之后传递给packetizer进行打包发送
        // 或者采用设置编码器的方式？
        final int bufferSize = AudioRecord.getMinBufferSize(8000/*mQuality.samplingRate*/, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)*2;

        ((AACLATMPacketizer)mPacketizer).setSamplingRate(8000/*mQuality.samplingRate*/);

        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000/*mQuality.samplingRate*/, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        mMediaCodec = MediaCodec.createEncoderByType("audio/mp4a-latm");
        MediaFormat format = new MediaFormat();
        format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
        format.setInteger(MediaFormat.KEY_BIT_RATE,32000 /*mQuality.bitRate*/);
        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
        format.setInteger(MediaFormat.KEY_SAMPLE_RATE, 8000/*mQuality.samplingRate*/);
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        format.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, bufferSize);
        mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mAudioRecord.startRecording();
        mMediaCodec.start();

        final MediaCodecInputStream inputStream = new MediaCodecInputStream(mMediaCodec);
        final ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int len = 0, bufferIndex = 0;
                try {
                    while (!Thread.interrupted()) {
                        bufferIndex = mMediaCodec.dequeueInputBuffer(10000);
                        if (bufferIndex>=0) {
                            inputBuffers[bufferIndex].clear();
                            len = mAudioRecord.read(inputBuffers[bufferIndex], bufferSize);
                            if (len ==  AudioRecord.ERROR_INVALID_OPERATION || len == AudioRecord.ERROR_BAD_VALUE) {
                                L.e("An error occured with the AudioRecord API !");
                            } else {
                                //Log.v(TAG,"Pushing raw audio to the decoder: len="+len+" bs: "+inputBuffers[bufferIndex].capacity());
                                mMediaCodec.queueInputBuffer(bufferIndex, 0, len, System.nanoTime()/1000, 0);
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        });

        mThread.start();
        // 设置 packetizer 的发送地址
        mPacketizer.setDestination(mDestination, mRtpPort, mRtcpPort);
        // 设置 packetizer 的接收流，使其能够打包发送编码后的数据
        mPacketizer.setInputStream(inputStream);
        // 开始循环获取inputStream的数据
        mPacketizer.start();

        mStreaming = true;
    }

    @Override
    public void stop() {
        if (mStreaming) {
            mThread.interrupt();
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
            super.stop();
        }
    }

    @Override
    public String getSessionDescription() throws IllegalStateException {
        if (mSessionDescription == null) throw new IllegalStateException("You need to call configure() first !");
        return mSessionDescription;
    }

    @Override
    public boolean isStreaming() {
        return mStreaming;
    }

}
