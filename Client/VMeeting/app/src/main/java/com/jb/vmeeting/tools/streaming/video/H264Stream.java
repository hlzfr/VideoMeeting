package com.jb.vmeeting.tools.streaming.video;

import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Base64;

import com.jb.vmeeting.app.App;
import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.rtp.MediaCodecInputStream;
import com.jb.vmeeting.tools.streaming.rtp.packetizer.H264Packetizer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * Created by Jianbin on 2016/3/31.
 */
public class H264Stream extends VideoStream {
    MediaCodec mMediaCodec; // android自带的编码器
    NV21Convertor mConvertor; // 将NV21转换成YUV
    EncoderDebugger debugger;

    public H264Stream() {
    }

    @Override
    public synchronized void configure() throws IllegalStateException, IOException {
        super.configure();
        mPacketizer = new H264Packetizer();
        initMediaCodec();
    }

    /**
     * 初始化并启动编码器
     */
    private void initMediaCodec() {
        int dgree = getDgree();
        int framerate = 15;
        int bitrate = 2 * width * height * framerate / 20;
        debugger = EncoderDebugger.debug(App.getInstance().getApplicationContext(), width, height);
        byte[] pps = Base64.decode(debugger.getB64PPS(), Base64.NO_WRAP);
        byte[] sps = Base64.decode(debugger.getB64SPS(), Base64.NO_WRAP);
        // 设置pps和sps
        ((H264Packetizer) mPacketizer).setStreamParameters(pps, sps);
        mConvertor = debugger.getNV21Convertor();
        try {
            mMediaCodec = MediaCodec.createByCodecName(debugger.getEncoderName());
            MediaFormat mediaFormat;
            if (dgree == 0) {
                // FIXME 将height和width颠倒就花屏了,而不颠倒vlc上显示的是颠倒的
//                mediaFormat = MediaFormat.createVideoFormat("video/avc", height, width);
                mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
            } else {
                mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
            }
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    debugger.getEncoderColorFormat());
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void encode() throws IOException {
        Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                if (data == null || !mStreaming) {
                    mCamera.addCallbackBuffer(data);
                    return;
                }

                ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();

                byte[] dst = new byte[data.length];
                Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
                if (getDgree() == 0) {
//                    L.d("previewSize.width " + previewSize.width + "; previewSize.height" + previewSize.height);
                    // FIXME rotate but messed 旋转后在vlc播放显示花屏
//                    dst = Util.rotateNV21Degree90(data, previewSize.width, previewSize.height);
                    dst = data;
                } else {
                    dst = data;
                }
                try {
                    int bufferIndex = mMediaCodec.dequeueInputBuffer(500000);
                    if (bufferIndex >= 0) {
                        inputBuffers[bufferIndex].clear();
                        mConvertor.convert(dst, inputBuffers[bufferIndex]);
                        mMediaCodec.queueInputBuffer(bufferIndex, 0, inputBuffers[bufferIndex].position(), System.nanoTime()/1000, 0);
                    } else {
                        L.e("No buffer available !");
                    }
                } catch (Exception e) {
                    L.e(e);
                } finally {
                    mCamera.addCallbackBuffer(dst);
                }
            }
        };

        mCamera.setPreviewCallbackWithBuffer(previewCallback);

        // 设置mPacketizer发送到的服务端ip
        mPacketizer.setDestination(mDestination, mRtpPort, mRtcpPort);
        mPacketizer.setInputStream(new MediaCodecInputStream(mMediaCodec));
        mPacketizer.start();
        mStreaming = true;
    }

    @Override
    public synchronized void start() throws IllegalStateException, IOException {
        if(!mStreaming) {
            super.start();
            mStreaming = true;
//            startPreview();
        }
    }

    @Override
    public void stop() {
        if (mStreaming) {
//            stopPreview();
            mCamera.setPreviewCallbackWithBuffer(null);
            super.stop();
        }
    }

    @Override
    public String getSessionDescription() throws IllegalStateException {
        if (debugger == null) throw new IllegalStateException("You need to call configure() first !");
        return "m=video "+String.valueOf(getDestinationPorts()[0])+" RTP/AVP 96\r\n" +
                "a=rtpmap:96 H264/90000\r\n" +
                "a=fmtp:96 packetization-mode=1;sprop-parameter-sets="+debugger.getB64SPS()+","+debugger.getB64PPS()+";\r\n";
    }

    @Override
    public boolean isStreaming() {
        return mStreaming;
    }
}
