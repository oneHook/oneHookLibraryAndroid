package com.onehook.view.progress;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import com.onehookinc.androidlib.R;

/**
 * Created by odaeagle on 15-06-08.
 */
public class ProgressRing extends View {

    /**
     * Bottom ring progress, from 0 to 1. By default it will always be 1. This value can be
     * animated by property animator.
     */
    private float bottomRingProgress;

    /**
     * Current progress, from 0 to 1. This value can be
     * animated by property animator.
     */
    private float progress;

    /**
     * Progress bar base colorRes (drawn underneath actual progress).
     */
    private int progressBaseColor = Color.RED;

    /**
     * Progress colorRes.
     */
    private int progressColor = Color.GREEN;

    /**
     * Progress Text colorRes.
     */
    private int mProgressTextColor = Color.BLACK;

    /**
     * Circle background colorRes.
     */
    private int mCircleBackgroundColor = Color.WHITE;

    /**
     * Ratio to determine how thick is the ring.
     */
    private float mProgressRingStrokeRatio;

    /**
     * RectF for drawing progress bar(ARC).
     */
    private RectF mProgressRect;

    /**
     * Rect for measuring text( so we can center the text).
     */
    private Rect mTextRect;

    /**
     * Main paint. for drawing progress base, and progress bar.
     */
    private Paint mPaint;

    /**
     * Text Paint.
     */
    private Paint mTextPaint;

    /**
     * Progress radius.
     */
    private float mRadius;

    /**
     * Whether or not render center percentage.
     */
    private boolean mShouldShowPercentage;

    public ProgressRing(Context context) {
        super(context);
        commonInit(context, null);
    }

    public ProgressRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public ProgressRing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, AttributeSet attrs) {

        /*
         * CUSTOM DRAWING!!
         */
        setWillNotDraw(false);
        /*
         * Default value.
         */
        this.progress = 0.5f;
        this.bottomRingProgress = 1.0f;
        mProgressRingStrokeRatio = 0.05f;
        progressBaseColor = Color.RED;
        progressColor = Color.GREEN;
        mProgressTextColor = Color.BLUE;
        mShouldShowPercentage = true;

        /*
         * Load from style if any.
         */
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgressRing);
            progressBaseColor = a.getColor(R.styleable.ProgressRing_oh_progress_view_base_color, progressBaseColor);
            progressColor = a.getColor(R.styleable.ProgressRing_oh_progress_view_primary_color, progressColor);
            mProgressTextColor = a.getColor(R.styleable.ProgressRing_oh_progress_view_text_color, mProgressTextColor);
            this.progress = a.getFloat(R.styleable.ProgressRing_oh_progress_view_progress, this.progress);
            this.bottomRingProgress = a.getFloat(R.styleable.ProgressRing_oh_progress_view_bottom_ring_progress, this.bottomRingProgress);
            mShouldShowPercentage = a.getBoolean(R.styleable.ProgressRing_oh_progress_view_show_percentage, mShouldShowPercentage);
            mProgressRingStrokeRatio = a.getFloat(R.styleable.ProgressRing_oh_progress_view_stroke_ratio, mProgressRingStrokeRatio);
            mCircleBackgroundColor = a.getColor(R.styleable.ProgressRing_oh_progress_view_circle_background_color, mCircleBackgroundColor);
            a.recycle();
        }

        /*
         * setup all needed variables.
         */
        mPaint = new Paint();
        mPaint.setColor(progressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mProgressRect = new RectF();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mProgressTextColor);
        mTextRect = new Rect();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final float length = Math.min(w, h);
        final float strokeWidth = length * mProgressRingStrokeRatio;
        mRadius = length / 2 - strokeWidth;
        final float textSize = mRadius / 2;

        mPaint.setStrokeWidth(strokeWidth);
        mProgressRect.left = w / 2 - mRadius - strokeWidth / 2;
        mProgressRect.top = h / 2 - mRadius - strokeWidth / 2;
        mProgressRect.right = w / 2 + mRadius + strokeWidth / 2;
        mProgressRect.bottom = h / 2 + mRadius + strokeWidth / 2;
        mTextPaint.setTextSize(textSize);

        invalidate();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mCircleBackgroundColor);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);

        /* Draw base first */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(progressBaseColor);
        canvas.drawArc(mProgressRect, -90, 360 * this.bottomRingProgress, false, mPaint);

        /* draw actual progress bar */
        mPaint.setColor(progressColor);
        canvas.drawArc(mProgressRect, -90, 360 * this.progress, false, mPaint);

        if (mShouldShowPercentage) {
            /* generate progress text and measure it */
            final String progressText = String.format("%d%%", (int) (this.progress * 100));
            mTextPaint.getTextBounds(progressText, 0, progressText.length(), mTextRect);
            canvas.drawText(progressText, getMeasuredWidth() / 2 - (mTextRect.right - mTextRect.left) / 2,
                    getMeasuredHeight() / 2 + (mTextRect.bottom - mTextRect.top) / 2,
                    mTextPaint);
        }
    }

    public void setProgress(final float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setBottomRingProgress(final float progress) {
        this.bottomRingProgress = progress;
        postInvalidate();
    }

    public void setProgressBaseColor(@ColorInt int progressBaseColor) {
        this.progressBaseColor = progressBaseColor;
        postInvalidate();
    }

    public void setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
        invalidate();
    }

    public float getBottomRingProgress() {
        return bottomRingProgress;
    }

    public float getProgress() {
        return progress;
    }

    /* Animation Handler */

    public ValueAnimator createProgressAnimation(final float toProgress,
                                                 final long duration) {
        return createProgressAnimation(this.progress, toProgress, duration);
    }

    public ValueAnimator createProgressAnimation(final float fromProgress,
                                                 final float toProgress,
                                                 final long duration) {
        return ObjectAnimator.ofFloat(this, "progress",
                fromProgress, toProgress).setDuration(duration);
    }

    public ValueAnimator createBottomProgressAnimation(final float toProgress,
                                                       final long duration) {
        return createBottomProgressAnimation(this.bottomRingProgress, toProgress, duration);
    }

    public ValueAnimator createBottomProgressAnimation(final float fromProgress,
                                                       final float toProgress,
                                                       final long duration) {
        return ObjectAnimator.ofFloat(this, "bottomRingProgress",
                fromProgress, toProgress).setDuration(duration);
    }

    public ValueAnimator createRingColorAnimation(@ColorInt int ringColor,
                                                  final long duration) {
        return ObjectAnimator.ofInt(this, "progressColor",
                this.progressColor, ringColor).setDuration(duration);
    }

    public ValueAnimator createBottomRingColorAnimation(@ColorInt int ringColor,
                                                        final long duration) {
        return ObjectAnimator.ofInt(this, "progressBaseColor",
                this.progressBaseColor, ringColor).setDuration(duration);
    }

}
