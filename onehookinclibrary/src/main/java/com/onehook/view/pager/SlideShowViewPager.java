package com.onehook.view.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by eaglediaotomore on 2016-07-10.
 */

public class SlideShowViewPager extends NonSwipableViewPager {

    final long DELAY = 4000;

    final Runnable mToNextPageRunnable = new Runnable() {
        @Override
        public void run() {
            final int curr = getCurrentItem();
            if (curr < getAdapter().getCount() - 1) {
                setCurrentItem(getCurrentItem() + 1, true);
            } else {
                setCurrentItem(0);
            }
            postDelayed(this, DELAY);
        }
    };

    public SlideShowViewPager(Context context) {
        super(context);
        commonInit();
    }

    public SlideShowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    private void commonInit() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new CustomScroller(getContext(), new LinearInterpolator(), 800));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomScroller extends Scroller {

        private int mDuration;

        public CustomScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    public void startSlideShow() {
        removeCallbacks(mToNextPageRunnable);
        postDelayed(mToNextPageRunnable, DELAY);
    }

    public void endSlideShow() {
        removeCallbacks(mToNextPageRunnable);
    }

}
