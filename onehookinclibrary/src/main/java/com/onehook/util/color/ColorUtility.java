package com.onehook.util.color;

import android.graphics.Color;

/**
 * Created by EagleDiao on 2016-02-15.
 */
public class ColorUtility {

    public static int getTransitionColor(int fromColor, int toColor, float step) {

        final int fromRed = Color.red(fromColor);
        final int fromGreen = Color.green(fromColor);
        final int fromBlue = Color.blue(fromColor);

        final int toRed = Color.red(toColor);
        final int toGreen = Color.green(toColor);
        final int toBlue = Color.blue(toColor);

        return Color.argb(255,
                (int) (fromRed * step + toRed * (1 - step)),
                (int) (fromGreen * step + toGreen * (1 - step)),
                (int) (fromBlue * step + toBlue * (1 - step)));
    }
}
