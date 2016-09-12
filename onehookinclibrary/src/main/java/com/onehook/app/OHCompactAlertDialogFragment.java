package com.onehook.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.onehook.app.util.IconTitleListAdapter;
import com.onehookinc.androidlib.R;

import java.io.Serializable;

/**
 * Created by EagleDiao on 2016-04-29.
 */
public class OHCompactAlertDialogFragment extends DialogFragment {

    public static final int BUTTON_1_INDEX = -1;

    public static final int BUTTON_2_INDEX = -2;

    public static final int BUTTON_CANCEL_INDEX = -3;

    public interface OHCompactAlertDialogFragmentCallback {

        /**
         * On alert dialog button clicked.
         *
         * @param buttonIndex button index
         * @param dialogTag   dialog tag
         * @param payload     payload
         */
        void onAlertDialogFragmentButtonClicked(final int buttonIndex, final @NonNull String dialogTag, final @Nullable Object payload);
    }

    private static final String ARG_TITLE = "title";

    private static final String ARG_BUTTON1 = "button1";

    private static final String ARG_BUTTON2 = "button2";

    private static final String ARG_TAG = "tag";

    private static final String ARG_TEXT = "text";

    private static final String ARG_SELECTABLE_ITEMS = "selectableItems";

    private static final String ARG_SELECTABLE_ITEM_ICONS_RES = "selectableItemIconsRes";

    private static final String ARG_SELECTABLE_ITEMS_RES = "selectableItemsRes";

    private static final String ARG_OBJECT_S = "objectS";

    private static final String ARG_OBJECT_P = "objectP";

    private static final String ARG_CANCELLABLE = "cancelable";

    /**
     * Builder.
     */
    public static class OHCompactAlertDialogFragmentBuilder {

        private Resources mRes;

        String mButton1Text;

        String mButton2Text;

        String mTitle;

        String mMesssage;

        Serializable mObjectS;

        Parcelable mObjectP;

        int[] mSelectableItemsRes;

        int[] mSelectableItemIconsRes;

        CharSequence[] mSelectableItems;

        boolean mCancelable;

        public OHCompactAlertDialogFragmentBuilder(final Resources res) {
            mRes = res;
            mButton1Text = null;
            mButton2Text = null;
            mTitle = null;
            mMesssage = null;
            mObjectS = null;
            mObjectP = null;
            mCancelable = false;
        }

        public OHCompactAlertDialogFragmentBuilder button1Text(final String text) {
            mButton1Text = text;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder button1Text(final int res) {
            mButton1Text = mRes.getString(res);
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder button2Text(final String text) {
            mButton2Text = text;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder button2Text(final int res) {
            mButton2Text = mRes.getString(res);
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder selectableItemIconsRes(final int[] icons) {
            mSelectableItemIconsRes = icons;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder selectableItemsRes(final int[] items) {
            mSelectableItemsRes = items;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder selectableItems(final CharSequence[] items) {
            mSelectableItems = items;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder selectableItems(final int res) {
            mSelectableItems = mRes.getTextArray(res);
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder title(final String text) {
            mTitle = text;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder title(final int res) {
            mTitle = mRes.getString(res);
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder message(final String text) {
            mMesssage = text;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder message(final int res) {
            mMesssage = mRes.getString(res);
            return this;
        }


        public OHCompactAlertDialogFragmentBuilder object(final Serializable object) {
            mObjectS = object;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder object(final Parcelable object) {
            mObjectP = object;
            return this;
        }

        public OHCompactAlertDialogFragmentBuilder cancelable(final boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public OHCompactAlertDialogFragment show(FragmentManager manager, final String tag) {
            if(manager.findFragmentByTag(tag) == null) {
                return show(manager, tag, false);
            }
            return (OHCompactAlertDialogFragment) manager.findFragmentByTag(tag);
        }

        public OHCompactAlertDialogFragment showAllowingStateLoss(FragmentManager manager, final String tag) {
            if(manager.findFragmentByTag(tag) == null) {
                return show(manager, tag, true);
            }
            return (OHCompactAlertDialogFragment) manager.findFragmentByTag(tag);
        }

        private OHCompactAlertDialogFragment show(FragmentManager manager, final String tag, final boolean allowStateLoss) {
            final OHCompactAlertDialogFragment fragment;
            if (mObjectP == null) {
                fragment = OHCompactAlertDialogFragment.newInstance(tag, mTitle, mMesssage, mButton1Text, mButton2Text,
                        mSelectableItems, mSelectableItemsRes, mSelectableItemIconsRes, mObjectS, mCancelable);
            } else {
                fragment = OHCompactAlertDialogFragment.newInstance(tag, mTitle, mMesssage, mButton1Text, mButton2Text,
                        mSelectableItems, mSelectableItemsRes, mSelectableItemIconsRes, mObjectP, mCancelable);
            }
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(fragment, tag);
            if (allowStateLoss) {
                ft.commitAllowingStateLoss();
            } else {
                ft.commit();
            }
            return fragment;
        }
    }


    /**
     * @param tag                    tag of this dialog. can be used to identify the dialog
     * @param title                  title res id
     * @param text                   message res id
     * @param button1                button 1 text res id (on the right)
     * @param button2                button 2 text res id (on the left)
     * @param selectableItems
     * @param selectableItemsRes
     * @param selectableItemIconsRes
     * @param object                 additional serialized  object
     * @param cancelable             is dialog is cancelable when tapped outside
     * @return a new alert dialog fragment
     */
    private static OHCompactAlertDialogFragment newInstance(final String tag, final String title,
                                                            final String text, final String button1, final String button2,
                                                            final CharSequence[] selectableItems,
                                                            final int[] selectableItemsRes,
                                                            final int[] selectableItemIconsRes,
                                                            final Object object, final boolean cancelable) {
        final OHCompactAlertDialogFragment frag = new OHCompactAlertDialogFragment();
        frag.setCancelable(true);
        final Bundle args = new Bundle();
        args.putString(ARG_TAG, tag);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BUTTON1, button1);
        args.putString(ARG_BUTTON2, button2);
        args.putString(ARG_TEXT, text);
        args.putCharSequenceArray(ARG_SELECTABLE_ITEMS, selectableItems);
        args.putIntArray(ARG_SELECTABLE_ITEM_ICONS_RES, selectableItemIconsRes);
        args.putIntArray(ARG_SELECTABLE_ITEMS_RES, selectableItemsRes);
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

    private OHCompactAlertDialogFragmentCallback mCallback;

    public void setCallback(final OHCompactAlertDialogFragmentCallback callback) {
        mCallback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        final String title = arguments.getString(ARG_TITLE);
        final String button1 = arguments.getString(ARG_BUTTON1);
        final String button2 = arguments.getString(ARG_BUTTON2);
        final String text = arguments.getString(ARG_TEXT);
        final CharSequence[] selectableItems = arguments.getCharSequenceArray(ARG_SELECTABLE_ITEMS);
        final int[] selectableItemsRes = arguments.getIntArray(ARG_SELECTABLE_ITEMS_RES);
        final int[] selectableItemIconsRes = arguments.getIntArray(ARG_SELECTABLE_ITEM_ICONS_RES);
        final Object attachedObject;
        if (getArguments().containsKey(ARG_OBJECT_S)) {
            attachedObject = getArguments().getSerializable(ARG_OBJECT_S);
        } else {
            attachedObject = getArguments().getParcelable(ARG_OBJECT_P);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OHAlertDialogStyle);

        /* title is optional */
        if (title != null) {
            builder.setTitle(title);
        }

        /* button 1 is optinal */
        if (button1 != null) {
            builder.setPositiveButton(button1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallback == null) {
                        return;
                    }
                    mCallback.onAlertDialogFragmentButtonClicked(BUTTON_1_INDEX, getTag(), attachedObject);
                    mCallback = null;
                }
            });
        }

        /* button 2 is optional */
        if (button2 != null) {
            builder.setNegativeButton(button2, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mCallback == null) {
                        return;
                    }
                    mCallback.onAlertDialogFragmentButtonClicked(BUTTON_2_INDEX, getTag(), attachedObject);
                    mCallback = null;
                }
            });
        }
        if (selectableItems != null && selectableItemIconsRes != null) {
            /* has strings and icons */
            builder.setAdapter(new IconTitleListAdapter(selectableItemIconsRes, selectableItems),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mCallback == null) {
                                return;
                            }
                            mCallback.onAlertDialogFragmentButtonClicked(i, getTag(), attachedObject);
                            mCallback = null;
                        }
                    });
        } else if (selectableItemIconsRes != null && selectableItemsRes != null) {
        /* selectable items res and icons are optional */
            builder.setAdapter(new IconTitleListAdapter(selectableItemIconsRes, selectableItemsRes),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mCallback == null) {
                        return;
                    }
                    mCallback.onAlertDialogFragmentButtonClicked(i, getTag(), attachedObject);
                    mCallback = null;
                }
            });
        } else if (selectableItems != null) {
            /* only strings */
            builder.setItems(selectableItems, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (mCallback == null) {
                        return;
                    }
                    mCallback.onAlertDialogFragmentButtonClicked(i, getTag(), attachedObject);
                    mCallback = null;
                }
            });
        }

        /* message is optional */
        if (text != null) {
            builder.setMessage(text);
        }

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
        mCallback.onAlertDialogFragmentButtonClicked(BUTTON_CANCEL_INDEX, getTag(), attachedObject);
        mCallback = null;
    }

}
