package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by EagleDiaoOptimity on 2017-06-27.
 */

public class StackLayout extends FrameLayout {

    public StackLayout(@NonNull Context context) {
        super(context);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int mMaxChildWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxChildWidth = 0;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                mMaxChildWidth = Math.max(getChildAt(i).getMeasuredWidth(), mMaxChildWidth);
            }
//            if (mMaxChildWidth > getMeasuredWidth() / getChildCount()) {
//                mMaxChildWidth = getMeasuredWidth() / getChildCount();
//            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final int childCount = getChildCount();

        if (childCount > 1) {
            final int width = right - left;
            final int height = bottom - top;
            final int offset = (width - mMaxChildWidth) / (childCount - 1);

            int xOffset = 0;
            for (int i = 0; i < childCount; i++) {
                final View v = getChildAt(i);
                final int childHeight = v.getMeasuredHeight();
                final int yOffset = (height - childHeight) / 2;
                v.layout(xOffset, yOffset, xOffset + mMaxChildWidth, childHeight);
                xOffset += offset;
            }
        } else {

        }
    }
}
