
package com.onehook.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @param <T>
 * @author EagleDiao
 */
public class RecyclerViewLoadingAdapter<T extends BaseRecyclerViewAdapter> extends
        BaseWrappedRecyclerViewAdapter<T> implements OnClickListener {

    private boolean mLoadingEnabled;

    /**
     * If the loading is triggered by clicking or auto.
     */
    private boolean mAutoTriggerLoading;

    /**
     * Loading state listener.
     */
    private OnLoadingListener mOnLoadingListener;

    final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    /**
     * @param wrappedAdapter
     */
    public RecyclerViewLoadingAdapter(T wrappedAdapter) {
        super(wrappedAdapter);

        mLoadingEnabled = true;
        mAutoTriggerLoading = true;

        setDataObserver(mObserver);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingView(position)) {
            return TYPE_VIEW_LOADING;
        } else {
            return mWrappedAdapter.getItemViewType(position);
        }
    }

    private boolean isLoadingView(int position) {
        return mLoadingEnabled && (position == getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        if (mLoadingEnabled) {
            return mWrappedAdapter.getItemCount() + 1;
        } else {
            return mWrappedAdapter.getItemCount();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (isLoadingView(position)) {
            @SuppressWarnings("unchecked")
            final LoadingViewHolder holder = (LoadingViewHolder) viewHolder;

            if (mAutoTriggerLoading) {
                notifyLoadingStarted(position);
            }

            if (holder != null) {
                if (mAutoTriggerLoading) {
                    holder.mProgressBar.setVisibility(View.VISIBLE);
                    holder.mLoadMoreText.setVisibility(View.INVISIBLE);
                } else {
                    holder.mProgressBar.setVisibility(View.INVISIBLE);
                    holder.mLoadMoreText.setVisibility(View.VISIBLE);
                }
            }
        } else {
            mWrappedAdapter.onBindViewHolder(viewHolder, position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW_LOADING) {
            final LoadingViewHolder holder = new LoadingViewHolder(LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.loading_view, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        } else {
            return mWrappedAdapter.createViewHolder(parent, viewType);
        }
    }

    private class LoadingViewHolder extends FlexGridViewHolder {

        public LoadingViewHolder(View view) {
            super(view, FlexGridViewHolder.SIZE_WRAP_CONTENT);
            mProgressBar = (ProgressBar) view.findViewById(R.id.view_loading_progress);
            mLoadMoreText = (TextView) view.findViewById(R.id.view_loading_load_more_text);
            view.setTag(R.id.loading_adapter_view_tag_key, this);
        }

        private ProgressBar mProgressBar;

        private TextView mLoadMoreText;
    }

    public void setLoading(boolean enabled) {
        mLoadingEnabled = enabled;
        notifyDataSetChanged();
    }

    public void setAutoLoading(boolean autoLoading) {
        mAutoTriggerLoading = autoLoading;
        notifyDataSetChanged();
    }

    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    public OnLoadingListener getOnLoadingListener() {
        return mOnLoadingListener;
    }

    private void notifyLoadingStarted(int position) {
        if (mOnLoadingListener != null) {
            mOnLoadingListener.onLoadNextPage(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (!mAutoTriggerLoading) {
            notifyLoadingStarted(getItemCount() - 1);
            mAutoTriggerLoading = true;
            /*
             * Start loading state.
             */
            final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.view_loading_progress);
            final TextView loadMoreText = (TextView) v
                    .findViewById(R.id.view_loading_load_more_text);
            progressBar.setVisibility(View.VISIBLE);
            loadMoreText.setVisibility(View.INVISIBLE);
        }
    }

    public static interface OnLoadingListener {
        void onLoadNextPage(int position);
    }
}
