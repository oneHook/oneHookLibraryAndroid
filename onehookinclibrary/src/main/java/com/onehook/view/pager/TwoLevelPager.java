package com.onehook.view.pager;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.onehook.util.animator.AnimationEndListener;

/**
 * Created by EagleDiao on 2016-11-14.
 */

public class TwoLevelPager extends FrameLayout {

    /**
     * Adapter.
     */
    public interface TwoLevelPagerAdapter {

        int getCount();

        View getViewAt(final LayoutInflater inflater, final ViewGroup viewGroup, final int position);

        void onViewDestroyed(final View view);
    }

    public TwoLevelPager(Context context) {
        super(context);
        commonInit();
    }

    public TwoLevelPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public TwoLevelPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public TwoLevelPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private TwoLevelPagerAdapter mAdapter;

    private View mTopView;

    private View mBottomView;

    private int mCurrentPage;

    private void commonInit() {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        populate();
    }

    private void populate() {
        if (mAdapter == null) {
            return;
        }

        if (mTopView == null) {
            final LayoutInflater inflater = LayoutInflater.from(getContext());
            mTopView = mAdapter.getViewAt(inflater, this, mCurrentPage);
            addView(mTopView);

            if (mCurrentPage + 1 <= mAdapter.getCount()) {
                mBottomView = mAdapter.getViewAt(inflater, this, mCurrentPage + 1);
                addView(mBottomView, 0);

                final Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        mBottomView.setScaleX(0.98f);
                        mBottomView.setScaleY(0.98f);
                        mBottomView.setTranslationY(25);
                    }
                };
                post(r);
            }
        }
    }

    public void setAdapter(final TwoLevelPagerAdapter adapter) {
        if (mAdapter != null && mAdapter == adapter) {
            /* same adapter, do nothing */
            return;
        }
        if (mAdapter != null) {
            /* change of adapter, need clear views */
            removeAllViews();
            mTopView = null;
            mBottomView = null;
            mCurrentPage = 0;
        }
        mAdapter = adapter;
        populate();
    }

    public TwoLevelPagerAdapter getAdapter() {
        return mAdapter;
    }

    public boolean gotoPage(final int page, final boolean animated) {
//        if (mAdapter != null && mCurrentPage < mAdapter.getCount() - 1) {
        mCurrentPage++;
        mTopView.animate().translationX(-mTopView.getMeasuredWidth()).setDuration(2000).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEndOrCanceled(Animator animation) {
//                    removeView(mTopView);
//                    mTopView = mAdapter.getViewAt(LayoutInflater.from(getContext()), TwoLevelPager.this,  mCurrentPage + 1);
//                    addView(mTopView, 0);
//                    final View temp = mTopView;
//                    mTopView = mBottomView;
//                    mBottomView = temp;
            }
        }).start();
        mBottomView.animate().translationX(0).setDuration(2000).start();


        return true;
//        } else {
//            return false;
//        }
    }

    public boolean gotoNextPage(final boolean animated) {
        return gotoPage(mCurrentPage + 1, animated);
    }
}
