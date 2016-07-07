package com.onehook.util.device;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

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
}
