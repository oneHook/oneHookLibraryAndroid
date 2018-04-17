package com.onehook.util.device;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import java.util.UUID;

/**
 * Created by eaglediaotomore on 2016-06-12.
 */

public class DeviceInfoUtil {

    private static Point sDisplayPoint;

    public static void init(Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display defaultDisplay = wm.getDefaultDisplay();
        sDisplayPoint = new Point();
        defaultDisplay.getSize(sDisplayPoint);
    }

    public static final Point getDisplayPoint() {
        return sDisplayPoint;
    }

    public static String generateDeviceID() {
        return UUID.randomUUID().toString();
    }

    public static String getDeviceName() {
        return Build.MODEL;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDevicePlatformVersion() {
        return String.format("%s (%d)", Build.VERSION.CODENAME, Build.VERSION.SDK_INT);
    }
}
