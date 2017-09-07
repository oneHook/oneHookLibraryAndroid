package com.onehook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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

    private int borderColor;

    private float borderThickness;

    /**
     * Main paint. for drawing progress base, and progress bar.
     */
    private Paint mPaint;
    private RectF mRectF;

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
        mRectF = new RectF();

        /*
         * Default values.
         */
        this.primaryProgress = 0.5f;
        this.secondaryProgress = 0.75f;
        this.baseColor = Color.WHITE;
        this.primaryProgressColor = Color.RED;
        this.secondaryProgressColor = Color.YELLOW;
        this.borderColor = Color.TRANSPARENT;
        this.borderThickness = 0;

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
            this.borderColor = a.getColor(R.styleable.AnimatedProgressBar_oh_progress_bar_border_color, this.borderColor);
            this.borderThickness = a.getDimension(R.styleable.AnimatedProgressBar_oh_progress_bar_border_thickness, this.borderThickness);
            a.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();
        final float secondaryThickness = height * 0.85f;
        final float primaryThickness = height * 0.6f;
        final float primaryProgressXOffset = height * 0.15f;

        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);


        mPaint.setColor(this.baseColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(secondaryThickness);

        canvas.drawLine(secondaryThickness / 2, height / 2, width - secondaryThickness / 2, height / 2, mPaint);

        mPaint.setStrokeWidth(primaryThickness);
        mPaint.setColor(this.secondaryProgressColor);
        canvas.drawLine(primaryThickness / 2 + primaryProgressXOffset,
                height / 2,
                Math.max(0, getMeasuredWidth() * this.secondaryProgress - primaryThickness / 2 - 2 * primaryProgressXOffset),
                height / 2, mPaint);

        mPaint.setColor(this.primaryProgressColor);
        final float primaryLineWidth = Math.max(0, getMeasuredWidth() * this.primaryProgress - primaryThickness / 2 - primaryProgressXOffset);
        if (primaryLineWidth >= primaryThickness) {
            canvas.drawLine(primaryThickness / 2 + primaryProgressXOffset,
                    height / 2,
                    Math.max(0, primaryLineWidth),
                    height / 2, mPaint);
        }

        /* draw border if apply */
        if(this.borderThickness > 0) {
            mPaint.setColor(this.borderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(this.borderThickness);
            mRectF.set(borderThickness / 2,
                    borderThickness / 2,
                    width - borderThickness,
                    height - borderThickness);
            canvas.drawRoundRect(mRectF,
                    (height - this.borderThickness) / 2,
                    (height - this.borderThickness) / 2,
                    mPaint);
        }
    }

    public void setPrimaryProgress(float primaryProgress) {
        if (primaryProgress < 0) {
            primaryProgress = 0;
        } else if (primaryProgress > 1) {
            primaryProgress = 1;
        }
        this.primaryProgress = primaryProgress;
        invalidate();
    }

    public void setSecondaryProgress(float secondaryProgress) {
        if (secondaryProgress < 0) {
            secondaryProgress = 0;
        } else if (secondaryProgress > 1) {
            secondaryProgress = 1;
        }
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
