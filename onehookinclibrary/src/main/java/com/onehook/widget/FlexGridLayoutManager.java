
package com.onehook.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * A Flexiable GridLayoutManager that will measure each cell based on the
 * provided sizing information. But All ViewHolder must be extending from
 * FlexGridViewHolder.
 *
 * @author EagleDiao
 */
public class FlexGridLayoutManager extends GridLayoutManager {

    final int mParallaxHeaderCount;

    /**
     * @param context                context
     * @param spanCount              max span count
     * @param numberOfParallaxHeader count of parallax header
     */
    public FlexGridLayoutManager(Context context, int spanCount, int numberOfParallaxHeader) {
        super(context, spanCount);
        mParallaxHeaderCount = numberOfParallaxHeader;
    }

    @Override
    public int getDecoratedMeasuredHeight(View child) {
        /*
         * We leave the width measuring to the super class, we can assume the
         * width measuring always based on span information. when we are
         * measuring the height information, either we provide absolute number,
         * or make it square, or leave it to the super.
         */
//        final Integer sizing = (Integer) child.getTag(R.id.view_sizing_tag_key);
//        if (sizing != null) {
//            final int superMeasured = super.getDecoratedMeasuredHeight(child);
//            final int childMeasured = child.getMeasuredHeight();
//            final int offset = superMeasured - childMeasured;
//            /*
//             * If we wish to make it a square, just use measured width for
//             * height
//             */
//            if (sizing.intValue() == FlexGridViewHolder.SIZE_SQUARE) {
//                return getDecoratedMeasuredWidth(child) + offset;
//            } else if (sizing.intValue() > 0) {
//                /*
//                 * Absolute height
//                 */
//                return sizing.intValue() + offset;
//            }
//        }
        /*
         * No sizing information available or sizing info is SIZE_WRAP_CONTENT,
         * leave the measuring to super.
         */
        return super.getDecoratedMeasuredHeight(child);
    }

    @Override
    public int getBottomDecorationHeight(View child) {
        final int position = getPosition(child);
        if (position < mParallaxHeaderCount) {
            /*
             * Extend the child bottom decoration height for parallax effect.
             */
            return super.getBottomDecorationHeight(child) + child.getMeasuredHeight();
        }
        return super.getBottomDecorationHeight(child);
    }

    /**
     * @author EagleDiao
     */
    public static class FlexGridViewHolder extends ViewHolder {

        public static final int SIZE_SQUARE = -1;

        public static final int SIZE_WRAP_CONTENT = 0;

//        final int mSizing;

        public FlexGridViewHolder(View view, final int sizing) {
            super(view);
//            mSizing = sizing;
//            view.setTag(R.id.view_sizing_tag_key, Integer.valueOf(sizing));
        }

    }

}
