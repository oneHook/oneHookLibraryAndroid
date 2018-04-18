package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    private static final int DEFAULT_INDICATOR_WIDTH_DP = 8;

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
    private int mMaximumLineHeight;
    private int mIndicatorWidth;

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
    private Paint mIndicatorPaint;
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
        mIndicatorWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DEFAULT_INDICATOR_WIDTH_DP,
                context.getResources().getDisplayMetrics());
        mScaleIntervalCount = DEFAULT_SCALE_INTERVAL_COUNT;
        mCurrentScale = DEFAULT_CURRENT_SCALE;
        mIsNaturalPanning = false;
        mMaximumLineHeight = Integer.MAX_VALUE;

        /* Setup default colorRes and line thickness */
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
            mScaleIntervalInPixel = (int) a.getDimension(R.styleable.ScaleView_oh_scale_view_scale_interval_length, mScaleIntervalInPixel);
            mCurrentScale = a.getInteger(R.styleable.ScaleView_oh_scale_view_current_scale, mCurrentScale);

            mLineColor = a.getColor(R.styleable.ScaleView_oh_scale_view_scale_color, mLineColor);
            mIndicatorColor = a.getColor(R.styleable.ScaleView_oh_scale_view_indicator_color, mIndicatorColor);
            mThickBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thick_bar_thickness, mThickBarThickness);
            mThinBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thin_bar_thickness, mThinBarThickness);
            mMaximumLineHeight = (int) a.getDimension(R.styleable.ScaleView_oh_scale_view_maximum_line_height, mMaximumLineHeight);
            mIndicatorWidth = (int) a.getDimension(R.styleable.ScaleView_oh_scale_view_indicator_width, mIndicatorWidth);

            mIsNaturalPanning = a.getBoolean(R.styleable.ScaleView_oh_scale_view_pan_direction_natural, mIsNaturalPanning);
        }

        /* Setup all paints */

        mThickLinePaint = new Paint();
        mThickLinePaint.setStrokeWidth(mThickBarThickness);
        mThickLinePaint.setColor(mLineColor);

        mThinLinePaint = new Paint();
        mThinLinePaint.setStrokeWidth(mThinBarThickness);
        mThinLinePaint.setColor(mLineColor);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setColor(mIndicatorColor);
        mIndicatorPaint.setStrokeWidth(20);
        mIndicatorPaint.setStrokeCap(Paint.Cap.ROUND);

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
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        final int centerX = width / 2 - mRawTranslationX % (mScaleIntervalInPixel * mScaleIntervalCount);

        final float lineHeight = Math.min(mMaximumLineHeight, height - paddingTop - paddingBottom);
        /* draw center line */
        canvas.drawLine(centerX,
                (height - lineHeight),
                centerX,
                height - paddingBottom,
                mThickLinePaint);

        /* left lines */
        for (int i = 1; centerX + i * mScaleIntervalInPixel <= width; i++) {
            final Paint p = (i % mScaleIntervalCount == 0) ? mThickLinePaint : mThinLinePaint;
            final int yOffset = (i % mScaleIntervalCount == 0) ? 0 : (int) (lineHeight * 0.4);
            canvas.drawLine(centerX + i * mScaleIntervalInPixel,
                    (height - lineHeight) + yOffset,
                    centerX + i * mScaleIntervalInPixel,
                    height - paddingBottom,
                    p);
        }

        /* right lines */
        for (int i = 1; centerX - i * mScaleIntervalInPixel >= 0; i++) {
            final Paint p = (i % mScaleIntervalCount == 0) ? mThickLinePaint : mThinLinePaint;
            final int yOffset = (i % mScaleIntervalCount == 0) ? 0 : (int) (lineHeight * 0.4);
            canvas.drawLine(centerX - i * mScaleIntervalInPixel,
                    (height - lineHeight) + yOffset,
                    centerX - i * mScaleIntervalInPixel,
                    height - paddingBottom,
                    p);
        }

        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setAntiAlias(true);

        final Path indicatorPath = new Path();
        indicatorPath.moveTo(width / 2, height - mIndicatorWidth);
        indicatorPath.lineTo(width / 2 - mIndicatorWidth / 2, height);
        indicatorPath.lineTo(width / 2 + mIndicatorWidth / 2, height);
        indicatorPath.close();

        canvas.drawPath(indicatorPath, mIndicatorPaint);
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
     * Set line colorRes. Make sure to call invalidate() after.
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
        mIndicatorPaint.setColor(mIndicatorColor);
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
