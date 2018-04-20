package com.onehook.view;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.onehook.util.StringUtil;
import com.onehook.util.color.ColorUtility;
import com.onehookinc.androidlib.R;

/**
 * Created by eaglediaotomore on 2016-11-08.
 */

public class AnimatedCircleToggle extends View {

    @ColorInt
    private int mOnColor = Color.RED;

    @ColorInt
    private int mOffColor = Color.BLACK;

    private float mBorderRatio = 0.1f;
    private float mInternalRatio = 0.3f;
    private int mAnimationDuration = 200;

    /**
     * Main paint. for drawing progress base, and progress bar.
     */
    private Paint mPaint;

    private float progress;

    public AnimatedCircleToggle(Context context) {
        super(context);
        commonInit(context, null);
    }

    public AnimatedCircleToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public AnimatedCircleToggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public AnimatedCircleToggle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedCircleToggle);

            mOnColor = a.getColor(R.styleable.AnimatedCircleToggle_on_color,
                    mOnColor);
            mOnColor = a.getColor(R.styleable.AnimatedCircleToggle_on_color,
                    mOnColor);
            mBorderRatio = a.getFloat(R.styleable.AnimatedCircleToggle_border_thickness_ratio,
                    mBorderRatio);
            mInternalRatio = a.getFloat(R.styleable.AnimatedCircleToggle_internal_thickness_ratio,
                    mInternalRatio);
            mAnimationDuration = a.getInteger(R.styleable.AnimatedCircleToggle_animation_duration,
                    mAnimationDuration);
            a.recycle();
        }

        /*
         * CUSTOM DRAWING!!
         */
        setWillNotDraw(false);

        mPaint = new Paint();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progress == 1) {
                    setToggleOn(false, true);
                } else {
                    setToggleOn(true, true);
                }
            }
        });
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final float borderThickness = height * mBorderRatio;
        final float selectionDotRadius = height * mInternalRatio;

        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        final int color = ColorUtility.getTransitionColor(mOnColor, mOffColor, this.progress);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(borderThickness);
        canvas.drawCircle(width / 2, height / 2, width / 2 - borderThickness / 2, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, selectionDotRadius * progress, mPaint);
    }

    /**
     * @param toggleOn
     */
    public void setToggleOn(final boolean toggleOn) {
        setToggleOn(toggleOn, true);
    }

    /**
     * @param toggleOn
     * @param animated
     */
    public void setToggleOn(final boolean toggleOn,
                            final boolean animated) {
        if (animated) {
            final ObjectAnimator animation = ObjectAnimator.ofFloat(this,
                    "progress",
                    this.progress,
                    toggleOn ? 1f : 0);
            animation.setDuration(200);
            animation.start();
        } else {
            this.progress = toggleOn ? 1 : 0;
            invalidate();
        }
    }

    /**
     * @param progress progress 1 means on, 0 means not.
     */
    public void setProgress(final float progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * @return current progress
     */
    public float getProgress() {
        return this.progress;
    }

    /**
     * Is this toggle selected.
     *
     * @return true if selected
     */
    public boolean isSelected() {
        return this.progress == 1;
    }
}
