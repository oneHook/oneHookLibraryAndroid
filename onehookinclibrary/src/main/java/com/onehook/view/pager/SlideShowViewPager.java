package com.onehook.view.pager;

import android.content.Context;
import android.util.AttributeSet;

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
        ViewPagerUtility.setViewPagerSmoothScrollDuration(800, this);
    }


    public void startSlideShow() {
        removeCallbacks(mToNextPageRunnable);
        postDelayed(mToNextPageRunnable, DELAY);
    }

    public void endSlideShow() {
        removeCallbacks(mToNextPageRunnable);
    }

}
