package com.onehook.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

    private static final String INVALID_RES = null;

    private static final String ARG_TITLE = "title";

    private static final String ARG_BUTTON1 = "button1";

    private static final String ARG_BUTTON2 = "button2";

    private static final String ARG_TAG = "tag";

    private static final String ARG_TEXT = "text";

    private static final String ARG_OBJECT_S = "objectS";

    private static final String ARG_OBJECT_P = "objectP";

    private static final String ARG_CANCELLABLE = "cancelable";

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

    /**
     * @param tag        tag of this dialog. can be used to identify the dialog
     * @param title      title res id
     * @param text       message res id
     * @param button     button 1 text res id (on the right)
     * @param object     additional serialized object
     * @param cancelable is dialog is cancelable when tapped outside
     * @return a new alert dialog fragment
     */
    private static OHAlertDialogFragment newInstance(final String tag, final String title,
                                                     final String text, final String button, final Object object,
                                                     final boolean cancelable) {
        final OHAlertDialogFragment frag = new OHAlertDialogFragment();
        final Bundle args = new Bundle();
        frag.setCancelable(true);
        args.putString(ARG_TAG, tag);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BUTTON1, button);
        args.putString(ARG_BUTTON2, null);
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

    /**
     * Creating and immediately show a new alert dialog with title, message and
     * two possible options. The caller must already subscribe to event bus in
     * order to receive callback.
     *
     * @param context      application context
     * @param fm           fragment manager
     * @param tag          string tag of this dialog. use this to identify your dialog.
     * @param titleResID   title text res id
     * @param messageResID message text res id
     * @param button1ResID button 1 text res id (on the right)
     * @param button2ResID button 2 text res id (on the left)
     * @param object       additional Serializable data, will be attached to call back
     *                     event
     * @param cancelable   is dialog is cancelable when tapped outside
     */
    public static OHAlertDialogFragment showAlertDialog(final Context context, final FragmentManager fm,
                                                 final String tag, final int titleResID, final int messageResID, final int button1ResID,
                                                 final int button2ResID, final Object object, final boolean cancelable) {
        final OHAlertDialogFragment newFragment = OHAlertDialogFragment.newInstance(tag, context
                        .getResources().getString(titleResID),
                context.getResources().getString(messageResID),
                context.getResources().getString(button1ResID),
                context.getResources().getString(button2ResID), object, cancelable);
        newFragment.show(fm, tag);
        return newFragment;
    }

    /**
     * Creating and immediately show a new alert dialog with title, message and
     * two possible options. The caller must already subscribe to event bus in
     * order to receive callback.
     *
     * @param fm         fragment manager
     * @param tag        string tag of this dialog. use this to identify your dialog.
     * @param title      title text
     * @param message    message text res id
     * @param button1    button 1 text
     * @param button2    button 2 text
     * @param object     additional Serializable data, will be attached to call back
     *                   event
     * @param cancelable is dialog is cancelable when tapped outside
     */
    public static void showAlertDialog(final FragmentManager fm, final String tag,
                                       final String title, final String message, final String button1, final String button2,
                                       final Object object, final boolean cancelable) {
        final DialogFragment newFragment = OHAlertDialogFragment.newInstance(tag, title,
                message, button1, button2, object, cancelable);
        newFragment.show(fm, tag);
    }

    /**
     * Creating and immediately show a new alert dialog with title, message and
     * only one possible option. The caller must already subscribe to event bus
     * in order to receive callback.
     *
     * @param fm           fragment manager
     * @param tag          string tag of this dialog. use this to identify your dialog.
     * @param titleResID   title text res id
     * @param messageResID message text res id
     * @param object       additional Serializable data, will be attached to call back
     *                     event
     * @param cancelable   is dialog is cancelable when tapped outside
     */
    public static void showAlertDialog(final Context context, final FragmentManager fm,
                                       final String tag, final int titleResID, final int messageResID, final int buttonResID,
                                       final Object object, final boolean cancelable) {
        final DialogFragment newFragment = OHAlertDialogFragment.newInstance(tag, context
                        .getResources().getString(titleResID),
                context.getResources().getString(messageResID),
                context.getResources().getString(buttonResID), object, cancelable);
        newFragment.show(fm, tag);
    }

    /**
     * Creating and immediately show a new alert dialog with title, message and
     * only one possible option. The caller must already subscribe to event bus
     * in order to receive callback.
     *
     * @param fm         fragment manager
     * @param tag        string tag of this dialog. use this to identify your dialog.
     * @param title      title text
     * @param message    message text
     * @param button     button text (single button)
     * @param object     additional Serializable data, will be attached to call back
     *                   event
     * @param cancelable is dialog is cancelable when tapped outside
     */
    public static void showAlertDialog(final FragmentManager fm, final String tag,
                                       final String title, final String message, final String button,
                                       final Object object, final boolean cancelable) {
        final DialogFragment newFragment = OHAlertDialogFragment.newInstance(tag, title,
                message, button, object, cancelable);
        newFragment.show(fm, tag);
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
        if (button2 != INVALID_RES) {
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
