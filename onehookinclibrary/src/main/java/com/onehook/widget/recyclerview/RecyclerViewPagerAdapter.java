package com.onehook.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.onehookinc.androidlib.R;

import java.lang.ref.WeakReference;

/**
 * RecyclerViewPagerAdapter </br>
 * Adapter wrapper. make sure each page is full screen.
 */
public class RecyclerViewPagerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /**
     * Parent pager weak ref.
     */
    private final WeakReference<RecyclerViewPager> mViewPagerRef;

    /**
     * Wrapped adapter.
     */
    RecyclerView.Adapter<VH> mAdapter;


    public RecyclerViewPagerAdapter(RecyclerViewPager viewPager, RecyclerView.Adapter<VH> adapter) {
        mAdapter = adapter;
        mViewPagerRef = new WeakReference<>(viewPager);
        setHasStableIds(mAdapter.hasStableIds());
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mAdapter.onBindViewHolder(holder, position);
        final View itemView = holder.itemView;

        final RecyclerViewPager pager = mViewPagerRef.get();
        if (pager != null) {
            int heightOffset = pager.getOverlapOffset();
            /*
             * force each item to have the same size of the screen.
             */
            final ViewGroup.LayoutParams lp = itemView.getLayoutParams() == null ?
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) :
                    itemView.getLayoutParams();


            /* find original bottom padding */
            int bottomPadding = itemView.getPaddingBottom();
            if (itemView.getTag(R.id.recycler_view_pager_original_bottom_padding_key) != null) {
                final Integer savedBottomPadding = (Integer) itemView.getTag(R.id.recycler_view_pager_original_bottom_padding_key);
                bottomPadding = savedBottomPadding.intValue();
            } else {
                /*
                 * No original padding saved, save it.
                 */
                itemView.setTag(R.id.recycler_view_pager_original_bottom_padding_key, Integer.valueOf(bottomPadding));
            }

            /* reset the view to use original padding */
            itemView.setPadding(
                    itemView.getPaddingLeft(),
                    itemView.getPaddingTop(),
                    itemView.getPaddingRight(),
                    bottomPadding);

            /* if it is the last item */
            if (position == getItemCount() - 1) {
                /*
                 * check last item fill style.
                 */
                boolean shouldFillLastItem = false;
                if (mViewPagerRef != null && mViewPagerRef.get() != null) {
                    shouldFillLastItem = mViewPagerRef.get().shouldFillLastItem();
                }

                if (shouldFillLastItem) {
                /* last item will take over whole screen */
                    heightOffset = 0;
                } else {
                    /* use padding to fill */
                    itemView.setPadding(
                            itemView.getPaddingLeft(),
                            itemView.getPaddingTop(),
                            itemView.getPaddingRight(),
                            bottomPadding + heightOffset);
                    heightOffset = 0;
                }
            }

            if (pager.getLayoutManager().canScrollHorizontally()) {
                lp.width = pager.getWidth() - pager.getPaddingLeft() - pager.getPaddingRight();
            } else {
                lp.height = pager.getHeight() - pager.getPaddingTop() - pager.getPaddingBottom() - heightOffset;
            }
            itemView.setLayoutParams(lp);
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        mAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }
}
