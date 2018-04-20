package com.onehook.view.pager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Simple ViewPager adapter that supports only one types of view.
 */
public abstract class SimpleViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> mRecycled;

    public SimpleViewPagerAdapter() {
        mRecycled = new ArrayList<>();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final View v;
        if(mRecycled.size() > 0) {
            v = mRecycled.remove(mRecycled.size() - 1);
        } else {
            v = createView(LayoutInflater.from(collection.getContext()), collection, position);
        }
        bindView(v, position);
        collection.addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
        mRecycled.add((View) view);
    }

    public abstract View createView(final LayoutInflater inflater, final ViewGroup viewGroup, final int position);

    public abstract void bindView(final View view, int position);

}
