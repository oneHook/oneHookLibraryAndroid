package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.view.ScaleView;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class ScaleViewSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_scale_view, container, false);
    }

    private TextView mNumberTextView;
    private ScaleView mScaleView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberTextView = view.findViewById(R.id.fragment_sample_scale_view_textview);
        mScaleView = view.findViewById(R.id.fragment_sample_scale_view);

        mScaleView.setListener(new ScaleView.ScaleViewListener() {
            @Override
            public void onScaleMoved(ScaleView scaleView, int currentScale) {
                mNumberTextView.setText(String.valueOf(currentScale));
            }
        });
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("ScaleView Sample");
    }
}
