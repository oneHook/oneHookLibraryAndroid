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
 * Created by Eagle Diao on 2017-06-27.
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
    private boolean mMatchToMaxChild = false;

    /**
     * If we distribute to center or to bound.
     */
    private boolean mDistributeToEdge = true;

    /* for horizontal */

    private int mTotalChildrenWidth;
    private int mMaxChildWidth;

    /* for vertical */

    private int mTotalChildrenHeight;
    private int mMaxChildHeight;

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
            mMatchToMaxChild = a.getBoolean(R.styleable.StackLayout_equalToMax, mMatchToMaxChild);
            mDistributeToEdge = a.getBoolean(R.styleable.StackLayout_distributeToEdge, mDistributeToEdge);
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

    /* Horizontal handling */

    private void measureHorizontally() {
        mMaxChildWidth = 0;
        mTotalChildrenWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final int measuredWidth = getChildAt(i).getMeasuredWidth();
            mMaxChildWidth = Math.max(measuredWidth, mMaxChildWidth);
            mTotalChildrenWidth += measuredWidth;
        }
        if (mMatchToMaxChild) {
            mTotalChildrenWidth = mMaxChildWidth * getChildCount();
        }
    }

    /**
     * Layout Horizontally.
     *
     * @param width  view actual width (with padding included)
     * @param height view actual height (with padding included)
     */
    private void layoutHorizontally(final int width, final int height) {
        final int childCount = getChildCount();
        final int spacingCount;
        if (mDistributeToEdge) {
            spacingCount = childCount - 1;
        } else {
            spacingCount = childCount + 1;
        }
        final int spacing = (width - mTotalChildrenWidth - getPaddingLeft() - getPaddingRight()) / spacingCount;
        int xOffset = mDistributeToEdge ? getPaddingLeft() : getPaddingLeft() + spacing;
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);

            final int childWidth = mMatchToMaxChild ? mMaxChildWidth : v.getMeasuredWidth();
            final int childHeight = v.getMeasuredHeight();

            /* find top starting point */
            final int yOffset;
            switch (mAlignment) {
                case LEFT_OR_TOP:
                    yOffset = getPaddingTop();
                    break;
                case RIGHT_OR_BOTTOM:
                    yOffset = height - childHeight - getPaddingBottom();
                    break;
                case CENTER:
                default:
                    yOffset = (height - childHeight) / 2;
                    break;
            }

            v.layout(xOffset, yOffset, xOffset + childWidth, yOffset + childHeight);
            xOffset += childWidth + spacing;
        }
    }

    /* Vertical handling */

    private void measureVertically() {
        mMaxChildHeight = 0;
        mTotalChildrenHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final int measuredHeight = getChildAt(i).getMeasuredHeight();
            mMaxChildHeight = Math.max(measuredHeight, mMaxChildHeight);
            mTotalChildrenHeight += measuredHeight;
        }
        if (mMatchToMaxChild) {
            mTotalChildrenHeight = mMaxChildHeight * getChildCount();
        }
    }

    /**
     * Layout Vertically.
     *
     * @param width  view actual width (with padding included)
     * @param height view actual height (with padding included)
     */
    private void layoutVertically(final int width, final int height) {
        final int childCount = getChildCount();
        final int spacingCount;
        if (mDistributeToEdge) {
            spacingCount = childCount - 1;
        } else {
            spacingCount = childCount + 1;
        }
        final int spacing = (height - mTotalChildrenHeight - getPaddingTop() - getPaddingBottom()) / spacingCount;
        int yOffset = mDistributeToEdge ? getPaddingTop() : getPaddingTop() + spacing;
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);

            final int childWidth = v.getMeasuredWidth();
            final int childHeight = mMatchToMaxChild ? mMaxChildHeight : v.getMeasuredHeight();

            /* find top starting point */
            final int xOffset;
            switch (mAlignment) {
                case LEFT_OR_TOP:
                    xOffset = getPaddingLeft();
                    break;
                case RIGHT_OR_BOTTOM:
                    xOffset = width - childWidth - getPaddingRight();
                    break;
                case CENTER:
                default:
                    xOffset = (width - childWidth) / 2;
                    break;
            }

            v.layout(xOffset, yOffset, xOffset + childWidth, yOffset + childHeight);
            yOffset += childHeight + spacing;
        }
    }
}
