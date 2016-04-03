package com.jb.vmeeting.tools.streaming.video;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jb.vmeeting.tools.L;
import com.jb.vmeeting.tools.streaming.MediaStream;

import java.io.IOException;
import java.util.List;

/**
 *
 * Created by Jianbin on 2016/3/25.
 */
public abstract class VideoStream extends MediaStream {

    private int mCameraId = 0;
    protected SurfaceView mSurfaceView = null;
    protected Camera mCamera;

    protected int width = 640, height = 480; // 视频的宽高

    protected SurfaceHolder.Callback mSurfaceHolderCallback = null;

//    protected boolean mCameraOpenedManually = true; // 是否已经手动打开摄像头
//    protected boolean mSurfaceReady = false;        // surface view是否创建完毕
//    protected boolean mUnlocked = false;            // camera是否unlock
//    protected boolean mPreviewStarted = false;      // 是否开始预览

    protected int mRequestedOrientation = 0, mOrientation = 0;

    public VideoStream() {
        this(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    public VideoStream(int camera) {
        super();
        setCamera(camera);
    }

    public synchronized void configure() throws IllegalStateException, IOException {
        super.configure();
        mOrientation = mRequestedOrientation;
    }

    public synchronized void setSurfaceView(SurfaceView view) {
        mSurfaceView = view;
        if (mSurfaceHolderCallback != null && mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceView.getHolder().removeCallback(mSurfaceHolderCallback);
        }
        if (mSurfaceView != null && mSurfaceView.getHolder() != null) {
            mSurfaceHolderCallback = new SurfaceHolder.Callback() {
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
//                    mSurfaceReady = false;
                    stopPreview();
                    L.d("Surface destroyed !");
                }

                @Override
                public void surfaceCreated(SurfaceHolder holder) {
//                    mSurfaceReady = true;
                    createCamera(holder);
                    startPreview();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                    L.d("Surface Changed !");
                }
            };
            mSurfaceView.getHolder().addCallback(mSurfaceHolderCallback);
//            mSurfaceReady = true;
        }
    }

    public synchronized void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            try {
                mCamera.autoFocus(null);
            } catch (Exception e) {
                //忽略异常
                L.i("auto foucus fail");
            }

            int previewFormat = mCamera.getParameters().getPreviewFormat();
            Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
            int size = previewSize.width * previewSize.height
                    * ImageFormat.getBitsPerPixel(previewFormat)
                    / 8;
            mCamera.addCallbackBuffer(new byte[size]);

        }
    }

    public synchronized void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallbackWithBuffer(null);
        }
        stop();
    }

    protected synchronized boolean createCamera(SurfaceHolder holder) throws RuntimeException {
        if (mSurfaceView == null)
            throw new RuntimeException("Invalid surface !");
//        if (mSurfaceView.getHolder() == null || !mSurfaceReady)
        if (mSurfaceView.getHolder() == null)
            throw new RuntimeException("Invalid surface !");
        try {
            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); //自动对焦
            int[] max = Util.determineMaximumSupportedFramerate(parameters);
            Camera.CameraInfo camInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, camInfo);
            int cameraRotationOffset = camInfo.orientation;
            int rotate = (360 + cameraRotationOffset - getDgree()) % 360;
            parameters.setRotation(rotate); //设置旋转角度exif信息
            parameters.setPreviewFormat(ImageFormat.NV21);//设置预览图片格式为NV21
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            // TODO 设置为手机支持的宽高
            parameters.setPreviewSize(width, height);//设置预览宽高
            parameters.setPreviewFpsRange(max[0], max[1]);
            mCamera.setParameters(parameters);
            int displayRotation;
            displayRotation = (cameraRotationOffset - getDgree() + 360) % 360;
            mCamera.setDisplayOrientation(displayRotation);//设置相机旋转角度
            mCamera.setPreviewDisplay(holder);
            return true;
        } catch (Exception e) {
            destroyCamera();
            return false;
        }
    }

    protected synchronized void destroyCamera() {
        if (mCamera != null) {
            if (mStreaming) super.stop();
            mCamera.stopPreview();
            try {
                mCamera.release();
            } catch (Exception e) {

            }
            mCamera = null;
//            mUnlocked = false;
//            mPreviewStarted = false;
        }
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

    public void switchCamera() throws RuntimeException, IOException {
        if (Camera.getNumberOfCameras() == 1) throw new IllegalStateException("Phone only has one camera !");
        boolean streaming = mStreaming;
//        boolean previewing = mCamera!=null && mCameraOpenedManually;
        boolean previewing = mCamera!=null;
        mCameraId = (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        setCamera(mCameraId);
        stopPreview();
//        mFlashEnabled = false;
        if (previewing) startPreview();
        if (streaming) start();
    }

    public int getCamera() {
        return mCameraId;
    }

    public void setPreviewOrientation(int orientation) {
        mRequestedOrientation = orientation;
    }

    protected int getDgree() {
        return 0;
    }
}
