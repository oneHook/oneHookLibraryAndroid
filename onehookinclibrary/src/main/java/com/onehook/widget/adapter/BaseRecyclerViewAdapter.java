
package com.onehook.widget.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * @author EagleDiao
 */
public abstract class BaseRecyclerViewAdapter<T extends ViewHolder> extends Adapter<T> {

    /**
     * Default view type for loading view.
     */
    public static final int TYPE_VIEW_LOADING = -1;

    /**
     * All header view type starts from 100 up to 200 when we manage them
     * internally, to users, they are still number from 0 to total number of
     * headers.
     */
    public static final int TYPE_HEADER_VIEW_START = 100;

    /**
     * All footer view type starts from 200, to users, they are still a number
     * from 0 to total number of footer.
     */
    public static final int TYPE_FOOTER_VIEW_START = 200;
}
