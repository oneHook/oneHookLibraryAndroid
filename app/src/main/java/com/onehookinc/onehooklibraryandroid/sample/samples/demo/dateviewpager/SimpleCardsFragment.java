package com.onehookinc.onehooklibraryandroid.sample.samples.demo.dateviewpager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehookinc.onehooklibraryandroid.R;
import com.onehookinc.onehooklibraryandroid.sample.common.BaseListFragment;

public class SimpleCardsFragment extends BaseListFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list_no_toolbar, container, false);
    }

    public static SimpleCardsFragment newInstance() {
        return new SimpleCardsFragment();
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new CardsAdapter();
    }

    private static class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public CardViewHolder(final ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_rounded_corner, parent, false));
            mTextView = itemView.findViewById(R.id.cell_rounded_corner_textview);
        }
    }

    private static class CardsAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
