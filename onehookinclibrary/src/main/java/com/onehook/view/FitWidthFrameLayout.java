package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by EagleDiao on 2016-10-18.
 */

public class FitWidthFrameLayout extends FrameLayout {

    public FitWidthFrameLayout(Context context) {
        super(context);
    }

    public FitWidthFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitWidthFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public FitWidthFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }
}
