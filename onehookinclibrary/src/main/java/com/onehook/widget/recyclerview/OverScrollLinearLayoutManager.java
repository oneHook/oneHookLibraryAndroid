package com.onehook.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by eaglediaotomore on 2016-10-29.
 */

public class OverScrollLinearLayoutManager extends LinearLayoutManager {

    private int mVerticalOverScrollAmount;

    public OverScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public OverScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public OverScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        return super.scrollVerticallyBy(dy, recycler, state);

        final int scrollRange = super.scrollVerticallyBy(dy, recycler, state);

        if(scrollRange == 0) {
            mVerticalOverScrollAmount += dy;
        } else {
            mVerticalOverScrollAmount = 0;
        }
        Log.d("oneHook", "Scroll Range " + scrollRange + " dy: " + dy + "  Over scroll amount " + mVerticalOverScrollAmount);

        int overscroll = dy - scrollRange;
        if (overscroll > 0) {
            // bottom overscroll
            Log.d("oneHook", "Bottom overscroll " + overscroll + " , " + dy);
        } else if (overscroll < 0) {
            // top overscroll
            Log.d("oneHook", "Top overscroll " + overscroll + " , " + dy);
        }
        return scrollRange;
    }
}
