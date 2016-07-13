package com.onehook.view.pageindicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.onehook.util.color.ColorUtility;
import com.onehook.widget.adapter.InfinitePagerAdapter;
import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-07-12.
 */

public class OHPagerIndicator extends LinearLayout implements ViewPager.OnPageChangeListener {

    public OHPagerIndicator(Context context) {
        super(context);
        commonInit(context, null);
    }

    public OHPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public OHPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public OHPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    private View[] mDots;
    private DotDrawable[] mDotDrawables;
    private int mDefaultDotColor;
    private int mSelectedDotColor;
    private int mRealItemCount;

    private LinearLayout.LayoutParams obtainLayoutParams() {
        final int size = (int) getContext().getResources().getDimension(R.dimen.pager_indicator_default_length);
        final int margin = (int) getContext().getResources().getDimension(R.dimen.common_margin_xsmall);
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            lp.gravity = Gravity.CENTER_VERTICAL;
            lp.leftMargin = margin;
            lp.rightMargin = margin;
        } else {
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            lp.topMargin = margin;
            lp.bottomMargin = margin;
        }
        return lp;
    }

    private void commonInit(final Context context, final AttributeSet attributeSet) {
        mDefaultDotColor = Color.WHITE;
        mSelectedDotColor = Color.RED;
        if (attributeSet != null) {
            final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.OHPagerIndicator);
            mDefaultDotColor = a.getColor(R.styleable.OHPagerIndicator_oh_pager_indicator_default_color, Color.WHITE);
            mSelectedDotColor = a.getColor(R.styleable.OHPagerIndicator_oh_pager_indicator_selected_color, Color.RED);
        }
    }

    public void setViewPager(final ViewPager viewPager) {
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
            if (viewPager.getAdapter() == null) {
                throw new RuntimeException("Set View pager's adapter before you call setViewPager");
            }
            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter instanceof InfinitePagerAdapter<?>) {
                mRealItemCount = ((InfinitePagerAdapter<?>) adapter).getWrappedPagerAdapter().getCount();
            } else {
                mRealItemCount = adapter.getCount();
            }

            final int itemCount = mRealItemCount;
            mDotDrawables = new DotDrawable[itemCount];
            mDots = new View[itemCount];
            for (int i = 0; i < itemCount; i++) {
                final View view = new View(getContext());
                mDotDrawables[i] = new DotDrawable(i == viewPager.getCurrentItem() ? mSelectedDotColor : mDefaultDotColor);
                mDots[i] = view;
                if (Build.VERSION.SDK_INT <= 15) {
                    view.setBackgroundDrawable(mDotDrawables[i]);
                } else {
                    view.setBackground(mDotDrawables[i]);
                }
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

    private int mCurrentPage;

    private float mCurrentOffset;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        final int adapterPosition = position;
        position = position % mRealItemCount;
        mCurrentPage = position;
        mCurrentOffset = positionOffset;
        for (int i = 0; i < mDotDrawables.length; i++) {
            final boolean colorChanged;
            if (i == mCurrentPage) {
                colorChanged = mDotDrawables[i].setColor(ColorUtility.getTransitionColor(mSelectedDotColor, mDefaultDotColor, 1 - mCurrentOffset));
            } else if (i == mCurrentPage + 1 || ((adapterPosition > mRealItemCount) && (i == (mCurrentPage + 1) % mRealItemCount))) {
                colorChanged = mDotDrawables[i].setColor(ColorUtility.getTransitionColor(mSelectedDotColor, mDefaultDotColor, mCurrentOffset));
            } else {
                colorChanged = mDotDrawables[i].setColor(mDefaultDotColor);
            }
            if (colorChanged) {
                mDots[i].invalidate();
            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class DotDrawable extends ShapeDrawable {

        private int mColor;

        public DotDrawable(final int color) {
            super(new OvalShape());
        }

        @Override
        protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
            paint.setColor(mColor);
            super.onDraw(shape, canvas, paint);
        }

        public boolean setColor(final int color) {
            if (mColor == color) {
                return false;
            }
            mColor = color;
            return true;
        }
    }
}
