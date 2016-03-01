package com.onehook.util.animator.interpolator;

import android.view.animation.Interpolator;

/**
 * Created by EagleDiao on 2016-03-01.
 */
public class PopInterpolator implements Interpolator {

    private float mPopDegree;

    public PopInterpolator(final float popDegree) {
        mPopDegree = popDegree;
    }

    @Override
    public float getInterpolation(float v) {
        final float y = (float) ((-3 * Math.pow(v, 3) - 2 * Math.pow(v, 2) + 5 * v) * (mPopDegree - 1) + 1);
        return y;
    }
}
