package com.onehook.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eaglediaotomore on 2016-11-08.
 */

public class AnimatedCircleToggle extends View {

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

        /*
         * CUSTOM DRAWING!!
         */
        setWillNotDraw(false);

        mPaint = new Paint();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final float borderThickness = height * 0.1f;
        final float selectionDotRadius = height * 0.3f;

        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);


        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(borderThickness);

        canvas.drawCircle(width / 2, height / 2, width / 2 - borderThickness / 2, mPaint);

        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(width / 2, height / 2, selectionDotRadius * progress, mPaint);
    }

    public void setToogleOn(final boolean toogleOn, final boolean animated) {
        if (animated) {
            final ObjectAnimator animation = ObjectAnimator.ofFloat(this, "progress", this.progress, toogleOn ? 1f : 0);
            animation.setDuration(200);
            animation.start();
        } else {
            this.progress = toogleOn ? 1 : 0;
            invalidate();
        }
    }

    public void setProgress(final float progress) {
        this.progress = progress;
        invalidate();
    }

    public float getProgress() {
        return this.progress;
    }

}
