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

    private static final String KEY_TITLE = "keyTitle";

    private static final String KEY_MESSAGE = "keyMessage";

    private static final String KEY_CANCELABLE = "keyCancelable";

    public static class ProgressDialogFragmentBuilder {

        private String mTitle;

        private String mMessage;

        private boolean mCancelable;

        public ProgressDialogFragmentBuilder() {
            mTitle = null;
            mMessage = null;
            mCancelable = false;
        }

        public ProgressDialogFragmentBuilder setTitle(final String title) {
            mTitle = title;
            return this;
        }

        public ProgressDialogFragmentBuilder setMessage(final String message) {
            mMessage = message;
            return this;
        }

        public ProgressDialogFragmentBuilder setCancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public ProgressDialogFragment build() {
            final ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
            final Bundle args = new Bundle();
            if (mTitle != null) {
                args.putString(KEY_TITLE, mTitle);
            }
            if (mMessage != null) {
                args.putString(KEY_MESSAGE, mMessage);
            }
            args.putBoolean(KEY_CANCELABLE, mCancelable);
            progressDialogFragment.setArguments(args);
            return progressDialogFragment;
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String title = args.getString(KEY_TITLE);
        final String message = args.getString(KEY_MESSAGE);
        final boolean cancelable = args.getBoolean(KEY_CANCELABLE);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        if (title != null) {
            dialog.setTitle(title);
        }
        if (message != null) {
            dialog.setMessage(message);
        }
        dialog.setIndeterminate(true);
        dialog.setCancelable(cancelable);
        return dialog;
    }
}
