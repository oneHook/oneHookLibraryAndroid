package com.onehook.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by EagleDiao on 2016-03-22.
 */
public class AnimatedNumberTextView extends TextView {

    protected int number;

    public AnimatedNumberTextView(Context context) {
        super(context);
    }

    public AnimatedNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public AnimatedNumberTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void commonInit() {
        setNumber(0);
    }

    /**
     * For property animator.
     *
     * @param newNumber new number
     */
    public void setNumber(final int newNumber) {
        this.number = newNumber;
        renderNumber(this.number);
    }

    /**
     * Child class can override this function to render the textview differently.
     * @param numberToRender number to render
     */
    protected void renderNumber(final int numberToRender) {
        setText(String.valueOf(numberToRender));
    }

    public ValueAnimator createAnimation(final int fromNumber, final int toNumber, final long duration) {
        return ObjectAnimator.ofInt(this, "number", fromNumber, toNumber).setDuration(duration);
    }

    public ValueAnimator createAnimation(final int toNumber, final long duration) {
        return ObjectAnimator.ofInt(this, "number", this.number, toNumber).setDuration(duration);
    }
}
