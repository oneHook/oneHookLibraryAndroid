package com.onehook.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.onehook.util.animator.AnimationEndListener;

/**
 * Created by EagleDiao on 15-07-23.
 */
public class CountDownTextView extends TextView {

    public interface CountDownTextViewCallback {
        void onCountDownFinished();

        void onCountDownStopped();

        void onCountDownStarted();
    }

    private int remainingTime;

    private CountDownTextViewCallback mCallback;

    private ObjectAnimator mCountDownObjectAnimator;

    public CountDownTextView(Context context) {
        super(context);
        commonInit(context, null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, AttributeSet attrs) {
        setRemainingTime(0);
    }

    public void setRemainingTime(final int remainTime) {
        this.remainingTime = remainTime;
        final int mSecs = remainTime % 1000 / 10;
        final int sec = (remainTime / 1000) % 60;
        final int min = remainTime / 1000 / 60;
        final String text = String.format("%02d:%02d:%02d", min, sec, mSecs);
        setText(text);
    }

    public void startCountDown() {
        if (mCountDownObjectAnimator != null) {
            throw new RuntimeException("Count down already in progress!");
        }
        mCountDownObjectAnimator
                = ObjectAnimator.ofInt(this, "remainingTime", remainingTime, 0);
        mCountDownObjectAnimator.setInterpolator(new LinearInterpolator());
        mCountDownObjectAnimator.setDuration(remainingTime);
        mCountDownObjectAnimator.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEndOrCanceled(Animator animation) {
                if (mCallback != null) {
                    mCallback.onCountDownFinished();
                }
            }
        });
        mCountDownObjectAnimator.start();
        if (mCallback != null) {
            mCallback.onCountDownStarted();
        }
    }

    public void stopCountDown() {
        if (mCountDownObjectAnimator == null) {
            throw new RuntimeException("Count down is not started!");
        }
        mCountDownObjectAnimator.cancel();
        mCountDownObjectAnimator = null;
        if (mCallback != null) {
            mCallback.onCountDownStopped();
        }
    }

    public boolean isCountDownInProgress() {
        return mCountDownObjectAnimator != null;
    }

    public void setCallback(CountDownTextViewCallback callback) {
        mCallback = callback;
    }
}
