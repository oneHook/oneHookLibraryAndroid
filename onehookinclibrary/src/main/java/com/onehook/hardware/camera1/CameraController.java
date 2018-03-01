package com.onehook.hardware.camera1;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by EagleDiaoOptimity on 2018-03-01.
 */

public class CameraController implements SensorEventListener {

    public interface CameraControllerCallback {
        void onPictureTaken();
    }

    private Camera mCamera;

    private File sdRoot;
    private String dir;
    private String fileName;
    private int degrees = -1;
    private int orientation;
    private ExifInterface exif;
    private int deviceHeight;

    private SensorManager sensorManager;

    public CameraController(final Context context, final Bundle savedInstanceState) {
        // Setting all the path for the image
        sdRoot = Environment.getExternalStorageDirectory();
        dir = "/DCIM/Camera/";


        // Getting the sensor service.
        sensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
    }

    public void onDestroy() {

    }

    public void onResume() {
        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Setting the right parameters in the camera
//        Camera.Parameters params = mCamera.getParameters();
//        params.setPictureSize(1600, 1200);
//        params.setPictureFormat(PixelFormat.JPEG);
//        params.setJpegQuality(85);
//        mCamera.setParameters(params);

        // Test if there is a camera on the device and if the SD card is
        // mounted.
//        if (!checkCameraHardware(this)) {
//            Intent i = new Intent(this, NoCamera.class);
//            startActivity(i);
//            finish();
//        } else if (!checkSDCard()) {
//            Intent i = new Intent(this, NoSDCard.class);
//            startActivity(i);
//            finish();
//        }


        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void onPause() {
        if (mCamera != null) {
            mCamera.release(); // release the camera for other applications
            mCamera = null;
        }

        // release the camera immediately on pause event
        sensorManager.unregisterListener(this);
    }

    @Nullable
    public Camera getCamera() {
        return mCamera;
    }

    public void takePicture() {

//                mCamera.takePicture(null, null, mPicture);
    }

    public void retakePicture() {
        //                // Deleting the image from the SD card/
//                File discardedPhoto = new File(sdRoot, dir + fileName);
//                discardedPhoto.delete();
//
//                // Restart the camera preview.
//                mCamera.startPreview();
//
//                // Reorganize the buttons on the screen
//                flBtnContainer.setVisibility(LinearLayout.VISIBLE);
//                ibRetake.setVisibility(LinearLayout.GONE);
//                ibUse.setVisibility(LinearLayout.GONE);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean checkSDCard() {
        boolean state = false;

        String sd = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(sd)) {
            state = true;
        }

        return state;
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    @Nullable
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            // attempt to get a Camera instance
            c = Camera.open();
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }

        // returns null if camera is unavailable
        return c;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {

            // Replacing the button after a photho was taken.

            // File name of the image that we just took.
            fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()).toString() + ".jpg";

            // Creating the directory where to save the image. Sadly in older
            // version of Android we can not get the Media catalog name
            File mkDir = new File(sdRoot, dir);
            mkDir.mkdirs();

            // Main file where to save the data that we recive from the camera
            File pictureFile = new File(sdRoot, dir + fileName);

            try {
                FileOutputStream purge = new FileOutputStream(pictureFile);
                purge.write(data);
                purge.close();
            } catch (FileNotFoundException e) {
                Log.d("DG_DEBUG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("DG_DEBUG", "Error accessing file: " + e.getMessage());
            }

            // Adding Exif data for the orientation. For some strange reason the
            // ExifInterface class takes a string instead of a file.
            try {
                exif = new ExifInterface("/sdcard/" + dir + fileName);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + orientation);
                exif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * Putting in place a listener so we can get the sensor data only when
     * something changes.
     */
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                RotateAnimation animation = null;
                if (event.values[0] < 4 && event.values[0] > -4) {
                    if (event.values[1] > 0 && orientation != ExifInterface.ORIENTATION_ROTATE_90) {
                        // UP
                        orientation = ExifInterface.ORIENTATION_ROTATE_90;
                        animation = getRotateAnimation(270);
                        degrees = 270;
                    } else if (event.values[1] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_270) {
                        // UP SIDE DOWN
                        orientation = ExifInterface.ORIENTATION_ROTATE_270;
                        animation = getRotateAnimation(90);
                        degrees = 90;
                    }
                } else if (event.values[1] < 4 && event.values[1] > -4) {
                    if (event.values[0] > 0 && orientation != ExifInterface.ORIENTATION_NORMAL) {
                        // LEFT
                        orientation = ExifInterface.ORIENTATION_NORMAL;
                        animation = getRotateAnimation(0);
                        degrees = 0;
                    } else if (event.values[0] < 0 && orientation != ExifInterface.ORIENTATION_ROTATE_180) {
                        // RIGHT
                        orientation = ExifInterface.ORIENTATION_ROTATE_180;
                        animation = getRotateAnimation(180);
                        degrees = 180;
                    }
                }
                if (animation != null) {
//                        rotatingImage.startAnimation(animation);
                }
            }

        }
    }

    /**
     * Calculating the degrees needed to rotate the image imposed on the button
     * so it is always facing the user in the right direction
     *
     * @param toDegrees
     * @return
     */
    private RotateAnimation getRotateAnimation(float toDegrees) {
        float compensation = 0;

        if (Math.abs(degrees - toDegrees) > 180) {
            compensation = 360;
        }

        // When the device is being held on the left side (default position for
        // a camera) we need to add, not subtract from the toDegrees.
        if (toDegrees == 0) {
            compensation = -compensation;
        }

        // Creating the animation and the RELATIVE_TO_SELF means that he image
        // will rotate on it center instead of a corner.
        RotateAnimation animation = new RotateAnimation(degrees, toDegrees - compensation, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        // Adding the time needed to rotate the image
        animation.setDuration(250);

        // Set the animation to stop after reaching the desired position. With
        // out this it would return to the original state.
        animation.setFillAfter(true);

        return animation;
    }

    /**
     * STUFF THAT WE DON'T NEED BUT MUST BE HEAR FOR THE COMPILER TO BE HAPPY.
     */
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}