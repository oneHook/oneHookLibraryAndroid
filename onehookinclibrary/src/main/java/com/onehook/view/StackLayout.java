package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiaoOptimity on 2017-06-27.
 */
public class StackLayout extends FrameLayout {

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public enum Alignment {
        LEFT_OR_TOP,
        CENTER,
        RIGHT_OR_BOTTOM
    }

    public StackLayout(@NonNull Context context) {
        super(context);
        commonInit(context, null);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    /**
     * Orientation. VERTICAL or HORIZONTAL.
     */
    private Orientation mOrientation = Orientation.HORIZONTAL;

    /**
     * View Alignment.
     */
    private Alignment mAlignment = Alignment.CENTER;

    /**
     * If all the children view should have the same size as the maximum sized child.
     */
    private boolean mEqualToMax = true;

    private int mMaxChildWidth;

    /**
     * Common init with nullable attributes.
     *
     * @param context context
     * @param attrs   attributes
     */
    private void commonInit(final Context context, @Nullable final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StackLayout);
            final int orientation = a.getInt(R.styleable.StackLayout_orientation, 0);
            switch (orientation) {
                case 1:
                    mOrientation = Orientation.VERTICAL;
                    break;
                default:
                    mOrientation = Orientation.HORIZONTAL;
                    break;
            }
            final int alignment = a.getInt(R.styleable.StackLayout_alignment, 1);
            switch (alignment) {
                case 0:
                    mAlignment = Alignment.LEFT_OR_TOP;
                    break;
                case 2:
                    mAlignment = Alignment.RIGHT_OR_BOTTOM;
                    break;
                default:
                    mAlignment = Alignment.CENTER;
                    break;
            }
            mEqualToMax = a.getBoolean(R.styleable.StackLayout_equalToMax, true);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            if (mOrientation == Orientation.HORIZONTAL) {
                measureHorizontally();
            } else {
                measureVertically();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int childCount = getChildCount();
        if (childCount > 1) {
            final int width = right - left;
            final int height = bottom - top;
            if (mOrientation == Orientation.HORIZONTAL) {
                layoutHorizontally(width, height);
            } else {
                layoutVertically(width, height);
            }
        }
    }

    private void measureHorizontally() {
        mMaxChildWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            mMaxChildWidth = Math.max(getChildAt(i).getMeasuredWidth(), mMaxChildWidth);
        }
    }

    private void layoutHorizontally(final int width, final int height) {
        final int childCount = getChildCount();

        final int offset = (width - mMaxChildWidth) / (childCount - 1);
        int xOffset = 0;
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);

            final int childHeight = v.getMeasuredHeight();

            /* find top starting point */
            final int yOffset;
            switch (mAlignment) {
                case LEFT_OR_TOP:
                    yOffset = 0;
                    break;
                case RIGHT_OR_BOTTOM:
                    yOffset = height - childHeight;
                    break;
                case CENTER:
                default:
                    yOffset = (height - childHeight) / 2;
                    break;
            }


            v.layout(xOffset, yOffset, xOffset + mMaxChildWidth, yOffset + childHeight);
            xOffset += offset;
        }
    }

    private void measureVertically() {

    }

    private void layoutVertically(final int width, final int height) {

    }
}
