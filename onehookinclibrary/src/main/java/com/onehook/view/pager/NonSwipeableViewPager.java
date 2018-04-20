package com.onehook.view.pager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by EagleDiao on 2016-02-08.
 */
public class NonSwipeableViewPager extends ViewPager {

    private boolean mNoInteraction = true;

    public NonSwipeableViewPager(Context context) {
        super(context);
        commonInit(context, null);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    private void commonInit(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        mNoInteraction = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(mNoInteraction) {
            return false;
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mNoInteraction) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    public void setNoUserInteraction(final boolean noUserInteraction) {
        mNoInteraction = noUserInteraction;
    }
}
