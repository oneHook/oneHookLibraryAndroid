package com.onehookinc.onehooklibraryandroid.sample.samples.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.view.FlowLayout;
import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseFragment;

public class FlowLayoutSampleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample_flow_layout, container, false);
    }

    @Override
    public void onToolbarReady(Toolbar toolbar) {
        super.onToolbarReady(toolbar);
        getBaseAcivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getBaseAcivity().getSupportActionBar().setTitle("FlowLayout Sample");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LayoutInflater inflater = LayoutInflater.from(getContext());

        final String[] messages = new String[]{"This", "is", "a", "something", "very", "import", "a",
                "message", "please read", "it", "carefully"};

        final FlowLayout flowLayout1 = view.findViewById(R.id.fragment_sample_flow_layout_flow1);
        for (String s : messages) {
            final TextView tv = (TextView) inflater.inflate(R.layout.item_textview_rounded, flowLayout1, false);
            tv.setText(s);
            flowLayout1.addView(tv);
        }
    }
}
