package com.onehook.view.pager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class CarouselTransformer implements ViewPager.PageTransformer {

    /**
     * Padding on the view pager.
     */
    private float mPadding;

    /**
     * Maximum Scale applied to previous and next page.
     */
    private float mScale;

    /**
     * Carousel Transformer with padding (same as in view pager) and
     * scale (scale apply to each previous and next page).
     *
     * @param padding padding
     * @param scale  scale
     */
    public CarouselTransformer(final int padding, final float scale) {
        mPadding = padding;
        mScale = scale;
    }

    @Override
    public void transformPage(View view, float position) {
        /* offset the position to -1, 0, 1 since we may have a padding on the pager */
        position -= (mPadding * 1.0f / view.getMeasuredWidth());

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        final float scale = 1 - Math.abs(position) * (1 - mScale);
        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
