package com.onehook.hardware.camera1;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by EagleDiaoOptimity on 2018-03-01.
 * Camera view for < 21.
 */
public class Camera1View extends SurfaceView implements SurfaceHolder.Callback {

    private static final String DEBUG_TAG = "oneHook";

    /**
     * Surface holder.
     */
    private SurfaceHolder mHolder;

    /**
     * System camera object.
     */
    private Camera mCamera;

    /**
     * Camera config object.
     */
    private CameraConfig mCameraConfig;

    /**
     * Hosted preview view size. calculated in onMeasure.
     */
    private Point mPreviewViewSize;

    private boolean mIsSafeToTakePicture = false;

    public Camera1View(Context context, Camera1Controller cameraController) {
        super(context);
        mCamera = cameraController.getCamera();
        mCameraConfig = cameraController.getCameraConfig();
        mPreviewViewSize = new Point();
        mHolder = getHolder();
        mHolder.addCallback(this);
        cameraController.setView(this);

        /* deprecated setting, but required on Android versions prior to 3.0 */
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            restartCamera();
            mIsSafeToTakePicture = true;
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /* If your preview can change or rotate, take care of those events here.
           Make sure to stop the preview before resizing or reformatting it. */
        if (mHolder.getSurface() == null) {
            /* preview surface does not exist */
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "SURFACE CHANGED Failed to stop preview: " + e.getMessage());
        }

        try {
            restartCamera();
            mIsSafeToTakePicture = true;
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "SURFACE DESTROYED Failed to stop preview: " + e.getMessage());
        }
        mIsSafeToTakePicture = false;
    }

    /**
     * Restart the camera.
     *
     * @throws IOException exception
     */
    private void restartCamera() throws IOException {
        final Camera.Parameters params = mCamera.getParameters();
        final Camera.Size previewSize = getOptimalPreviewSize(mCamera.getParameters().getSupportedPreviewSizes(),
                mPreviewViewSize.x,
                mPreviewViewSize.y);

        Camera.Size pictureSize = params.getSupportedPictureSizes().get(0);

        if (mCameraConfig.shortEdgeLength != CameraConfig.HIGHIST_POSSIBLE) {
            for (Camera.Size size : params.getSupportedPictureSizes()) {
                if (Math.min(size.width, size.height) < mCameraConfig.shortEdgeLength) {
                    break;
                }
                pictureSize = size;
            }
        }
        params.setPictureSize(pictureSize.width, pictureSize.height);
        params.setPictureFormat(PixelFormat.JPEG);
        params.setJpegQuality(mCameraConfig.jpegQuality);

        final Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraConfig.cameraInfo, cameraInfo);


        if (mCameraConfig.cameraInfo == CameraConfig.BACK_FACING) {
            /* back camera */
            /* TODO orientation calculation*/
            if (display.getRotation() == Surface.ROTATION_0) {
                mCamera.setDisplayOrientation(90);
            } else if (display.getRotation() == Surface.ROTATION_90) {
            } else if (display.getRotation() == Surface.ROTATION_180) {
            } else if (display.getRotation() == Surface.ROTATION_270) {
                mCamera.setDisplayOrientation(180);
            }
        } else {
            /* front camera */
            /* TODO orientation calculation */
            if (display.getRotation() == Surface.ROTATION_0) {
                /* Super hacky */
                if("Nexus 5".equals(Build.MODEL)) {
                    mCamera.setDisplayOrientation(90);
                } else {
                    final int degrees = 0;
                    int result = (cameraInfo.orientation + degrees) % 360;
                    result = (360 - result) % 360;
                    mCamera.setDisplayOrientation(result);
                }
            } else if (display.getRotation() == Surface.ROTATION_90) {
                mCamera.setDisplayOrientation(90);
            } else if (display.getRotation() == Surface.ROTATION_180) {
                mCamera.setDisplayOrientation(90);
            } else if (display.getRotation() == Surface.ROTATION_270) {
                mCamera.setDisplayOrientation(90);
            }
        }

        /* make sure to auto focus */
        if (params.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (params.getSupportedFocusModes().size() > 0) {
            params.setFocusMode(params.getSupportedFocusModes().get((0)));
        }

        mCamera.setParameters(params);
        mCamera.setPreviewDisplay(mHolder);
        mCamera.startPreview();
    }

    public void startPreview() {
        mCamera.startPreview();
        mIsSafeToTakePicture = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mPreviewViewSize.set(width, height);
    }

    /**
     * Get optimal preview size based on current view width and height.
     *
     * @param sizes device supported sizes
     * @param w     width
     * @param h     height
     * @return optimal size
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public boolean isSafeToTake() {
        return mIsSafeToTakePicture;
    }

    public void takePicture(Camera.ShutterCallback callback, Camera.PictureCallback rawPictureCallback, Camera.PictureCallback jpgPictureCallback ) {
        mCamera.takePicture(callback, rawPictureCallback, jpgPictureCallback);
        mIsSafeToTakePicture = false;
    }
}