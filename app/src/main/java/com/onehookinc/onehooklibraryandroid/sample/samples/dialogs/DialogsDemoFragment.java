package com.onehookinc.onehooklibraryandroid.sample.samples.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onehook.app.ProgressDialogFragment;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class DialogsDemoFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_view_dialogs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.fragment_dialog_sample_button1).setOnClickListener(v -> onButton1Clicked());
        view.findViewById(R.id.fragment_dialog_sample_button2).setOnClickListener(v -> onButton2Clicked());
        view.findViewById(R.id.fragment_dialog_sample_button3).setOnClickListener(v -> onButton3Clicked());
        view.findViewById(R.id.fragment_dialog_sample_button4).setOnClickListener(v -> onButton4Clicked());
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("Dialogs");
    }

    private static final String TAG_PROGRESS_DIALOG = "tagProgress";

    private void onButton1Clicked() {
        final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment
                .ProgressDialogFragmentBuilder()
                .setMessage("I will go away in 2 seconds, I promise")
                .setTitle("a title")
                .build();
        progressDialogFragment.show(getChildFragmentManager(), TAG_PROGRESS_DIALOG);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getView() != null) {
                    ProgressDialogFragment pdf = (ProgressDialogFragment) getChildFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);
                    pdf.dismiss();
                }
            }
        }, 2000);
    }

    private void onButton2Clicked() {

    }

    private void onButton3Clicked() {

    }

    private void onButton4Clicked() {

    }
}
