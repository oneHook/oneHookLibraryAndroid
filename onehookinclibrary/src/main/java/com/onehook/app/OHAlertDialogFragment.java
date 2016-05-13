package com.onehook.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.onehookinc.androidlib.R;

import java.io.Serializable;

/**
 * Created by EagleDiao on 2016-04-29.
 */
public class OHAlertDialogFragment extends DialogFragment {

    public interface IOHAlertDialogFragmentCallback {

        void onAlertDialogFragmentButton1Clicked(final String tag, final Object payload);

        void onAlertDialogFragmentButton2Clicked(final String tag, final Object payload);

        void onAlertDialogFragmentCanceled(final String tag, final Object payload);
    }

    private static final String ARG_TITLE = "title";

    private static final String ARG_BUTTON1 = "button1";

    private static final String ARG_BUTTON2 = "button2";

    private static final String ARG_TAG = "tag";

    private static final String ARG_TEXT = "text";

    private static final String ARG_OBJECT_S = "objectS";

    private static final String ARG_OBJECT_P = "objectP";

    private static final String ARG_CANCELLABLE = "cancelable";

    public static class OHAlertDialogFragmentBuilder {

        private Resources mRes;

        String mFragmentTag;

        String mButton1Text;

        String mButton2Text;

        String mTitle;

        String mMesssage;

        Serializable mObjectS;

        Parcelable mObjectP;

        boolean mCancelable;

        public OHAlertDialogFragmentBuilder(final Resources res) {
            mRes = res;
            mFragmentTag = null;
            mButton1Text = null;
            mButton2Text = null;
            mTitle = null;
            mMesssage = null;
            mObjectS = null;
            mObjectP = null;
            mCancelable = false;
        }

        public OHAlertDialogFragmentBuilder fragmentTag(final String tag) {
            mFragmentTag = tag;
            return this;
        }


        public OHAlertDialogFragmentBuilder button1Text(final String text) {
            mButton1Text = text;
            return this;
        }

        public OHAlertDialogFragmentBuilder button1Text(final int res) {
            mButton1Text = mRes.getString(res);
            return this;
        }

        public OHAlertDialogFragmentBuilder button2Text(final String text) {
            mButton2Text = text;
            return this;
        }

        public OHAlertDialogFragmentBuilder button2Text(final int res) {
            mButton2Text = mRes.getString(res);
            return this;
        }

        public OHAlertDialogFragmentBuilder title(final String text) {
            mTitle = text;
            return this;
        }

        public OHAlertDialogFragmentBuilder title(final int res) {
            mTitle = mRes.getString(res);
            return this;
        }

        public OHAlertDialogFragmentBuilder message(final String text) {
            mMesssage = text;
            return this;
        }

        public OHAlertDialogFragmentBuilder message(final int res) {
            mMesssage = mRes.getString(res);
            return this;
        }


        public OHAlertDialogFragmentBuilder object(final Serializable object) {
            mObjectS = object;
            return this;
        }

        public OHAlertDialogFragmentBuilder object(final Parcelable object) {
            mObjectP = object;
            return this;
        }

        public OHAlertDialogFragmentBuilder cancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public OHAlertDialogFragment show(FragmentManager manager) {
            final OHAlertDialogFragment fragment;
            if (mObjectP == null) {
                fragment = OHAlertDialogFragment.newInstance(mFragmentTag, mTitle, mMesssage, mButton1Text, mButton2Text, mObjectS, mCancelable);
            } else {
                fragment = OHAlertDialogFragment.newInstance(mFragmentTag, mTitle, mMesssage, mButton1Text, mButton2Text, mObjectP, mCancelable);
            }
            fragment.show(manager, mFragmentTag);
            return fragment;
        }
    }


    /**
     * @param tag        tag of this dialog. can be used to identify the dialog
     * @param title      title res id
     * @param text       message res id
     * @param button1    button 1 text res id (on the right)
     * @param button2    button 2 text res id (on the left)
     * @param object     additional serialized  object
     * @param cancelable is dialog is cancelable when tapped outside
     * @return a new alert dialog fragment
     */
    private static OHAlertDialogFragment newInstance(final String tag, final String title,
                                                     final String text, final String button1, final String button2,
                                                     final Object object, final boolean cancelable) {
        final OHAlertDialogFragment frag = new OHAlertDialogFragment();
        frag.setCancelable(true);
        final Bundle args = new Bundle();
        args.putString(ARG_TAG, tag);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BUTTON1, button1);
        args.putString(ARG_BUTTON2, button2);
        args.putString(ARG_TEXT, text);
        if (object != null) {
            if (object instanceof Parcelable) {
                args.putParcelable(ARG_OBJECT_P, (Parcelable) object);
            } else if (object instanceof Serializable) {
                args.putSerializable(ARG_OBJECT_S, (Serializable) object);
            } else {
                throw new RuntimeException("Attached object can only be parcelable or serializable");
            }
        }
        args.putBoolean(ARG_CANCELLABLE, cancelable);
        frag.setArguments(args);
        return frag;
    }

    private IOHAlertDialogFragmentCallback mCallback;

    public void setCallback(final IOHAlertDialogFragmentCallback callback) {
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString(ARG_TITLE);
        final String button1 = getArguments().getString(ARG_BUTTON1);
        final String button2 = getArguments().getString(ARG_BUTTON2);
        final String text = getArguments().getString(ARG_TEXT);
        final Object attachedObject;
        if (getArguments().containsKey(ARG_OBJECT_S)) {
            attachedObject = getArguments().getSerializable(ARG_OBJECT_S);
        } else {
            attachedObject = getArguments().getParcelable(ARG_OBJECT_P);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_close_black_18dp).setTitle(title)
                .setPositiveButton(button1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallback == null) {
                            return;
                        }
                        mCallback.onAlertDialogFragmentButton1Clicked(getTag(), attachedObject);
                    }
                });
        if (button2 != null) {
            builder.setNegativeButton(button2, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallback == null) {
                        return;
                    }
                    mCallback.onAlertDialogFragmentButton2Clicked(getTag(), attachedObject);
                }
            });
        }
        builder.setMessage(text);
        final Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(getArguments().getBoolean(ARG_CANCELLABLE));
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mCallback == null) {
            return;
        }
        final Object attachedObject;
        if (getArguments().containsKey(ARG_OBJECT_S)) {
            attachedObject = getArguments().getSerializable(ARG_OBJECT_S);
        } else {
            attachedObject = getArguments().getParcelable(ARG_OBJECT_P);
        }
        mCallback.onAlertDialogFragmentCanceled(getTag(), attachedObject);
    }

}
