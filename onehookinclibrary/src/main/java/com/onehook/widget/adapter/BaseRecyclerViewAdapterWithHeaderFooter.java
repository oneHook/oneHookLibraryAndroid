
package com.onehook.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

/**
 * A Wrapper class that wrap a base adapter to provide header and footer.
 * 
 * @author EagleDiao
 * @param <T> wrapped base recycler view adapter
 */
public abstract class BaseRecyclerViewAdapterWithHeaderFooter<T extends BaseRecyclerViewAdapter>
        extends BaseWrapperRecyclerViewAdapter<T> {

    final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart + getHeaderViewCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart + getHeaderViewCount(), itemCount);
        }
    };
    /**
     * Create a new instance of this class by providing wrapped adapter.
     * 
     * @param wrapped wrapped adapter
     */
    public BaseRecyclerViewAdapterWithHeaderFooter(T wrapped) {
        super(wrapped);
        setDataObserver(mObserver);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderViewCount()) {
            /*
             * All Header view type should starts from TYPE_HEADER_VIEW_START
             */
            return TYPE_HEADER_VIEW_START + getHeaderViewType(position);
        } else if (position >= getItemCount() - getFooterViewCount()) {
            /*
             * All Footer view type should starts from TYPE_FOOTER_VIEW_START
             */
            return TYPE_FOOTER_VIEW_START
                    + getFooterViewType(position - mWrappedAdapter.getItemCount()
                            - getHeaderViewCount());
        } else {
            /*
             * Wrapped adapter decide the view type
             */
            return mWrappedAdapter.getItemViewType(position - getHeaderViewCount());
        }
    }

    @Override
    public int getItemCount() {
        return mWrappedAdapter.getItemCount() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int type = getItemViewType(position);
        if (type >= TYPE_HEADER_VIEW_START && type < TYPE_FOOTER_VIEW_START) {
            onBindHeaderViewHolder(viewHolder, position, type - TYPE_HEADER_VIEW_START);
        } else if (type >= TYPE_FOOTER_VIEW_START) {
            onBindFooterViewHolder(viewHolder, position, type - TYPE_FOOTER_VIEW_START);
        } else {
            mWrappedAdapter.onBindViewHolder(viewHolder, position - getHeaderViewCount());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type >= TYPE_HEADER_VIEW_START && type < TYPE_FOOTER_VIEW_START) {
            return onCreateHeaderViewHolder(parent, type - TYPE_HEADER_VIEW_START);
        } else if (type >= TYPE_FOOTER_VIEW_START) {
            return onCreateFooterViewHolder(parent, type - TYPE_FOOTER_VIEW_START);
        }
        return mWrappedAdapter.onCreateViewHolder(parent, type);
    }

    /**
     * @param position position of the header view, 0 to header view count
     * @return type of the header view, 0 to header view count
     */
    public int getHeaderViewType(final int position) {
        return position;
    }

    /**
     * @param position position of the footer view, 0 to footer view count
     * @return type of the footer view, 0 to footer view count
     */
    public int getFooterViewType(final int position) {
        return position;
    }

    /**
     * By default, there is no header, child class can override this function to
     * provide header.
     * 
     * @return number of header view
     */
    public int getHeaderViewCount() {
        return 0;
    }

    /**
     * By default, there is no footer, child class can override this function to
     * provide footer.
     * 
     * @return number of footer view
     */
    public int getFooterViewCount() {
        return 0;
    }

    /**
     * @param parent parent view
     * @param type view type, view type will be a number between 0 and total
     *            number of header view
     * @return header view holder with given type
     */
    public ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int type) {
        return null;
    }

    /**
     * @param parent parent view
     * @param type view type, view type will be a number between 0 and total
     *            number of footer view
     * @return footer view holder with given type
     */
    public ViewHolder onCreateFooterViewHolder(ViewGroup parent, int type) {
        return null;
    }

    /**
     * @param holder view holder
     * @param position position in adapter
     * @param type view type, view type will be a number between 0 and total
     *            number of header view
     */
    public void onBindHeaderViewHolder(ViewHolder holder, int position, int type) {

    }

    /**
     * @param holder view holder
     * @param position position in adapter
     * @param type view type, view type will be a number between 0 and total
     *            number of footer view
     */
    public void onBindFooterViewHolder(ViewHolder holder, int position, int type) {

    }
}
