package com.onehook.hardware.camera1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by eaglediaooptimity on 2018-03-02.
 */

public abstract class BaseCameraController implements SensorEventListener {

    /**
     * Sensor manager.
     */
    private SensorManager mSensorManager;

    /**
     * Current device rotation degrees.
     */
    protected int mCurrentDegrees = -1;

    /**
     * Current device orientation.
     */
    protected int mCurrentOrientation;

    /**
     * Camera config.
     */
    protected CameraConfig mCameraConfig;

    /**
     * Callback.
     */
    protected CameraControllerCallback mCallback;

    /**
     * @param context
     * @param savedInstnaceState
     * @param cameraConfig
     */
    public BaseCameraController(@NonNull final Context context,
                                @Nullable CameraConfig cameraConfig) {

        /* Getting the sensor service. */
        mSensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);

        if (cameraConfig != null) {
            mCameraConfig = cameraConfig;
        } else {
            mCameraConfig = new CameraConfig();
        }
    }

    /**
     * Client should call this in onDestory.
     */
    public void onDestroy() {
        mSensorManager = null;
    }

    /**
     * Client should call this in onResume.
     */
    public void onResume() {
        /* Register this class as a listener for the accelerometer sensor */
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Client should call this in onPause.
     */
    public void onPause() {
        /* make sure we dont listen to sensor events */
        mSensorManager.unregisterListener(this);
    }

    /**
     * Get camera config object.
     */
    @NonNull
    public CameraConfig getCameraConfig() {
        return mCameraConfig;
    }

    /**
     * Check if this device has a camera
     */
    protected boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Putting in place a listener so we can get the sensor data only when
     * something changes.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                RotateAnimation animation = null;
                if (event.values[0] < 4 && event.values[0] > -4) {
                    if (event.values[1] > 0 && mCurrentOrientation != ExifInterface.ORIENTATION_ROTATE_90) {
                        // UP
                        mCurrentOrientation = ExifInterface.ORIENTATION_ROTATE_90;
                        animation = getRotateAnimation(270);
                        mCurrentDegrees = 270;
                    } else if (event.values[1] < 0 && mCurrentOrientation != ExifInterface.ORIENTATION_ROTATE_270) {
                        // UP SIDE DOWN
                        mCurrentOrientation = ExifInterface.ORIENTATION_ROTATE_270;
                        animation = getRotateAnimation(90);
                        mCurrentDegrees = 90;
                    }
                } else if (event.values[1] < 4 && event.values[1] > -4) {
                    if (event.values[0] > 0 && mCurrentOrientation != ExifInterface.ORIENTATION_NORMAL) {
                        // LEFT
                        mCurrentOrientation = ExifInterface.ORIENTATION_NORMAL;
                        animation = getRotateAnimation(0);
                        mCurrentDegrees = 0;
                    } else if (event.values[0] < 0 && mCurrentOrientation != ExifInterface.ORIENTATION_ROTATE_180) {
                        // RIGHT
                        mCurrentOrientation = ExifInterface.ORIENTATION_ROTATE_180;
                        animation = getRotateAnimation(180);
                        mCurrentDegrees = 180;
                    }
                }
                if (animation != null) {
//                        rotatingImage.startAnimation(animation);
                }
            }

        }
    }

    /**
     * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Calculating the mCurrentDegrees needed to rotate the image imposed on the button
     * so it is always facing the user in the right direction
     *
     * @param toDegrees
     * @return
     */
    private RotateAnimation getRotateAnimation(float toDegrees) {
        float compensation = 0;

        if (Math.abs(mCurrentDegrees - toDegrees) > 180) {
            compensation = 360;
        }

        // When the device is being held on the left side (default position for
        // a camera) we need to add, not subtract from the toDegrees.
        if (toDegrees == 0) {
            compensation = -compensation;
        }

        // Creating the animation and the RELATIVE_TO_SELF means that he image
        // will rotate on it center instead of a corner.
        RotateAnimation animation = new RotateAnimation(mCurrentDegrees,
                toDegrees - compensation, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        // Adding the time needed to rotate the image
        animation.setDuration(250);

        // Set the animation to stop after reaching the desired position. With
        // out this it would return to the original state.
        animation.setFillAfter(true);

        return animation;
    }

    public void setCallback(final CameraControllerCallback callback) {
        mCallback = callback;
    }

    public void resetCameraConfigAndStart(final CameraConfig config) {
        mCameraConfig = config;
        onCameraConfigChanged(mCameraConfig);
    }

    /* child should implement the following */

    public abstract void takePicture();

    public abstract void restartPreview();

    public abstract View obtainCameraView(final Context context);

    public abstract void onCameraConfigChanged(final CameraConfig config);

}
