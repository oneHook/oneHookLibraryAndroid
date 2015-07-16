package com.onehook.widget.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by eaglediaotomore on 15-07-05.
 */
public interface OnItemClickListener {

    void onItemClicked(final RecyclerView.ViewHolder viewHolder, final int position);
}
