package com.onehook.view.touchevent;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import java.lang.ref.WeakReference;

/**
 * Created by EagleDiao on 2016-03-29.
 */
public class CollapseAppBarLayoutOnScrollListener extends RecyclerView.OnScrollListener {

    private int mCurrentVerticalOffset;

    private WeakReference<AppBarLayout> mAppbarLayout;

    private WeakReference<SwipeRefreshLayout> mRefreshLayout;

    public CollapseAppBarLayoutOnScrollListener(final AppBarLayout appBarLayout, final SwipeRefreshLayout swipeRefreshLayout) {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mCurrentVerticalOffset = verticalOffset;
            }
        });
        mAppbarLayout = new WeakReference<>(null);
        mRefreshLayout = new WeakReference<>(swipeRefreshLayout);

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mRefreshLayout.get() != null) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mCurrentVerticalOffset == 0) {
                mRefreshLayout.get().setEnabled(true);
            } else {
                mRefreshLayout.get().setEnabled(false);
            }
        }
    }
}