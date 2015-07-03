
package com.onehook.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by EagleDiao on 14-11-11.
 */
public class RecyclerViewWithInset extends RecyclerView {

    private int mTopContentInset;

    private int mBottomContentInset;

    private int mItemMargin;

    public RecyclerViewWithInset(Context context) {
        super(context);
        init();
    }

    public RecyclerViewWithInset(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerViewWithInset(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mTopContentInset = 0;
        mBottomContentInset = 0;
        mItemMargin = 0;
        addItemDecoration(new InsetItemDecoration());
    }

    public void setTopContentInset(int topInset) {
        mTopContentInset = topInset;
    }

    public void setBottomContentInset(final int bottomInset) {
        mBottomContentInset = bottomInset;
    }

    public void setItemMargin(final int itemMargin) {
        mItemMargin = itemMargin;
    }

    private class InsetItemDecoration extends ItemDecoration {

        public InsetItemDecoration() {
            super();
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            /*
             * find the position of the child, and figure out if the child is at
             * the first row
             */
            final int position = parent.getChildPosition(view);

            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                /*
                 * if using grid layout manager
                 */
                final GridLayoutManager.SpanSizeLookup sizeLookUp = ((GridLayoutManager) parent
                        .getLayoutManager()).getSpanSizeLookup();
                final int span = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                final int groupIndex = sizeLookUp.getSpanGroupIndex(position, span);
                if (groupIndex == 0) {
                    outRect.top = mTopContentInset;
                } else {
                    final int lastRowGroupIndex = sizeLookUp.getSpanGroupIndex(parent.getAdapter()
                            .getItemCount() - 1, span);
                    if (groupIndex == lastRowGroupIndex) {
                        outRect.bottom = mBottomContentInset;
                    }
                }
            } else {
                /*
                 * if using LinearLayoutManager
                 */
                if (position == 0) {
                    outRect.top = mTopContentInset;
                }
                if (position == parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = mBottomContentInset;
                }
            }

            outRect.left = mItemMargin / 2;
            outRect.right = mItemMargin / 2;
            outRect.top += mItemMargin / 2;
            outRect.bottom += mItemMargin / 2;
        }
    }

    @Override
    public boolean isInEditMode() {
        return false;
    }

}
