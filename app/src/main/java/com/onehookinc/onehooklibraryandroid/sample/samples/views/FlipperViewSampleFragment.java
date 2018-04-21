package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.view.viewflipper.FlipperView;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class FlipperViewSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_flipper_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FlipperView flipperView = view.findViewById(R.id.fragment_sample_flipper_view);

        final TextView tv1 = flipperView.getFrontPage().findViewById(R.id.cell_rounded_corner_textview);
        tv1.setText("1");
        flipperView.setHasNextPage(true);

        flipperView.setCallback(new FlipperView.FlipperViewCallback() {

            @Override
            public void onWillPresentNextPage(View nextPage) {
                TextView tv = nextPage.findViewById(R.id.cell_rounded_corner_textview);
                tv.setText(String.valueOf(System.currentTimeMillis()));
            }

            @Override
            public boolean onDidPresentNextPage() {
                return true;
            }
        });

        flipperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipperView.flipPage(FlipperView.Direction.RIGHT);
            }
        });
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("FlipperView Sample");
    }
}
