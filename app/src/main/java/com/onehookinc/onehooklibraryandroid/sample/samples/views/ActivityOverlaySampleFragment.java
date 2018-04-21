package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.util.color.ColorUtility;
import com.onehook.view.overlay.ActivityOverlayView;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class ActivityOverlaySampleFragment extends BaseFragment {

    private ActivityOverlayView mOverlayView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_activity_overlay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.fragment_sample_activity_overlay_fab).setOnClickListener(v -> onFabClicked());
    }

    private void onFabClicked() {
        if (mOverlayView != null) {
            mOverlayView.destroy(getActivity(), false);
            mOverlayView = null;
        }

        final ActivityOverlayView.Builder builder = new ActivityOverlayView.Builder(getActivity());
        mOverlayView = builder.backgroundColor(ColorUtility.getColorWithAlpha(ResourcesCompat.getColor(getResources(), R.color.gray_900, null), 0.5f))
                .fadein(300)
                .contentRes(R.layout.view_popup)
                .build();

        mOverlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOverlayView.destroy(getActivity(), true);
            }
        });
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Activity Overlay Sample");
    }
}
