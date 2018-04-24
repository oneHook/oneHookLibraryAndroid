package com.onehookinc.onehooklibraryandroid.sample.samples.demo.dateviewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.view.pager.transformer.CarouselTransformer;
import com.onehook.widget.adapter.TaggedFragmentStatePagerAdapter;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class DateViewPaperDemo extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo_date_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ViewPager p1 = view.findViewById(R.id.fragment_demo_date_viewpager);
        p1.setClipToPadding(false);
        p1.setPadding(64, 0, 64, 0);
        p1.setPageTransformer(false, new CarouselTransformer(64, 1));
        p1.setAdapter(new FragmentPager(getChildFragmentManager()));
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Date View Pager Sample");
    }

    private class FragmentPager extends TaggedFragmentStatePagerAdapter {

        public FragmentPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new SimpleCardsFragment();
        }

        @Override
        public String getTag(int position) {
            return String.valueOf(position);
        }

        @Override
        public int getCount() {
            return 100;
        }
    }
}
