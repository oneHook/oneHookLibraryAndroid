package com.onehookinc.onehooklibraryandroid.sample.samples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

       mConfettiView.postDelayed(new Runnable() {
           @Override
           public void run() {
               startConfetti();
           }
       }, 100);
    }

    private void startConfetti() {
        mConfettiView.start(new ConfettiView.IConfettiViewCustomizationListener() {
            @Override
            public View createConfettiCell(ConfettiCellBuilder builder, int index) {
                final int colorRes = mConfettiColors[colorIndex];
                colorIndex++;
                if (colorIndex >= mConfettiColors.length) {
                    colorIndex -= mConfettiColors.length;
                }
//                return builder.imageCell(R.drawable.ic_close_black_24dp,
//                        colorRes);

                return builder.emojiCell(R.string.emoji_dog, colorRes);
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
