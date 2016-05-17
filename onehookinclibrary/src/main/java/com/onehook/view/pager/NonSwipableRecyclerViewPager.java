package com.onehook.view.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.onehook.widget.recyclerview.RecyclerViewPager;

/**
 * Created by EagleDiao on 2016-05-16.
 */
public class NonSwipableRecyclerViewPager extends RecyclerViewPager {

    public static boolean sDebugMode = false;

    public NonSwipableRecyclerViewPager(Context context) {
        super(context);
    }

    public NonSwipableRecyclerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(sDebugMode) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(sDebugMode) {
            return super.onTouchEvent(event);
        }
        return false;
    }


}
