package com.onehook.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by EagleDiao on 2016-02-10.
 */
public class ImagePickingHelper {

    /**
     * Create an image picking intent with both gallery and camera. Camera intent may
     * require permission (only when camera permission is listed in manifest file).
     *
     * @param context      context
     * @param newImageFile image file (only needed for camera)
     * @param res          title res for chooser
     * @return intent
     */
    public static Intent createCameraAndGalleryIntent(@NonNull final Context context,
                                                      @NonNull final File newImageFile,
                                                      @StringRes final int res) {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        final Intent chooserIntent = Intent.createChooser(pickIntent, context.getString(res));
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            final File photoFile = newImageFile;
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
                return chooserIntent;
            }
        }
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{});
        return pickIntent;
    }

    /**
     * Create an image picking intent with gallery only. No permission required.
     *
     * @param context context
     * @param res     title res for chooser
     * @return intent
     */
    public static Intent createGalleryIntent(@NonNull final Context context, @StringRes final int res) {
        final Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        final Intent chooserIntent = Intent.createChooser(pickIntent, context.getString(res));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{});
        return pickIntent;
    }


    /**
     * Create an image picking intent with camera only.
     *
     * @param context      context
     * @param newImageFile image file the camera will save to
     * @param res          title res for chooser
     * @return intent
     */
    public static Intent createCameraIntent(@NonNull final Context context,
                                            @NonNull final File newImageFile,
                                            @StringRes final int res) {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            final File photoFile = newImageFile;
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                return takePictureIntent;
            }
        }
        return null;
    }

    /**
     * Create a new file on external storage in picture directory. You need WRITE_EXTERNAL_STORAGE
     * for this method to work.
     *
     * @return new file or null if failed
     */
    @Nullable
    public static File createNewImageFile() {
        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        final String imageFileName = "JPEG_" + timeStamp + "_";
        final File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        try {
            final File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            Log.d("OH ImagePickingHelper", "Create new temp file " + image.getAbsolutePath());
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Create a new image file on internal storage (for current app). No permission needed.
     *
     * @param context  context
     * @param filename name of the file, if null, a default file name will be used
     * @return file, null if failed
     */
    @Nullable
    public static File createNewImageFileInInternal(@Nullable final Context context,
                                                    @Nullable final String filename) {
        final String imageFileName;
        if (filename != null) {
            imageFileName = filename;
        } else {
            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName = "JPEG_" + timeStamp + "_";
        }
        final File storageDir = context.getFilesDir();
        try {
            final File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",   /* suffix */
                    storageDir      /* directory */
            );
            Log.d("OH ImagePickingHelper", "Create new temp file " + image.getAbsolutePath());
            return image;
        } catch (IOException e) {
            return null;
        }
    }
}
