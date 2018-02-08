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

    public static int getDarkenColor(int color, float degree) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        degree = 1 - degree;
        return Color.rgb((int) (r * degree), (int) (g * degree), (int) (b * degree));
    }

    public static int getAlphaColor(int color, float alpha) {
        final int r = Color.red(color);
        final int b = Color.blue(color);
        final int g = Color.green(color);
        return Color.argb(255 * alpha, r, g, b);
    }

    public static int getWhiteColor(final float degree) {
        int comp = (int) (255 * degree);
        return Color.rgb(comp, comp, comp);
    }

    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }


}
