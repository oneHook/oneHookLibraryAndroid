package com.onehookinc.onehooklibraryandroid.sample;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class SampleItem implements Parcelable {

    public enum SampleItemType {
        CATEGORY,

        /* View */
        STACK_LAYOUT,

        /* Utility */
        DEVICE
    }

    @NonNull
    private String mName;

    private SampleItemType mType;

    @Nullable
    private ArrayList<SampleItem> mSubItems;

    public SampleItem(@NonNull final String name,
                      SampleItemType type) {
        this(name, type, null);
    }

    public SampleItem(@NonNull final String name,
                      @Nullable ArrayList<SampleItem> subItems) {
        this(name, SampleItemType.CATEGORY, subItems);
    }

    public SampleItem(@NonNull final String name,
                      @NonNull SampleItem... subItems) {
        this(name, SampleItemType.CATEGORY, new ArrayList<>(Arrays.asList(subItems)));
    }

    public SampleItem(@NonNull final String name,
                      SampleItemType type,
                      @Nullable ArrayList<SampleItem> subItems) {
        mName = name;
        mType = type;
        mSubItems = subItems;
    }

    @Nullable
    public ArrayList<SampleItem> getSubItems() {
        return mSubItems;
    }

    @NonNull
    public String getTitle() {
        return mName;
    }

    public SampleItemType getType() {
        return mType;
    }

    protected SampleItem(Parcel in) {
        mName = in.readString();
        mType = (SampleItemType) in.readSerializable();
        if (in.readByte() == 0x01) {
            mSubItems = new ArrayList<>();
            in.readList(mSubItems, SampleItem.class.getClassLoader());
        } else {
            mSubItems = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeSerializable(mType);
        if (mSubItems == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSubItems);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SampleItem> CREATOR = new Parcelable.Creator<SampleItem>() {
        @Override
        public SampleItem createFromParcel(Parcel in) {
            return new SampleItem(in);
        }

        @Override
        public SampleItem[] newArray(int size) {
            return new SampleItem[size];
        }
    };
}