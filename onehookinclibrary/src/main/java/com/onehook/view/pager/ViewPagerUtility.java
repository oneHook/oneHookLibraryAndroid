package com.onehook.view.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by EagleDiao on 2016-07-12.
 */

public class ViewPagerUtility {

    public static class CustomScroller extends Scroller {

        private long mDuration;

        public CustomScroller(Context context, Interpolator interpolator, long duration) {
            super(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) mDuration);
        }
    }

    public static void setViewPagerSmoothScrollDuration(final long duration, final ViewPager viewPager) {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(viewPager, new CustomScroller(viewPager.getContext(), new LinearInterpolator(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
