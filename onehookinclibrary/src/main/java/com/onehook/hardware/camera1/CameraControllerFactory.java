package com.onehook.hardware.camera1;

import android.content.Context;

public class CameraControllerFactory {

    public static BaseCameraController createCameraController(final Context context,
                                                              CameraConfig config) {
        return new Camera1Controller(context, config);
    }
}
