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
        mCamera = getCameraInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            /* Make sure we release the camera for other applications */
            mCamera.release();
            mCamera = null;
        }
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
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
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
            if (mCallback != null) {
                mCallback.onPictureTaken(data, info);
            }
        }
    };
}