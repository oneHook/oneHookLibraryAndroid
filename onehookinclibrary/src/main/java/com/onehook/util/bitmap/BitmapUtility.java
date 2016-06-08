package com.onehook.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by EagleDiao on 2016-05-30.
 */

public class BitmapUtility {

    public static String DEBUG_TAG = null;

    public static void rotatePhotoFile(final File file) {
        final Bitmap rotated = getRotatedBitmap(file);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void rotatePhotoFile(final File file, int rotation) {
        final Bitmap rotated = getRotatedBitmap(file, rotation);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            rotated.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getBitmapFileRotation(final File file) {
        final long startTime = System.currentTimeMillis();
        int rotate = 0;
        try {
            File imageFile = file;

            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            if (DEBUG_TAG != null) {
                Log.d(DEBUG_TAG, "Exif orientation: " + orientation + " rotate " + rotate);
                Log.d(DEBUG_TAG, "Time spent to get exif info " + (System.currentTimeMillis() - startTime) + " ms");
            }
            return rotate;
        } catch (Exception e) {
            e.printStackTrace();
            if (DEBUG_TAG != null) {
                Log.d(DEBUG_TAG, "Failed to retrieve Exif orientation");
            }
        } finally {
            return rotate;
        }
    }

    public static Bitmap getRotatedBitmap(final File file, final int rotation) {
        Bitmap originalBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        final Bitmap rv = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
        if (rv != originalBitmap) {
            originalBitmap.recycle();
        }
        return rv;
    }

    public static Bitmap getRotatedBitmap(final File file) {
        int rotation = getBitmapFileRotation(file);
        return getRotatedBitmap(file, rotation);
    }
}
