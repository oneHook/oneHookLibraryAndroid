package com.onehook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 15-07-26.
 */
public class AnimatedProgressBar extends View {

    private float primaryProgress;

    private float secondaryProgress;

    private int baseColor;

    private int primaryProgressColor;

    private int secondaryProgressColor;

    /**
     * Main paint. for drawing progress base, and progress bar.
     */
    private Paint mPaint;

    public AnimatedProgressBar(Context context) {
        super(context);
        commonInit(context, null);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public AnimatedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, AttributeSet attrs) {

        /*
         * CUSTOM DRAWING!!
         */
        setWillNotDraw(false);

        this.primaryProgress = 0.5f;
        this.secondaryProgress = 0.75f;
        this.baseColor = Color.WHITE;
        this.primaryProgressColor = Color.RED;
        this.secondaryProgressColor = Color.YELLOW;

                /*
         * Load from style if any.
         */
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedProgressBar);
            this.primaryProgress = a.getFloat(R.styleable.AnimatedProgressBar_oh_progress_bar_primary_progress, this.primaryProgress);
            this.secondaryProgress = a.getFloat(R.styleable.AnimatedProgressBar_oh_progress_bar_secondary_progress, this.secondaryProgress);
            this.baseColor = a.getColor(R.styleable.AnimatedProgressBar_oh_progress_bar_progress_base_color, this.baseColor);
            this.primaryProgressColor = a.getColor(R.styleable.AnimatedProgressBar_oh_progress_bar_primary_progress_color, this.primaryProgressColor);
            this.secondaryProgressColor = a.getColor(R.styleable.AnimatedProgressBar_oh_progress_bar_secondary_progress_color, this.secondaryProgressColor);
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final float baseThickness = height * 0.85f;
        final float progressThickness = height * 0.6f;
        final float progressXOffset = height * 0.15f;

        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);


        mPaint.setColor(this.baseColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(baseThickness);

        canvas.drawLine(baseThickness / 2, height / 2, width - baseThickness / 2, height / 2, mPaint);

        mPaint.setStrokeWidth(progressThickness);
        mPaint.setColor(this.secondaryProgressColor);
        canvas.drawLine(progressThickness / 2 + progressXOffset,
                height / 2,
                Math.max(0, getMeasuredWidth() * this.secondaryProgress - progressThickness / 2 - 2 * progressXOffset),
                height / 2, mPaint);

        mPaint.setColor(this.primaryProgressColor);
        final float primaryLineWidth = Math.max(0, getMeasuredWidth() * this.primaryProgress - progressThickness / 2 - 2 * progressXOffset);
        if(primaryLineWidth >= progressThickness) {
            canvas.drawLine(progressThickness / 2 + progressXOffset,
                    height / 2,
                    Math.max(0, getMeasuredWidth() * this.primaryProgress - progressThickness / 2 - 2 * progressXOffset),
                    height / 2, mPaint);
        }
    }

    public void setPrimaryProgress(float primaryProgress) {
        this.primaryProgress = primaryProgress;
        invalidate();
    }

    public void setSecondaryProgress(float secondaryProgress) {
        this.secondaryProgress = secondaryProgress;
        invalidate();
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
        invalidate();
    }

    public void setPrimaryProgressColor(int primaryProgressColor) {
        this.primaryProgressColor = primaryProgressColor;
        invalidate();
    }

    public void setSecondaryProgressColor(int secondaryProgressColor) {
        this.secondaryProgressColor = secondaryProgressColor;
        invalidate();
    }
}
