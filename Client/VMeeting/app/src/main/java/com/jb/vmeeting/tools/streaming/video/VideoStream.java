package com.jb.vmeeting.tools.streaming.video;

import android.hardware.Camera;
import android.view.SurfaceView;

import com.jb.vmeeting.tools.streaming.MediaStream;

/**
 *
 * Created by Jianbin on 2016/3/25.
 */
public abstract class VideoStream extends MediaStream {

    private int mCameraId = 0;

    public synchronized void setSurfaceView(SurfaceView view) {
        // TODO
    }

    public synchronized void startPreview() {
        // TODO
    }

    public synchronized void stopPreview() {
        // TODO
        stop();
    }

    public void setPreviewOrientation(int orientation) {
        // TODO
    }

    public void setCamera(int camera) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i=0;i<numberOfCameras;i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == camera) {
                mCameraId = i;
                break;
            }
        }
    }
}
