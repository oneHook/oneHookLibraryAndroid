package com.onehook.hardware.camera1;

import android.content.Context;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
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

    private File sdRoot;
    private String dir;
    private String fileName;
    private ExifInterface exif;

    /**
     * Camera object.
     */
    private Camera mCamera;

    /**
     * @param context
     * @param savedInstanceState
     */
    public Camera1Controller(final Context context, final Bundle savedInstanceState) {
        super(context);
        // Setting all the path for the image
        sdRoot = Environment.getExternalStorageDirectory();
        dir = "/DCIM/Camera/";

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onResume() {
        /* make sure the camera instance is ready */
        mCamera = getCameraInstance();
    }

    @Override
    public void onPause() {
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
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "" + mCurrentOrientation);
                exif.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };



}