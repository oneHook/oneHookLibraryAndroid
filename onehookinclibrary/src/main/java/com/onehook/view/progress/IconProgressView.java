package com.onehook.view.progress;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-05-01.
 */
public class IconProgressView extends TextView {

    public IconProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public IconProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public IconProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    private String[] mIcons;

    private int mIndex;

    private void commonInit(final Context context, final AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconProgressView);
        final int resID = a.getResourceId(R.styleable.IconProgressView_oh_icon_progress_view_icons, 0);
        if (resID == 0) {
            throw new IllegalArgumentException(
                    "IntegerListPreference: error - entryList is not specified");
        }
        mIcons = context.getResources().getStringArray(resID);
        if (mIcons == null) {
            throw new IllegalArgumentException(
                    "IntegerListPreference: error - entryList is not specified");
        }
        mIndex = 0;
        a.recycle();

        setVisibility(getVisibility());
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            startAnimating();
        } else {
            endAnimating();
        }
    }

    private void startAnimating() {
        setText(mIcons[mIndex]);
        postDelayed(mRunnable, 200);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            removeCallbacks(mRunnable);
            mIndex = (mIndex + 1) % mIcons.length;
            startAnimating();
        }
    };

    private void endAnimating() {
        removeCallbacks(mRunnable);
    }
}
