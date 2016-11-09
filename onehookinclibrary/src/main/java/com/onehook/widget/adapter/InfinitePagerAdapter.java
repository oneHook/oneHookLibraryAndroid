package com.onehook.widget.adapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by eaglediaotomore on 2016-07-10.
 */

public class InfinitePagerAdapter<T extends PagerAdapter> extends PagerAdapter {

    private T mWrappedAdapter;

    private int mWrappedAdapterItemCount;

    public InfinitePagerAdapter(T wrappedAdapter) {
        mWrappedAdapter = wrappedAdapter;
        if (mWrappedAdapter == null) {
            throw new RuntimeException("Wrapped adapter cannot be null");
        }
        mWrappedAdapterItemCount = mWrappedAdapter.getCount();
    }

    public PagerAdapter getWrappedPagerAdapter() {
        return mWrappedAdapter;
    }

    /**
     * @param infinitePosition
     * @return
     */
    public int getRealPosition(final int infinitePosition) {
        return infinitePosition % mWrappedAdapter.getCount();
    }

    /**
     * @return
     */
    public int getMidPosition() {
        if (mWrappedAdapterItemCount == 1) {
            return 0;
        }
        return mWrappedAdapterItemCount * (Integer.MAX_VALUE / mWrappedAdapterItemCount / 2);
    }

    /**
     * @param index
     * @return
     */
    public int getMidPosition(final int index) {
        if (mWrappedAdapterItemCount == 1) {
            return 0;
        }
        return mWrappedAdapterItemCount * (Integer.MAX_VALUE / mWrappedAdapterItemCount / 2) + index;
    }

    /* proxy */

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mWrappedAdapter.isViewFromObject(view, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int normalizedIndex = position % mWrappedAdapterItemCount;
        return mWrappedAdapter.instantiateItem(container, normalizedIndex);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        final int normalizedIndex = position % mWrappedAdapterItemCount;
        mWrappedAdapter.destroyItem(container, normalizedIndex, object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        final int normalizedIndex = position % mWrappedAdapterItemCount;
        mWrappedAdapter.setPrimaryItem(container, normalizedIndex, object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final int normalizedIndex = position % mWrappedAdapterItemCount;
        return mWrappedAdapter.getPageTitle(normalizedIndex);
    }

    @Override
    public float getPageWidth(int position) {
        final int normalizedIndex = position % mWrappedAdapterItemCount;
        return mWrappedAdapter.getPageWidth(normalizedIndex);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mWrappedAdapter.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        mWrappedAdapter.finishUpdate(container);
    }

    @Override
    public Parcelable saveState() {
        return mWrappedAdapter.saveState();
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        mWrappedAdapter.restoreState(state, loader);
    }

    @Override
    public int getItemPosition(Object object) {
        return mWrappedAdapter.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mWrappedAdapter.notifyDataSetChanged();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mWrappedAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mWrappedAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        if (mWrappedAdapter.getCount() == 1) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }


}
