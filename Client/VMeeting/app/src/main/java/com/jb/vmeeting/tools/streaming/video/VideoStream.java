package com.jb.vmeeting.tools.streaming.video;

import android.view.SurfaceView;

import com.jb.vmeeting.tools.streaming.MediaStream;

/**
 *
 * Created by Jianbin on 2016/3/25.
 */
public abstract class VideoStream extends MediaStream {

    public synchronized void setSurfaceView(SurfaceView view) {
        // TODO
    }

    public synchronized void stopPreview() {
        // TODO
        stop();
    }

    public void setPreviewOrientation(int orientation) {
        // TODO
    }
}
