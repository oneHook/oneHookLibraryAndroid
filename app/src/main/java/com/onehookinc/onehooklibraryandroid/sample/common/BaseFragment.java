package com.onehookinc.onehooklibraryandroid.sample.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.onehookinc.onehooklibraryandroid.R;

public class BaseFragment extends Fragment{

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Toolbar toolbar = view.findViewById(R.id.id_common_toolbar);
        if(toolbar != null) {
            final AppCompatActivity activity  = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            onToolbarReady(toolbar);
        }
    }

    public void onToolbarReady(final Toolbar toolbar) {

    }

    public BaseActivity getBaseAcivity() {
        return (BaseActivity) getActivity();
    }
}
