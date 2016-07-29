package com.onehook.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.onehookinc.androidlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eaglediaotomore on 2016-07-22.
 */

public class OHFlowLayout extends ViewGroup {

    public enum AlignType {
        TOP,
        BOTTOM,
        CENTER
    }

    private AlignType mAlignType;

    private int mHorizontalSpacing;

    private int mVerticalSpacing;

    private int mMaximumWidth;

    public OHFlowLayout(Context context) {
        super(context);
        commonInit(context, null);
    }

    public OHFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public OHFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public OHFlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    private void commonInit(final Context context, final AttributeSet attrs) {
        mAlignType = AlignType.CENTER;
        mHorizontalSpacing = 24;
        mVerticalSpacing = 24;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OHFlowLayout);
            mHorizontalSpacing = (int) a.getDimension(R.styleable.OHFlowLayout_oh_flow_layout_horizontal_margin, mHorizontalSpacing);
            mVerticalSpacing = (int) a.getDimension(R.styleable.OHFlowLayout_oh_flow_layout_vertical_margin, mVerticalSpacing);
            final int type = a.getInt(R.styleable.OHFlowLayout_oh_flow_layout_align_type, 0);
            switch (type) {
                case 0:
                    mAlignType = AlignType.CENTER;
                    break;
                case 1:
                    mAlignType = AlignType.BOTTOM;
                    break;
                default:
                    mAlignType = AlignType.TOP;
                    break;
            }
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED));
        }
        mMaximumWidth = MeasureSpec.getSize(widthMeasureSpec);
        calculateLayout();

    }

    private void calculateLayout() {
        final int childCount = getChildCount();
        float currentLineX = 0;
        float currentLineY = 0;
        float currentLineMaxHeight = 0;
        ArrayList<View> currentLineChildren = new ArrayList<>();

        int i = 0;
        while (i < childCount) {
            final View child = getChildAt(i);
            final float childWidth = child.getMeasuredWidth();
            final float childHeight = child.getMeasuredHeight();
            if (currentLineX + mHorizontalSpacing + childWidth < mMaximumWidth || currentLineChildren.size() == 0) {
                /* still in the same line */
                currentLineMaxHeight = Math.max(currentLineMaxHeight, childHeight);
                currentLineChildren.add(child);
                currentLineX += childWidth + mHorizontalSpacing;
                i++;
            } else {
                /* assign rect for each child */
                assignLayoutForLine(currentLineChildren, currentLineY, currentLineMaxHeight);
                /* should go into a new line */
                currentLineChildren.clear();
                currentLineY += currentLineMaxHeight + mVerticalSpacing;
                currentLineMaxHeight = 0;
                currentLineX = 0;
            }
        }

        if (currentLineChildren.size() > 0) {
            /* one more line */
            assignLayoutForLine(currentLineChildren, currentLineY, currentLineMaxHeight);
            currentLineChildren.clear();
            currentLineY += currentLineMaxHeight;

        } else {
            currentLineY -= mVerticalSpacing;
        }
        setMeasuredDimension(mMaximumWidth, (int) currentLineY);
    }

    private void assignLayoutForLine(final List<View> children, final float yStart, final float maxHeight) {
        float xStart = 0;
        for (int i = 0; i < children.size(); i++) {
            final View child = children.get(i);
            final float childWidth = child.getMeasuredWidth();
            final float childHeight = child.getMeasuredHeight();

            switch (mAlignType) {
                case CENTER:
                    child.setTag(R.id.view_layout_info_tag_key, new RectF(xStart,
                            yStart + (maxHeight - childHeight) / 2,
                            xStart + childWidth,
                            yStart + (maxHeight - childHeight) / 2 + childHeight
                    ));
                    break;
                case BOTTOM:
                    child.setTag(R.id.view_layout_info_tag_key, new RectF(xStart,
                            yStart + maxHeight - childHeight,
                            xStart + childWidth,
                            yStart + maxHeight));
                    break;
                case TOP:
                    child.setTag(R.id.view_layout_info_tag_key, new RectF(xStart,
                            yStart,
                            xStart + childWidth,
                            yStart + childHeight));
                    break;

            }
            xStart += childWidth + mHorizontalSpacing;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final RectF rect = (RectF) child.getTag(R.id.view_layout_info_tag_key);
            if (rect != null) {
                child.layout((int) rect.left, (int) rect.top, (int) rect.right, (int) rect.bottom);
            }
        }
    }
}
