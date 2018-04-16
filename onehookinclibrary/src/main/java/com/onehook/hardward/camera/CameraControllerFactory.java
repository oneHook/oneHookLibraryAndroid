package com.onehook.hardward.camera;

import android.content.Context;

import com.onehook.hardward.camera.camera1.Camera1Controller;

public class CameraControllerFactory {

    public static BaseCameraController createCameraController(final Context context,
                                                              CameraConfig config) {
        return new Camera1Controller(context, config);
    }
}
