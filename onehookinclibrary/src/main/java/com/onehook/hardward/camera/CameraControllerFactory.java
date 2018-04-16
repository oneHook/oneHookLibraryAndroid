package com.onehook.hardward.camera;

import android.content.Context;
import android.os.Build;

import com.onehook.hardward.camera.camera1.Camera1Controller;
import com.onehook.hardward.camera.camera2.Camera2Controller;

public class CameraControllerFactory {

    public static BaseCameraController createCameraController(final Context context,
                                                              CameraConfig config) {
        if (Build.VERSION.SDK_INT >= 21) {
            return new Camera2Controller(context, config);
        } else {
            return new Camera1Controller(context, config);
        }
    }
}
