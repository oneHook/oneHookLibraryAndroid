
package com.onehook.widget;

import android.support.v7.widget.RecyclerView;

/**
 * ParallaxOnScrollListener. This Parallax on scroll listener will apply
 * parallax effect on the first N view items of the RecyclerView. N will be
 * provided by user. If user wish to show the header with parallax effect half
 * revealed when covered by following items, user also have to override the
 * getBottomDecorationHeight in LayoutManager class to *extend* the view height.
 * so that the view with parallax will not be recycled(removed from view parent)
 * immediately after the view is *out of the bound*.
 *
 * @author EagleDiao
 */
public class ParallaxOnScrollListener extends RecyclerView.OnScrollListener {
    /**
     * Current content offset, this content offset is depending on orientation.
     * Content offset is not valid anymore if size of the recycler view changes.
     */
    private int mContentOffsetY;

    /**
     * Number of views to do paralex effect.
     */
    private int mNumberOfParalexHeader;

    /**
     * Constructor.
     *
     * @param numberOfParalexHeader
     */
    public ParallaxOnScrollListener(int numberOfParalexHeader) {
        mNumberOfParalexHeader = numberOfParalexHeader;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        /*
         * Roughly remember the current content offset, we use this value to
         * reset the translation of the views that we will to do parallax
         * effect.
         */
        mContentOffsetY += dy;
        /*
         * Ignore parallax effect if content offset is smaller than 0.
         */
        if (mContentOffsetY < 0) {
            mContentOffsetY = 0;
        }
        onViewScrolled(recyclerView, dy, mContentOffsetY);
        for (int i = 0; i < mNumberOfParalexHeader; i++) {
            final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForPosition(i);
            if (viewHolder != null) {
                viewHolder.itemView.setTranslationY(mContentOffsetY * 0.6f);
            }
        }
    }

    /**
     * Child class can override this class to receive on scroll changed info.
     *
     * @param recyclerView   recycler view
     * @param dy             difference in Y
     * @param contentOffsetY content offset along y axis
     */
    public void onViewScrolled(final RecyclerView recyclerView, final int dy,
                               final int contentOffsetY) {

    }

    /**
     * Reset content offset back to 0.
     */
    public void resetContentOffset() {
        mContentOffsetY = 0;
    }
}
