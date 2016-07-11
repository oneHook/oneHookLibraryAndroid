package com.onehook.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.request.BasePostprocessor;

/**
 * Created by eaglediaotomore on 2016-07-10.
 */

public class DarkenPostprocessor extends BasePostprocessor {

    private float mDegree;

    public DarkenPostprocessor(final float degree) {
        mDegree = degree;
    }

    @Override
    public void process(Bitmap bitmap) {
        final int height = bitmap.getHeight();
        final int width = bitmap.getWidth();
        int pixel;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixel = bitmap.getPixel(x, y);

                final int red = Color.red(pixel);
                final int green = Color.green(pixel);
                final int blue = Color.blue(pixel);

                pixel = Color.rgb(
                        applyDegree(red),
                        applyDegree(green),
                        applyDegree(blue));

                bitmap.setPixel(x, y, pixel);
            }
        }
    }

    private int applyDegree(final int val) {
        return (int) Math.min(255, val * mDegree);
    }

    @Override
    public CacheKey getPostprocessorCacheKey() {
        return new SimpleCacheKey(String.format("Darken %f", mDegree));
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
