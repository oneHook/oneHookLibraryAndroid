package com.onehook.hardware.camera1;

/**
 * Created by EagleDiaoOptimity on 2018-03-04.
 */

public class CameraConfig {

    public static final int HIGHIST_POSSIBLE = -1;

    public static final int FRONT_FACING = 0;
    public static final int BACK_FACING = 1;

    public int cameraInfo = BACK_FACING;

    public int jpegQuality = 85;

    public int shortEdgeLength = HIGHIST_POSSIBLE;
}
