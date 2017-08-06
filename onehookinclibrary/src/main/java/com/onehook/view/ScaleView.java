package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
    private static final int DEFAULT_NUM_SEGMENTS = 3;
    private static final int DEFAULT_LEN_SEGMENTS = 10;

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
     * @param attrs        atributes
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
         * called when scale moved. 1 means 1 unit (10 units is one big one..)
         *
         * @param offset offset
         */
        void onScaleMoved(final ScaleView scaleView, int offset);
    }

    /*
     * View Parameters.
     */
    private int mMinimumOffset = Integer.MIN_VALUE / 500;
    private int mMaximumOffset = Integer.MAX_VALUE / 500;
    private int mNumSegments;
    private int mSegmentLength;
    private int mLineColor;
    private int mIndicatorColor;
    private float mThickBarThickness;
    private float mThinBarThickness;


    /*
     * Touch event handling.
     */
    private float mStartX;
    private float mPreviousX;
    private int mUnitSpacing;
    private int mCurrentOffset;

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
        mCurrentOffset = 0;
        mNumSegments = DEFAULT_NUM_SEGMENTS;
        mSegmentLength = DEFAULT_LEN_SEGMENTS;
        mLineColor = Color.BLACK;
        mIndicatorColor = Color.RED;
        mThickBarThickness = 8;
        mThinBarThickness = 4;

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
            mMinimumOffset = a.getInteger(R.styleable.ScaleView_oh_scale_view_minimum_offset, mMinimumOffset);
            mMaximumOffset = a.getInteger(R.styleable.ScaleView_oh_scale_view_maximum_offset, mMaximumOffset);
            mNumSegments = a.getInteger(R.styleable.ScaleView_oh_scale_view_segment_count, mNumSegments);
            mSegmentLength = a.getInteger(R.styleable.ScaleView_oh_scale_view_segment_length, mSegmentLength);
            mLineColor = a.getColor(R.styleable.ScaleView_oh_scale_view_scale_color, mLineColor);
            mIndicatorColor = a.getColor(R.styleable.ScaleView_oh_scale_view_indicator_color, mIndicatorColor);
            mThickBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thick_bar_thickness, mThickBarThickness);
            mThinBarThickness = a.getDimension(R.styleable.ScaleView_oh_scale_view_thin_bar_thickness, mThinBarThickness);
        }

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
                mCurrentOffset += yOffset;
                if (mCurrentOffset < mMinimumOffset * mUnitSpacing) {
                    mCurrentOffset = mMinimumOffset * mUnitSpacing;
                } else if (mCurrentOffset > mMaximumOffset * mUnitSpacing) {
                    mCurrentOffset = mMaximumOffset * mUnitSpacing;
                }
                dispatchListener();
                invalidate();
                return true;
            case (MotionEvent.ACTION_UP):
            case (MotionEvent.ACTION_CANCEL):
            case (MotionEvent.ACTION_OUTSIDE):
                mStartX = -1;
                snap();
                dispatchListener();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    /**
     * Dispatch to listener.
     */
    private void dispatchListener() {
        final int newOffset = Math.round(mCurrentOffset * 1.0f / mUnitSpacing);
        System.out.println("oneHook current offset " + mCurrentOffset + " min offset " + (mMinimumOffset * mUnitSpacing) + " max offset " + mMaximumOffset);
        if (mListener != null) {
            mListener.onScaleMoved(this, newOffset);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getMeasuredWidth();
        mUnitSpacing = (width / mNumSegments) / mSegmentLength;
    }

    /**
     * Snap to the closet scale.
     */
    private void snap() {
        final int newOffset = Math.round(mCurrentOffset * 1.0f / mUnitSpacing) * mUnitSpacing;
        mCurrentOffset = newOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        final int interval = width / mNumSegments;
        final int yOffset = mCurrentOffset % interval;
        final int paddingTop = getPaddingTop();

        canvas.drawLine(width / 2,
                height - 10,
                width / 2,
                height,
                mDotPaint
        );

        canvas.drawLine(width / 2 - yOffset,
                paddingTop,
                width / 2 - yOffset,
                height,
                mThickLinePaint);

        for (int i = 1; i < mSegmentLength * mNumSegments; i++) {
            if (i % mSegmentLength == 0) {
                /* thick line */
                canvas.drawLine(width / 2 - i * mUnitSpacing - yOffset,
                        paddingTop,
                        width / 2 - i * mUnitSpacing - yOffset,
                        height,
                        mThickLinePaint);

                canvas.drawLine(width / 2 + i * mUnitSpacing - yOffset,
                        paddingTop,
                        width / 2 + i * mUnitSpacing - yOffset,
                        height,
                        mThickLinePaint);
            } else {
                /* thin line */
                canvas.drawLine(width / 2 - i * mUnitSpacing - yOffset,
                        paddingTop + (height - paddingTop) * 2 / 3,
                        width / 2 - i * mUnitSpacing - yOffset,
                        height,
                        mThinLinePaint);

                canvas.drawLine(width / 2 + i * mUnitSpacing - yOffset,
                        paddingTop + (height - paddingTop) * 2 / 3,
                        width / 2 + i * mUnitSpacing - yOffset,
                        height,
                        mThinLinePaint);
            }
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
     * Snap to offset. Each 1 means one unit on scale.
     *
     * @param offset offset
     */
    public void snapToOffset(final int offset) {
        final int newOffset = offset * mUnitSpacing;
        mCurrentOffset = newOffset;
        invalidate();
    }

    public void setMinimumOffset(final int minOffset) {
        mMinimumOffset = minOffset;
    }

    public void setMaximumOffset(final int maxOffset) {
        mMaximumOffset = maxOffset;
    }

    /**
     * This call only works if called before measuring.
     *
     * @param numSeg
     */
    public void setNumSegments(final int numSeg) {
        mNumSegments = numSeg;
    }

    /**
     * This call only works if called before measuring.
     *
     * @param segmentLength
     */
    public void setSegmentLength(final int segmentLength) {
        mSegmentLength = segmentLength;
    }
}
