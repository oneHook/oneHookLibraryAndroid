package com.onehookinc.onehooklibraryandroid.sample.samples;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.view.AnimatedNumberTextView;
import com.onehook.view.ButtonWithBadge;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class MiscViewSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_view_misc, container, false);
    }

    private AnimatedNumberTextView mNumberTextView;
    private ButtonWithBadge mButtonWithBagde;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumberTextView = view.findViewById(R.id.fragment_sample_view_misc_animated_number_textview);
        mButtonWithBagde = view.findViewById(R.id.fragment_sample_view_misc_random_button);
        mButtonWithBagde.setOnClickListener(v -> onRandomButtonClicked());
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Misc Views Sample");
    }

    private void onRandomButtonClicked() {
        final int newNumber = (int) (Math.random() * 100000);
        mNumberTextView.createAnimation(newNumber, 150).start();
        mButtonWithBagde.setBadgeCount(newNumber, false);
    }
}
