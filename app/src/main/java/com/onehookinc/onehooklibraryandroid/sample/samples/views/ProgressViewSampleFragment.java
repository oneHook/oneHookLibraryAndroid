package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.view.progress.ProgressBar;
import com.onehook.view.progress.ProgressRing;
import com.onehook.view.viewflipper.FlipperView;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class ProgressViewSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_progress_view, container, false);
    }

    private ProgressBar mProgressBar1;
    private ProgressBar mProgressBar2;

    private ProgressRing mProgressRing1;
    private ProgressRing mProgressRing2;
    private ProgressRing mProgressRing3;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressBar1 = view.findViewById(R.id.fragment_sample_progress_view_bar1);
        mProgressBar2 = view.findViewById(R.id.fragment_sample_progress_view_bar2);
        mProgressRing1 = view.findViewById(R.id.fragment_sample_progress_view_ring1);
        mProgressRing2 = view.findViewById(R.id.fragment_sample_progress_view_ring2);
        mProgressRing3 = view.findViewById(R.id.fragment_sample_progress_view_ring3);

        view.findViewById(R.id.fragment_sample_progress_view_random_button).setOnClickListener(v -> onRandomButtonClicked());
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("ProgressView Sample");
    }

    private void onRandomButtonClicked() {
        int bigRed = ContextCompat.getColor(getContext(), R.color.red_800);
        int lightRed = ContextCompat.getColor(getContext(), R.color.red_200);

        float progress = (float) Math.random();

        if(progress < 0.5) {
            final AnimatorSet animator = new AnimatorSet();
            animator.playTogether(mProgressBar1.createProgressAnimation(progress, 250),
                    mProgressBar1.createRingColorAnimation(lightRed, 250));
            animator.start();
        } else {
            final AnimatorSet animator = new AnimatorSet();
            animator.playTogether(mProgressBar1.createProgressAnimation(progress, 250),
                    mProgressBar1.createRingColorAnimation(bigRed, 250));
            animator.start();
        }

        mProgressBar2.createProgressAnimation((float) Math.random(), 250).start();
        mProgressRing1.createProgressAnimation((float) Math.random(), 250).start();
        mProgressRing2.createProgressAnimation((float) Math.random(), 250).start();
        mProgressRing3.createProgressAnimation((float) Math.random(), 250).start();

    }
}
