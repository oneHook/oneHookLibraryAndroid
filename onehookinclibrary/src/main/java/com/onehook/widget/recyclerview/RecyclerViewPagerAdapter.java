package com.onehook.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
        mViewPagerRef = new WeakReference<RecyclerViewPager>(viewPager);
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
            /*
             * force each item to have the same size of the screen.
             */
            ViewGroup.LayoutParams lp = itemView.getLayoutParams() == null ?
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) :
                    itemView.getLayoutParams();
            if (pager.getLayoutManager().canScrollHorizontally()) {
                lp.width = pager.getWidth() - pager.getPaddingLeft() - pager.getPaddingRight();
            } else {
                lp.height = pager.getHeight() - pager.getPaddingTop() - pager.getPaddingBottom();
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
