package com.onehookinc.onehooklibraryandroid.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseListFragment;

import java.util.ArrayList;

public class SampleListFragment extends BaseListFragment {

    private static final String ARG_PARENT = "argParent";

    public static SampleListFragment newInstance(final SampleItem itemParent) {
        final SampleListFragment fragment = new SampleListFragment();
        final Bundle args = new Bundle();
        args.putParcelable(ARG_PARENT, itemParent);
        fragment.setArguments(args);
        return fragment;
    }

    private SampleItem mParent;

    @Override
    public RecyclerView.Adapter createAdapter() {
        mParent = getArguments().getParcelable(ARG_PARENT);
        return new SampleListAdapter(mParent.getSubItems());
    }


    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sample_item,
                    parent, false));
            mTitleTextView = itemView.findViewById(R.id.cell_sample_item_title_textview);
        }

        private void bind(final SampleItem item) {
            mTitleTextView.setText(item.getTitle());
        }
    }

    private static class SampleListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        private ArrayList<SampleItem> mItems;

        public SampleListAdapter(final ArrayList<SampleItem> items) {
            mItems = items;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ItemViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            final SampleItem item = mItems.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

}
