package com.onehook.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-10-18.
 */

public class FitRatioFrameLayout extends FrameLayout {

    public FitRatioFrameLayout(Context context) {
        super(context);
        commonInit(context, null);
    }

    public FitRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public FitRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public FitRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    public static final int FIT_NONE = 2;
    public static final int FIT_WIDTH = 0;
    public static final int FIT_HEIGHT = 1;

    private int mWidthWeight = 1;
    private int mHeightWeight = 1;
    private int mFitTo = FIT_NONE;

    private void commonInit(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FitRatioFrameLayout);
            mWidthWeight = a.getInt(R.styleable.FitRatioFrameLayout_width_weight, mWidthWeight);
            mHeightWeight = a.getInt(R.styleable.FitRatioFrameLayout_height_weight, mHeightWeight);
            mFitTo = a.getInt(R.styleable.FitRatioFrameLayout_fit_to, mFitTo);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        if (mFitTo == FIT_WIDTH) {
            final float height = 1.0f * getMeasuredWidth() / mWidthWeight * mHeightWeight;
            setMeasuredDimension(getMeasuredWidth(), (int) height);
        } else if (mFitTo == FIT_HEIGHT) {
            final float width = 1.0f * getMeasuredHeight() / mHeightWeight * mWidthWeight;
            setMeasuredDimension((int) width, getMeasuredHeight());
        }
    }
}
