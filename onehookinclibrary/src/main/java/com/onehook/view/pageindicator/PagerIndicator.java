package com.onehook.view.pageindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-07-12.
 */

public class PagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    public PagerIndicator(Context context) {
        super(context);
        commonInit();
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private LinearLayout.LayoutParams obtainLayoutParams() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            lp.gravity = Gravity.CENTER_VERTICAL;
        } else {
            lp.gravity = Gravity.CENTER_HORIZONTAL;
        }
        return lp;
    }

    private void commonInit() {
        if (isInEditMode()) {
            for (int i = 0; i < 3; i++) {
                final View view = new View(getContext());
                view.setBackgroundResource(R.drawable.ic_pager_indicator_default_light);
                view.setLayoutParams(obtainLayoutParams());
                addView(view);
            }
        } else {

        }
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
            if (viewPager.getAdapter() == null) {
                throw new RuntimeException("Set View pager's adapter before you call setViewPager");
            }
            final int itemCount = viewPager.getAdapter().getCount();
            for (int i = 0; i < itemCount; i++) {
                final View view = new View(getContext());
                view.setBackgroundResource(R.drawable.ic_pager_indicator_default_light);
                view.setLayoutParams(obtainLayoutParams());
                addView(view);
            }
        }
    }


    public void removeViewPager(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
