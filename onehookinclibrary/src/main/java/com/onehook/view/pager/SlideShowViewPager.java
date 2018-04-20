package com.onehook.view.pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.onehook.view.FlowLayout;
import com.onehookinc.androidlib.R;

/**
 * Created by Eagle Diao on 2016-07-10.
 */

public class SlideShowViewPager extends NonSwipeableViewPager {

    /**
     * Each page delay. By Default 4 seconds.
     */
    private int mDelay = 4000;

    /**
     * Animation duration to flip a page, by default 800 ms.
     */
    private int mSwitchPageAnimationDuration = 800;

    /**
     * Runnable to present next page.
     */
    final Runnable mToNextPageRunnable = new Runnable() {
        @Override
        public void run() {
            final int curr = getCurrentItem();
            if (curr < getAdapter().getCount() - 1) {
                setCurrentItem(getCurrentItem() + 1, true);
            } else {
                setCurrentItem(0);
            }
            postDelayed(this, mDelay);
        }
    };

    public SlideShowViewPager(Context context) {
        super(context);
        commonInit(context, null);
    }

    public SlideShowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    private void commonInit(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        ViewPagerUtility.setViewPagerSmoothScrollDuration(mSwitchPageAnimationDuration,
                this);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideShowViewPager);
            mDelay = a.getInt(R.styleable.SlideShowViewPager_page_stay_duration,
                    mDelay);
            mSwitchPageAnimationDuration = a.getInt(R.styleable.SlideShowViewPager_page_animation_duration,
                    mSwitchPageAnimationDuration);
            a.recycle();
        }
    }

    /**
     * Start the slide show.
     */
    public void startSlideShow() {
        removeCallbacks(mToNextPageRunnable);
        postDelayed(mToNextPageRunnable, mDelay);
    }

    /**
     * End the slide show.
     */
    public void endSlideShow() {
        removeCallbacks(mToNextPageRunnable);
    }

    public void setDelay(int delay) {
        this.mDelay = delay;
    }

    public void setSwitchPageAnimationDuration(int switchPageAnimationDuration) {
        this.mSwitchPageAnimationDuration = switchPageAnimationDuration;
    }
}
