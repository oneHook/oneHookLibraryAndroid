package com.onehook.util.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.onehook.util.animator.interpolator.PopInterpolator;

/**
 * Created by EagleDiao on 2016-03-01.
 */
public class AnimationGenerator {

    /**
     * Start a infinite pop animation.
     *
     * @param view        dest view to start animation
     * @param scaleDegree scale degree > 1.0f
     * @param popDegree   pop degree > 1.0f
     * @param duration    duration in miliseconds.
     */
    public static void startInfinitePopAnimationWithDegree(final View view, final float scaleDegree, final float popDegree, final long duration) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", scaleDegree);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", scaleDegree);
        scaleDownX.setInterpolator(new PopInterpolator(popDegree));
        scaleDownY.setInterpolator(new PopInterpolator(popDegree));
        scaleDownX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDownY.setRepeatCount(ObjectAnimator.INFINITE);
        final AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);
        scaleDown.setDuration(duration);
        scaleDown.start();
    }
}
