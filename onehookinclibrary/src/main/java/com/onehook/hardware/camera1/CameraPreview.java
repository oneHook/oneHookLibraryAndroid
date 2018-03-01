package com.onehook.hardware.camera1;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
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
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private static final String DEBUG_TAG = "oneHook";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private Point mPreviewViewSize;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mPreviewViewSize = new Point();

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        mHolder.setFixedSize(100, 100);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the
        // preview.
        System.out.println(DEBUG_TAG + " surface created");
        try {
            restartCamera();
        } catch (IOException e) {
            Log.d(DEBUG_TAG, "Error setting camera preview: " + e.getMessage());
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        System.out.println(DEBUG_TAG + " surface changed");
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // make any resize, rotate or reformatting changes here

        // start preview with new settings
        try {
           restartCamera();
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio=(double)h / w;

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

    private void restartCamera() throws IOException {

        Camera.Parameters params = mCamera.getParameters();

        System.out.println(DEBUG_TAG + " supported preview sizes");
        for(Camera.Size size : mCamera.getParameters().getSupportedPreviewSizes()) {


            System.out.println(DEBUG_TAG + "width " + size.width + " height " + size.height);
        }

        final Camera.Size previewSize = getOptimalPreviewSize(mCamera.getParameters().getSupportedPreviewSizes(), mPreviewViewSize.x, mPreviewViewSize.y);
        params.setPreviewSize(previewSize.width, previewSize.height);

        System.out.println(DEBUG_TAG + "preview size width " + previewSize.width + " height " + previewSize.height);
//        params.setPictureSize(1600, 1200);
//        params.setPictureFormat(PixelFormat.JPEG);
//        params.setJpegQuality(85);

        Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0) {
//            params.setPreviewSize(previewSize.height, previewSize.width);
            mCamera.setDisplayOrientation(90);
            System.out.println(DEBUG_TAG + " rotation 0");
        }

        if(display.getRotation() == Surface.ROTATION_90) {
            params.setPreviewSize(previewSize.width, previewSize.height);
            System.out.println(DEBUG_TAG + " rotation 90");
        }

        if(display.getRotation() == Surface.ROTATION_180) {
            params.setPreviewSize(previewSize.height, previewSize.width);
            System.out.println(DEBUG_TAG + " rotation 180");
        }

        if(display.getRotation() == Surface.ROTATION_270) {
            params.setPreviewSize(previewSize.width, previewSize.height);
            mCamera.setDisplayOrientation(180);
            System.out.println(DEBUG_TAG + " rotation 270");
        }

        requestLayout();

        mCamera.setParameters(params);

        mCamera.setPreviewDisplay(mHolder);
        mCamera.startPreview();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.

        System.out.println(DEBUG_TAG + " surface destroyed");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        mPreviewViewSize.set(width, height);
        System.out.println(DEBUG_TAG + " preview width  " + width);
        System.out.println(DEBUG_TAG + " preview height " + height);
    }
}