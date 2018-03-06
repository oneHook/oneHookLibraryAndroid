package com.onehook.hardware.camera1;

import android.content.Context;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                             @Nullable final Bundle savedInstanceState,
                             @Nullable CameraConfig cameraConfig) {
        super(context, savedInstanceState, cameraConfig);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            /* Make sure we release the camera for other applications */
            mCamera.release();
            mCamera = null;
        }
        mCamera1View = null;
    }

    /**
     * Set the surface of the camera.
     *
     * @param view camera surface view
     */
    public void setView(final Camera1View view) {
        mCamera1View = view;
    }

    @Nullable
    protected Camera getCamera() {
        return mCamera;
    }

    @Override
    public void takePicture() {
        mCamera.takePicture(null, null, mPicture);
    }

    @Override
    public void restartPreview() {
        mCamera.startPreview();
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
}