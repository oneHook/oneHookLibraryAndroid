package com.onehookinc.onehooklibraryandroid.sample.samples;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.view.pager.SimpleViewPagerAdapter;
import com.onehook.view.pager.SlideShowViewPager;
import com.onehook.view.pager.VerticalViewPager;
import com.onehook.view.pager.transformer.CarouselTransformer;
import com.onehook.view.pager.transformer.VerticalPagerDefaultTransformer;
import com.onehook.widget.adapter.InfinitePagerAdapter;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

import org.w3c.dom.Text;

public class ViewPagerSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_view_pagers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SlideShowViewPager p1 = view.findViewById(R.id.fragment_sample_view_pagers_p1);
        p1.setAdapter(new DemoPager(getContext()));
        p1.startSlideShow();


        final SlideShowViewPager p2 = view.findViewById(R.id.fragment_sample_view_pagers_p2);
        p2.setPageTransformer(false, new VerticalPagerDefaultTransformer());
        p2.setAdapter(new DemoPager(getContext()));
        p2.startSlideShow();

        final SlideShowViewPager p3 = view.findViewById(R.id.fragment_sample_view_pagers_p3);
        p3.setPageTransformer(false, new CarouselTransformer(0));
        p3.setAdapter(new InfinitePagerAdapter<>(new DemoPager(getContext())));
        p3.startSlideShow();
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("ViewPagers Sample");
    }


    private class DemoPager extends SimpleViewPagerAdapter {

        private int mColors[];

        public DemoPager(final Context context) {
            mColors = new int[]{ContextCompat.getColor(context, R.color.red_900),
                    ContextCompat.getColor(context, R.color.gray_800)};
        }

        @Override
        public View createView(LayoutInflater inflater, ViewGroup viewGroup, int position) {
            final TextView tv = new TextView(viewGroup.getContext());
            tv.setTextSize(30);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.WHITE);
            return tv;
        }

        @Override
        public void bindView(View view, int position) {
            final TextView tv = (TextView) view;
            tv.setBackgroundColor(mColors[position % mColors.length]);
            tv.setText(String.valueOf(position));
        }

        @Override
        public int getCount() {
            return 10;
        }
    }
}
