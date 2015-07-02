
package com.onehook.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

public class OneHookProgressDialogFragment extends DialogFragment {

    private static final String ARG_TITLE_RES = "argTitleRes";

    public static OneHookProgressDialogFragment newInstance(final int titleRes) {
        final OneHookProgressDialogFragment frag = new OneHookProgressDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_TITLE_RES, titleRes);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final int titleRes = args.getInt(ARG_TITLE_RES);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(titleRes));
        dialog.setIndeterminate(true);
        setCancelable(false);
        return dialog;
    }

}
