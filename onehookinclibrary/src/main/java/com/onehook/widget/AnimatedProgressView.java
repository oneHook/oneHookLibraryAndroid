package com.onehook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.onehookinc.androidlib.R;

/**
 * Created by odaeagle on 15-06-08.
 */
public class AnimatedProgressView extends View {

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
     * Progress bar base color (drawn underneath actual progress).
     */
    private int mProgressBaseColor;

    /**
     * Progress color.
     */
    private int mProgressColor;

    /**
     * Progress Text color.
     */
    private int mProgressTextColor;

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

    public AnimatedProgressView(Context context) {
        super(context);
        commonInit(context, null);
    }

    public AnimatedProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public AnimatedProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, AttributeSet attrs) {
        /*
         * Default value.
         */
        this.progress = 0.5f;
        mProgressBaseColor = Color.RED;
        mProgressColor = Color.GREEN;
        mProgressTextColor = Color.BLUE;

        /*
         * Load from style if any.
         */
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatedProgressView);
            mProgressBaseColor = a.getColor(R.styleable.AnimatedProgressView_vs_progress_base_color, mProgressBaseColor);
            mProgressColor = a.getColor(R.styleable.AnimatedProgressView_vs_progress_color, mProgressColor);
            mProgressTextColor = a.getColor(R.styleable.AnimatedProgressView_vs_progress_text_color, mProgressTextColor);
            this.progress = a.getFloat(R.styleable.AnimatedProgressView_vs_progress, this.progress);
            a.recycle();
        }

        /*
         * setup all needed variables.
         */
        mPaint = new Paint();
        mPaint.setColor(mProgressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mProgressRect = new RectF();
        mTextPaint = new Paint();
        Typeface textFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(textFont);
        mTextPaint.setColor(mProgressTextColor);
        mTextRect = new Rect();

        /*
         * CUSTOM DRAWING!!
         */
        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final float length = Math.min(w, h);
        final float strokeWidth = length / 24;
        mRadius = length / 2 - strokeWidth;
        final float textSize = mRadius / 2;

        mPaint.setStrokeWidth(strokeWidth);
        mProgressRect.left = w / 2 - mRadius;
        mProgressRect.top = h / 2 - mRadius;
        mProgressRect.right = w / 2 + mRadius;
        mProgressRect.bottom = h / 2 + mRadius;
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        mPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius, mPaint);

        /* Draw base first */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mProgressBaseColor);
        canvas.drawArc(mProgressRect, -90, 360 * this.bottomRingProgress, false, mPaint);

        /* draw actual progress bar */
        mPaint.setColor(mProgressColor);
        canvas.drawArc(mProgressRect, -90, 360 * this.progress, false, mPaint);

        /* generate progress text and measure it */
        final String progressText = String.format("%d%%", (int) (this.progress * 100));
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), mTextRect);
        canvas.drawText(progressText, getMeasuredWidth() / 2 - (mTextRect.right - mTextRect.left) / 2,
                getMeasuredHeight() / 2 + (mTextRect.bottom - mTextRect.top) / 2,
                mTextPaint);
    }

    public void setProgress(final float progress) {
        this.progress = progress;
        invalidate();
    }

    public void setBottomRingProgress(final float progress) {
        this.bottomRingProgress = progress;
        postInvalidate();
    }

    public void setProgressBaseColor(int progressBaseColor) {
        mProgressBaseColor = progressBaseColor;
        postInvalidate();
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
        invalidate();
    }

    public void setProgressTextColor(int progressTextColor) {
        mProgressTextColor = progressTextColor;
        invalidate();
    }
}
