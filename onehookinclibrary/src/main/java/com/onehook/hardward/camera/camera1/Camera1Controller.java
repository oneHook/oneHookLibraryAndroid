package com.onehook.hardward.camera.camera1;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.onehook.hardward.camera.BaseCameraController;
import com.onehook.hardward.camera.CameraConfig;
import com.onehook.hardward.camera.PictureInfo;

/**
 * Created by EagleDiaoOptimity on 2018-03-01.
 * Camera Controller using Android camera1 API (for < 21).
 */
public class Camera1Controller extends BaseCameraController {

    /**
     * Camera object.
     */
    private Camera mCamera;

    /**
     * Camera Surface.
     */
    private Camera1View mCamera1View;

    /**
     * @param context
     * @param savedInstanceState
     */
    public Camera1Controller(@NonNull final Context context,
                             @Nullable CameraConfig cameraConfig) {
        super(context, cameraConfig);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        /* make sure the camera instance is ready */
        mCamera = getCameraInstance(mCameraConfig);
        mCamera1View.setCamera(mCamera);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera1View.stopCamera();
        mCamera.release();
        mCamera = null;
        mCamera1View.setCamera(null);
    }

    @Override
    public void takePicture() {
        if (mCamera1View != null && mCamera1View.isSafeToTake()) {
            mCamera1View.takePicture(null, null, mPicture);
        }
    }

    @Override
    public void restartPreview() {
        mCamera1View.startPreview();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    @Nullable
    public static Camera getCameraInstance(final CameraConfig config) {
        int cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        if (config.cameraInfo == CameraConfig.FRONT_FACING) {
            cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }

        Camera c = null;
        try {
            c = Camera.open(cameraID);
        } catch (Exception e) {
            /* Camera is not available (in use or does not exist) */
            e.printStackTrace();
        }
        return c;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            final PictureInfo info = new PictureInfo();
            info.orientation = mCurrentOrientation;
            info.width = camera.getParameters().getPictureSize().width;
            info.height = camera.getParameters().getPictureSize().height;

            if (mCallback != null) {
                mCallback.onPictureTaken(data, info);
            }
        }
    };

    @Override
    public View obtainCameraView(final Context context) {
        mCamera1View = new Camera1View(context, this);
        return mCamera1View;
    }

    @Override
    public void onCameraConfigChanged(CameraConfig config) {
        if (mCamera1View != null) {
            mCamera1View.stopCamera();
        }
        if (mCamera != null) {
            mCamera.release();
        }
        mCamera = getCameraInstance(config);
        mCamera1View.setCamera(mCamera);
        try {
            mCamera1View.restartCamera();
        } catch (Exception e) {

        }
    }
}