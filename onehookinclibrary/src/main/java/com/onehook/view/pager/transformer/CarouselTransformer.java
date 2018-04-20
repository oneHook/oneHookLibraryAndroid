package com.onehook.view.pager.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class CarouselTransformer implements ViewPager.PageTransformer {

    private float mOffset;

    public CarouselTransformer(final float offset) {
        mOffset = offset;
    }

    @Override
    public void transformPage(View view, float position) {
        position -= mOffset;
        view.setPivotY(0.5f * view.getMeasuredHeight());
        if (position < -0.5) {
            view.setPivotX(1 * view.getMeasuredWidth());
        } else if (position > 0.5) {
            view.setPivotX(0);
        } else {
            view.setPivotX(0.5f * view.getMeasuredWidth());
        }


        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }
        final float scale = 1 - Math.abs(position) * 0.2f;

        view.setScaleX(scale);
        view.setScaleY(scale);
    }
}
