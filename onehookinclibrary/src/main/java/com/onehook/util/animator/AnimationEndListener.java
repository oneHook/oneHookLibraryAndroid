package com.onehook.util.animator;

import android.animation.Animator;

/**
 * Created by EagleDiao on 15-01-13.
 */
public abstract class AnimationEndListener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        onAnimationEndOrCanceled(animation);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        onAnimationEndOrCanceled(animation);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public abstract void onAnimationEndOrCanceled(Animator animation);
}
