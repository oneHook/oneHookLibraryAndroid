package com.onehook.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by EagleDiao on 15-07-23.
 */
public class CountDownTextView extends TextView {

    private int remainingTime;

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

    }

    public void setRemainingTime(final int remainTime) {
        this.remainingTime = remainTime;
        System.out.println("oneHook remainTime + " +  remainTime);
        final int mSecs = remainTime % 1000 / 10;
        final int sec = (remainTime / 1000) % 60;
        final int min = remainTime / 1000 / 60;
        System.out.println("min : " + min + " sec " + sec + " , ms" + mSecs);
        final String text = String.format("%02d:%02d:%02d", min, sec, mSecs);
        setText(text);
    }

    public void startCountDown() {
        ObjectAnimator anim
                = ObjectAnimator.ofInt(this, "remainingTime", remainingTime, 0);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(remainingTime);
        anim.start();
    }
}
