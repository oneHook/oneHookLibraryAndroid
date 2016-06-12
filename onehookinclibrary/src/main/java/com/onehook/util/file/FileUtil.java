package com.onehook.util.file;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by EagleDiao on 2016-06-08.
 */

public class FileUtil {

    public static String DEBUG_TAG = null;

    /**
     * Save to a file from source uri. This operation will happen in caller's thread.
     *
     * @param context   context
     * @param sourceUri source uri
     * @param file      a file to be saved to
     */
    public static boolean savefileAsync(final Context context, Uri sourceUri, File file) {
        if (file == null || !file.canWrite()) {
            throw new RuntimeException("File should not be null and file should be writable");
        }
        final long startTime = System.currentTimeMillis();
        final String destinationFilename = file.getAbsolutePath();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(context.getContentResolver().openInputStream(sourceUri));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            final int available = bis.available();
            int read = 0;
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                read += 1024;
                if (DEBUG_TAG != null) {
//                    Log.d(DEBUG_TAG, "Write file: available " + available + " , read " + read);
                }
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (DEBUG_TAG != null) {
                Log.d(DEBUG_TAG, "Write file cost : " + (System.currentTimeMillis() - startTime) + " ms");
            }
            return true;
        }
    }
}
