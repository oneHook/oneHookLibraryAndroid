package com.onehook.hardward.camera;

/**
 * Created by eaglediaooptimity on 2018-03-02.
 */
public interface CameraControllerCallback {

    void onPictureTaken(byte[] data, final PictureInfo pictureInfo);
}