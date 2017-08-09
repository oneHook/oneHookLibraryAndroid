package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.onehookinc.androidlib.R;

/**
 * Created by Eagle Diao on 2017-08-04.
 */
public class ScaleView extends View {

    /**
     * Constants.
     */
    private static final int DEFAULT_SCALE_INTERVAL_IN_DP = 18;
    private static final int DEFAULT_SCALE_INTERVAL_COUNT = 10;
    private static final int DEFAULT_MIN_SCALE = 0;
    private static final int DEFAULT_MAX_SCALE = 500;
    private static final int DEFAULT_CURRENT_SCALE = 100;

    private static final int DEFAULT_THICK_LINE_THICKNESS_DP = 8;
    private static final int DEFAULT_THIN_LINE_THICKNESS_DP = 4;

    /**
     * @param context context
     */
    public ScaleView(Context context) {
        super(context);
        commonInit(context, null);
    }

    /**
     * @param context context
     * @param attrs   attributes
     */
    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    /**
     * @param context      context
     * @param attrs        attributes
     * @param defStyleAttr defined style attr
     */
    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    /**
     * @param context      context
     * @param attrs        attributes
     * @param defStyleAttr def style attr
     * @param defStyleRes  define style res
     */
    @TargetApi(21)
    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    public interface ScaleViewListener {

        /**
         * called when scale moved.
         *
         * @param currentScale current scale
         */
        void onScaleMoved(final ScaleView scaleView, int currentScale);
    }

    /*
     * View Parameters.
     */
    private int mMinimumScale;
    private int mMaximumScale;
    private int mScaleIntervalInPixel;
    private int mScaleIntervalCount;

    @ColorInt
    private int mLineColor;

    @ColorInt
    private int mIndicatorColor;
    private float mThickBarThickness;
    private float mThinBarThickness;

    private boolean mIsNaturalPanning;

    /*
     * Touch event handling.
     */
    private float mStartX;
    private float mPreviousX;
    private int mRawTranslationX;
    private int mCurrentScale;

    /*
     * Paints.
     */
    private Paint mDotPaint;
    private Paint mThinLinePaint;
    private Paint mThickLinePaint;

    /**
     * Listener.
     */
    private ScaleViewListener mListener;

    /**
     * @param context context
     * @param attrs   attribute sets
     */
    private void commonInit(final Context context, @Nullable final AttributeSet attrs) {
        setWillNotDraw(false);
        mMinimumScale = DEFAULT_MIN_SCALE;
        mMaximumScale = DEFAULT_MAX_SCALE;
        mScaleIntervalInPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_SCALE_INTERVAL_IN_DP,
                context.getResources().getDisplayMetrics());
        mScaleIntervalCount = DEFAULT_SCALE_INTERVAL_COUNT;
        mCurrentScale = DEFAULT_CURRENT_SCALE;
        mIsNaturalPanning = false;

        /* Setup default color and line thickness */
        mLineColor = Color.BLACK;
        mIndicatorColor = Color.RED;
        mThickBarThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_THICK_LINE_THICKNESS_DP,
                context.getResources().getDisplayMetrics());
        mThinBarThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_THIN_LINE_THICKNESS_DP,
                context.getResources().getDisplayMetrics());

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
            mMinimumScale = a.getInteger(R.styleable.ScaleView_oh_scale_view_minimum_scale, mMinimumScale);
            mMaximumScale = a.getInteger(R.styleable.ScaleView_oh_scale_view_maximum_scale, mMaximumScale);
            mScaleIntervalCount = a.getInteger(R.styleable.ScaleView_oh_scale_view_scale_interval_count, mScaleIntervalCount);
            mScaleIntervalInPixel = a.getInteger(R.styleable.ScaleView_oh_scale_view_scale_interval_length, mScaleIntervalInPixel);
            mCurrentScale = a.getInteger(R.styleable.ScaleView_oh_scale_view_current_scale, mCurrentScale);

            mLineColor = a.getColor(R.styleable.ScaleView_oh_scale_view_scale_color, mLineColor);
            mIndicatorColor = a.getColor(R.styleable.ScaleView_oh_scale_view_indicator_color, mIndicatorColor);
            mThickBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thick_bar_thickness, mThickBarThickness);
            mThinBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thin_bar_thickness, mThinBarThickness);

            mIsNaturalPanning = a.getBoolean(R.styleable.ScaleView_oh_scale_view_pan_direction_natural, mIsNaturalPanning);
        }

        /* Setup all paints */

        mThickLinePaint = new Paint();
        mThickLinePaint.setStrokeWidth(mThickBarThickness);
        mThickLinePaint.setColor(mLineColor);

        mThinLinePaint = new Paint();
        mThinLinePaint.setStrokeWidth(mThinBarThickness);
        mThinLinePaint.setColor(mLineColor);

        mDotPaint = new Paint();
        mDotPaint.setColor(mIndicatorColor);
        mDotPaint.setStrokeWidth(20);
        mDotPaint.setStrokeCap(Paint.Cap.ROUND);

        /* setup Internal */
        mRawTranslationX = mCurrentScale * mScaleIntervalInPixel;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                mStartX = event.getX();
                mPreviousX = mStartX;
                return true;
            case (MotionEvent.ACTION_MOVE):
                final float yOffset = event.getX() - mPreviousX;
                mPreviousX = event.getX();

                /* modify raw translation based on panning direction, YES, natural panning
                   introduced by apple. I dont get it, it doesn't feel natural to me.
                 */
                mRawTranslationX += mIsNaturalPanning ? yOffset : -yOffset;
                enforceLimit();
                invalidateCurrentScale();
                invalidate();
                return true;
            case (MotionEvent.ACTION_UP):
            case (MotionEvent.ACTION_CANCEL):
            case (MotionEvent.ACTION_OUTSIDE):
                mStartX = -1;
                snap();
                invalidateCurrentScale();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * Enforce the limit.
     */
    private void enforceLimit() {
        if (mRawTranslationX < mMinimumScale * mScaleIntervalInPixel) {
            mRawTranslationX = mMinimumScale * mScaleIntervalInPixel;
        } else if (mRawTranslationX > mMaximumScale * mScaleIntervalInPixel) {
            mRawTranslationX = mMaximumScale * mScaleIntervalInPixel;
        }
    }

    /**
     * Invalidate current scale based on raw translation X.
     */
    private void invalidateCurrentScale() {
        mCurrentScale = Math.round(mRawTranslationX * 1.0f / mScaleIntervalInPixel);
        if (mListener != null) {
            mListener.onScaleMoved(this, mCurrentScale);
        }
    }

    /**
     * Snap to the closet scale.
     */
    private void snap() {
        mRawTranslationX = Math.round(mRawTranslationX * 1.0f / mScaleIntervalInPixel) * mScaleIntervalInPixel;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        final int centerX = width / 2 - mRawTranslationX % (mScaleIntervalInPixel * mScaleIntervalCount);
        final int paddingTop = getPaddingTop();

        /* draw indicator */
        canvas.drawLine(width / 2,
                height - 10,
                width / 2,
                height,
                mDotPaint
        );

        /* draw center line */
        canvas.drawLine(centerX,
                paddingTop,
                centerX,
                height,
                mThickLinePaint);

        /* left lines */
        for (int i = 1; centerX + i * mScaleIntervalInPixel <= width; i++) {
            final Paint p = (i % mScaleIntervalCount == 0) ? mThickLinePaint : mThinLinePaint;
            final int yOffset = (i % mScaleIntervalCount == 0) ? 0 : (height / 4);
            canvas.drawLine(centerX + i * mScaleIntervalInPixel,
                    paddingTop + yOffset,
                    centerX + i * mScaleIntervalInPixel,
                    height,
                    p);
        }

        /* right lines */
        for (int i = 1; centerX - i * mScaleIntervalInPixel >= 0; i++) {
            final Paint p = (i % mScaleIntervalCount == 0) ? mThickLinePaint : mThinLinePaint;
            final int yOffset = (i % mScaleIntervalCount == 0) ? 0 : height / 4;
            canvas.drawLine(centerX - i * mScaleIntervalInPixel,
                    paddingTop + yOffset,
                    centerX - i * mScaleIntervalInPixel,
                    height,
                    p);
        }
    }

    /**
     * Set listener.
     *
     * @param listener
     */
    public void setListener(final ScaleViewListener listener) {
        mListener = listener;
    }

    /**
     * Set minimum scale. Make sure to call invalidate() after.
     *
     * @param minimumScale
     */
    public void setMinimumScale(int minimumScale) {
        mMinimumScale = minimumScale;
    }

    /**
     * Set maximum scale. Make sure to call invalidate() after.
     *
     * @param maximumScale
     */
    public void setMaximumScale(int maximumScale) {
        mMaximumScale = maximumScale;
    }

    /**
     * Set scale interval count. Make sure to call invalidate() after.
     *
     * @param scaleIntervalCount
     */
    public void setScaleIntervalCount(int scaleIntervalCount) {
        mScaleIntervalCount = scaleIntervalCount;
    }

    /**
     * Set line color. Make sure to call invalidate() after.
     *
     * @param lineColor
     */
    public void setLineColor(@ColorInt int lineColor) {
        mLineColor = lineColor;
        mThickLinePaint.setColor(mLineColor);
        mThinLinePaint.setColor(mLineColor);
    }

    /**
     * Set indicator scale. Make sure to call invalidate() after.
     *
     * @param indicatorColor
     */
    public void setIndicatorColor(@ColorInt int indicatorColor) {
        mIndicatorColor = indicatorColor;
        mDotPaint.setColor(mIndicatorColor);
    }

    /**
     * Set current scale. Make sure to call invalidate() after.
     *
     * @param currentScale
     */
    public void setCurrentScale(int currentScale) {
        mCurrentScale = currentScale;
        mRawTranslationX = mCurrentScale * mScaleIntervalInPixel;
        enforceLimit();
    }
}
