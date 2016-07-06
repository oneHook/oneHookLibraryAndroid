package com.onehook.widget.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by eaglediaotomore on 2016-03-12.
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpaceTop;

    private int mSpaceBottom;

    private int mSpaceLeft;

    private int mSpaceRight;

    /**
     * @param space for top, right, left, bottom
     */
    public SpaceItemDecoration(int space) {
        this(space, space, space, space);
    }

    /**
     * @param spaceTop
     * @param spaceBottom
     * @param spaceLeft
     * @param spaceRight
     */
    public SpaceItemDecoration(int spaceTop, int spaceBottom, int spaceLeft, int spaceRight) {
        this.mSpaceTop = spaceTop;
        this.mSpaceBottom = spaceBottom;
        this.mSpaceLeft = spaceLeft;
        this.mSpaceRight = spaceRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpaceLeft;
        outRect.right = mSpaceRight;
        outRect.bottom = mSpaceBottom;
        outRect.top = mSpaceTop;
    }
}