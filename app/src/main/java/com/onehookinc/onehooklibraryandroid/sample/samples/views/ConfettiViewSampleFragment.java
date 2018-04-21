package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.view.confetti.ConfettiCellBuilder;
import com.onehook.view.confetti.ConfettiView;
import com.onehook.view.confetti.ConfettiViewImageCell;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class ConfettiViewSampleFragment extends BaseFragment {

    public static int colorIndex = 0;
    private int[] mConfettiColors = new int[]{
            R.color.red_300,
            R.color.red_400,
            R.color.red_500,
            R.color.red_600,
            R.color.red_700,
            R.color.red_800
    };

    private ConfettiView mConfettiView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_confetti_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConfettiView = view.findViewById(R.id.fragment_sample_confetti_view);

        view.findViewById(R.id.fragment_sample_confetti_view_button1).setOnClickListener(v -> button1Clicked());
        view.findViewById(R.id.fragment_sample_confetti_view_button2).setOnClickListener(v -> button2Clicked());
        view.findViewById(R.id.fragment_sample_confetti_view_button3).setOnClickListener(v -> button3Clicked());
        view.findViewById(R.id.fragment_sample_confetti_view_button4).setOnClickListener(v -> button4Clicked());
    }

    private void button1Clicked() {
        if(mConfettiView.isAnimating()) {
            Snackbar.make(getView(), "Animation in progress, just wait", Snackbar.LENGTH_SHORT);
            return;
        }

        mConfettiView.setConfettiCellCount(100);
        mConfettiView.setSizeDiffRatio(1.5f);
        mConfettiView.setConfettiRotation(360);
        mConfettiView.start(new ConfettiView.IConfettiViewCustomizationListener() {
            @Override
            public View createConfettiCell(ConfettiCellBuilder builder, int index) {
                return builder.emojiCell(R.string.emoji_poop);
            }
        });
    }

    private void button2Clicked() {
        if(mConfettiView.isAnimating()) {
            Snackbar.make(getView(), "Animation in progress, just wait", Snackbar.LENGTH_SHORT);
            return;
        }

        mConfettiView.setConfettiCellCount(66);
        mConfettiView.setSizeDiffRatio(1);
        mConfettiView.setConfettiRotation(0);
        mConfettiView.start(new ConfettiView.IConfettiViewCustomizationListener() {
            @Override
            public View createConfettiCell(ConfettiCellBuilder builder, int index) {
                return builder.emojiCell(R.string.emoji_dog);
            }
        });
    }

    private void button3Clicked() {
        if(mConfettiView.isAnimating()) {
            Snackbar.make(getView(), "Animation in progress, just wait", Snackbar.LENGTH_SHORT);
            return;
        }

        mConfettiView.setConfettiCellCount(66);
        mConfettiView.setConfettiDelayRatio(2);
        mConfettiView.setSizeDiffRatio(1);
        mConfettiView.setConfettiRotation(720);
        mConfettiView.start(new ConfettiView.IConfettiViewCustomizationListener() {
            @Override
            public View createConfettiCell(ConfettiCellBuilder builder, int index) {
                final int colorRes = mConfettiColors[colorIndex];
                colorIndex++;
                if (colorIndex >= mConfettiColors.length) {
                    colorIndex -= mConfettiColors.length;
                }

                return builder.imageCell(R.drawable.ic_close_black_36dp, colorRes);
            }
        });
    }

    private void button4Clicked() {
        if(mConfettiView.isAnimating()) {
            Snackbar.make(getView(), "Animation in progress, just wait", Snackbar.LENGTH_SHORT);
            return;
        }

        mConfettiView.setConfettiCellCount(66);
        mConfettiView.setConfettiDelayRatio(8);
        mConfettiView.setSizeDiffRatio(1);
        mConfettiView.setConfettiRotation(360);
        mConfettiView.start(new ConfettiView.IConfettiViewCustomizationListener() {
            @Override
            public View createConfettiCell(ConfettiCellBuilder builder, int index) {
                final int colorRes = mConfettiColors[colorIndex];
                colorIndex++;
                if (colorIndex >= mConfettiColors.length) {
                    colorIndex -= mConfettiColors.length;
                }

                return builder.imageCell(R.drawable.ic_oda_avatar, colorRes);
            }
        });
    }


    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Confetti Sample");
    }
}
