package com.onehook.hardward.camera.camera1;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.onehook.hardward.camera.CameraConfig;

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
    private Point mDesiredPreviewViewSize;

    private boolean mIsSafeToTakePicture = false;

    public Camera1View(Context context, Camera1Controller cameraController) {
        super(context);
        mCameraConfig = cameraController.getCameraConfig();
        mDesiredPreviewViewSize = new Point();
        mHolder = getHolder();
        mHolder.addCallback(this);

        /* deprecated setting, but required on Android versions prior to 3.0 */
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(final Camera camera) {
        mCamera = camera;
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
            stopCamera();
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
            stopCamera();
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
    protected void restartCamera() throws IOException {
        if (mCamera == null) {
            return;
        }
        final Camera.Parameters params = mCamera.getParameters();
        final SizePair sp = generateValidPreviewSize(mCamera, mDesiredPreviewViewSize.x, mDesiredPreviewViewSize.y);

        params.setPictureSize(sp.mPicture.getWidth(), sp.mPicture.getHeight());
        params.setPreviewSize(sp.mPicture.getWidth(), sp.mPicture.getHeight());
        params.setPictureFormat(PixelFormat.JPEG);
        params.setJpegQuality(mCameraConfig.jpegQuality);

        final Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        final Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
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
            final int degrees;
            if (display.getRotation() == Surface.ROTATION_0) {
                degrees = 0;
            } else if (display.getRotation() == Surface.ROTATION_90) {
                degrees = 90;
            } else if (display.getRotation() == Surface.ROTATION_180) {
                degrees = 180;
            } else {
                degrees = 270;
            }

            int result = (cameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;
            mCamera.setDisplayOrientation(result);
            params.setRotation(result);
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

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
            mIsSafeToTakePicture = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mDesiredPreviewViewSize.set(width, height);
    }

    public boolean isSafeToTake() {
        return mIsSafeToTakePicture;
    }

    public void takePicture(Camera.ShutterCallback callback, Camera.PictureCallback rawPictureCallback, Camera.PictureCallback jpgPictureCallback) {
        mCamera.takePicture(callback, rawPictureCallback, jpgPictureCallback);
        mIsSafeToTakePicture = false;
    }

    /* Optimal Preview/Picture size */

    private static final double MAX_ASPECT_DISTORTION = 0.15;
    private static final float ASPECT_RATIO_TOLERANCE = 0.01f;

    // desiredWidth and desiredHeight can be the screen size of mobile device
    private static SizePair generateValidPreviewSize(Camera camera,
                                                     int desiredWidth,
                                                     int desiredHeight) {
        Camera.Parameters parameters = camera.getParameters();
        double screenAspectRatio = desiredWidth / (double) desiredHeight;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        SizePair bestPair = null;
        double currentMinDistortion = MAX_ASPECT_DISTORTION;
        for (Camera.Size previewSize : supportedPreviewSizes) {
            float previewAspectRatio = (float) previewSize.width / (float) previewSize.height;
            for (Camera.Size pictureSize : supportedPictureSizes) {
                float pictureAspectRatio = (float) pictureSize.width / (float) pictureSize.height;
                if (Math.abs(previewAspectRatio - pictureAspectRatio) < ASPECT_RATIO_TOLERANCE) {
                    SizePair sizePair = new SizePair(previewSize, pictureSize);

                    boolean isCandidatePortrait = previewSize.width < previewSize.height;
                    int maybeFlippedWidth = isCandidatePortrait ? previewSize.width : previewSize.height;
                    int maybeFlippedHeight = isCandidatePortrait ? previewSize.height : previewSize.width;
                    double aspectRatio = maybeFlippedWidth / (double) maybeFlippedHeight;
                    double distortion = Math.abs(aspectRatio - screenAspectRatio);
                    if (distortion < currentMinDistortion) {
                        currentMinDistortion = distortion;
                        bestPair = sizePair;
                    }
                    break;
                }
            }
        }

        return bestPair;
    }


    private static class SizePair {
        private Size mPreview;
        private Size mPicture;

        public SizePair(Camera.Size previewSize, Camera.Size pictureSize) {
            mPreview = new Size(previewSize.width, previewSize.height);
            if (pictureSize != null) {
                mPicture = new Size(pictureSize.width, pictureSize.height);
            }
        }

        public Size previewSize() {
            return mPreview;
        }

        @SuppressWarnings("unused")
        public Size pictureSize() {
            return mPicture;
        }
    }
}