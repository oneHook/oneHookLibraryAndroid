package com.onehook.util.color;

import android.graphics.Color;
import android.support.annotation.ColorInt;

/**
 * Created by EagleDiao on 2016-02-15.
 */
public class ColorUtility {


    /**
     * @param color
     * @param degree
     * @return
     */
    public static int getDarkenColor(int color, float degree) {
        int r = Color.red(color);
        int b = Color.blue(color);
        int g = Color.green(color);
        degree = 1 - degree;
        return Color.rgb((int) (r * degree), (int) (g * degree), (int) (b * degree));
    }

    /**
     * @param color
     * @param alpha
     * @return
     */
    public static int getColorWithAlpha(int color, float alpha) {
        final int r = Color.red(color);
        final int b = Color.blue(color);
        final int g = Color.green(color);
        return Color.argb((int) (255 * alpha), r, g, b);
    }

    /**
     * @param degree
     * @return
     */
    public static int getWhiteColor(final float degree) {
        int comp = (int) (255 * degree);
        return Color.rgb(comp, comp, comp);
    }

    /**
     * Produce true if given color is light.
     *
     * @param color color
     * @return true if given color is light, false the color is light
     */
    public static boolean isLightColor(@ColorInt int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    /**
     * Produce the average of two colors.
     *
     * @param color1 color1
     * @param color2 color2
     * @return average of two colors.
     */
    @ColorInt
    public static int getAverageColor(@ColorInt int color1,
                                      @ColorInt int color2) {

        return getTransitionColor(color1, color2, 0.5f);
    }

    /**
     * Produce a color in between two colors with given step.
     *
     * @param fromColor from color
     * @param toColor   to color
     * @param step      step (0 - 1)
     * @return a transition color
     */
    @ColorInt
    public static int getTransitionColor(@ColorInt int fromColor, @ColorInt int toColor, float step) {

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
