package com.onehook.widget.loadingdialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by eaglediaotomore on 2016-03-24.
 */
public class ProgressDialogFragment extends DialogFragment {

    public static ProgressDialogFragment newInstance() {
        return new ProgressDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("A MESSAGE");
        dialog.setMessage("SOME MESSAGE");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }
}
