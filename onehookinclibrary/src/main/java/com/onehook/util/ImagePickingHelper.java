package com.onehook.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by EagleDiao on 2016-02-10.
 */
public class ImagePickingHelper {

    public static Intent createImagePickingIntent(final Context context, final File newImageFile, final int res) {

        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        final Intent chooserIntent = Intent.createChooser(pickIntent, context.getString(res));
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
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


//        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        final Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("image/*");
//        final Intent chooserIntent = Intent.createChooser(pickIntent, context.getString(res));
//        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//            final List<Intent> cameraIntents = new ArrayList<>();
//            final File photoFile = newImageFile;
//            if (photoFile != null) {
//                takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                for (ResolveInfo ri : context.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)) {
//                    final Intent intent = context.getPackageManager().getLaunchIntentForPackage(ri.activityInfo.packageName);
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                    cameraIntents.add(intent);
//                }
//                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
//                return chooserIntent;
//            }
//        }
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{});
//        return pickIntent;
    }

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
}
