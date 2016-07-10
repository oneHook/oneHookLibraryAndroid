package com.onehook.widget.adapter;

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
    public int getCount() {
        if (mWrappedAdapter.getCount() == 1) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    public int getMidPosition() {
        if (mWrappedAdapterItemCount == 1) {
            return 0;
        }
        return mWrappedAdapterItemCount * (Integer.MAX_VALUE / mWrappedAdapterItemCount / 2);
    }
}
