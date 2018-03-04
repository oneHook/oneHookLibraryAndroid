package com.onehook.hardware.camera1;

import android.media.ExifInterface;

/**
 * Created by eaglediaooptimity on 2018-03-02.
 */
public interface CameraControllerCallback {

    void onPictureTaken(byte[] data, final ExifInterface exif);
}