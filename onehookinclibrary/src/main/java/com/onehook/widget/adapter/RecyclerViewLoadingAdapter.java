
package com.onehook.widget.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onehook.widget.FlexGridLayoutManager;
import com.onehookinc.androidlib.R;

/**
 * @param <T>
 * @author EagleDiao
 */
public class RecyclerViewLoadingAdapter<T extends BaseRecyclerViewAdapter> extends
        BaseWrappedRecyclerViewAdapter<T> implements OnClickListener {

    /**
     * If the loading is triggered by clicking or auto.
     */
    private boolean mAutoTriggerLoading;

    /**
     * Loading state listener.
     */
    private OnLoadingListener mOnLoadingListener;

    /**
     *
     */
    private String mCustomLoadMoreTextMessage;

    /**
     * Main thread handler.
     */
    private Handler mMainThreadHandler;

    /**
     * Observer for wrapped adapter.
     */
    final RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            notifyItemRangeChanged(positionStart, itemCount, payload);
            /*
             * for handling edge case that page size is small, make sure the bind view on loading
             * view is triggered.
             */
            notifyItemChanged(getItemCount() - 1);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
            /*
             * for handling edge case that page size is small, make sure the bind view on loading
             * view is triggered.
             */
            notifyItemChanged(getItemCount() - 1);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
            /*
             * for handling edge case that page size is small, make sure the bind view on loading
             * view is triggered.
             */
            notifyItemChanged(getItemCount() - 1);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
            /*
             * for handling edge case that page size is small, make sure the bind view on loading
             * view is triggered.
             */
            notifyItemChanged(getItemCount() - 1);
        }

    };

    /**
     * @param wrappedAdapter
     */
    public RecyclerViewLoadingAdapter(T wrappedAdapter) {
        super(wrappedAdapter);
        mAutoTriggerLoading = true;
        setDataObserver(mObserver);
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingView(position)) {
            return TYPE_VIEW_LOADING;
        } else {
            return mWrappedAdapter.getItemViewType(position);
        }
    }

    /**
     * Check if given position is the loading view.
     *
     * @param position adapter position
     * @return true if it is the loading view, false otherwise
     */
    private boolean isLoadingView(int position) {
        return position == getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        return mWrappedAdapter.getItemCount() + 1;
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

                if (mCustomLoadMoreTextMessage != null) {
                    holder.mLoadMoreText.setText(mCustomLoadMoreTextMessage);
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
                    parent.getContext()).inflate(R.layout.view_adapter_loading, parent, false));
            holder.itemView.setOnClickListener(this);
            return holder;
        } else {
            return mWrappedAdapter.createViewHolder(parent, viewType);
        }
    }

    /**
     * Loading view holder.
     */
    private class LoadingViewHolder extends FlexGridLayoutManager.FlexGridViewHolder {

        public LoadingViewHolder(View view) {
            super(view, FlexGridLayoutManager.FlexGridViewHolder.SIZE_WRAP_CONTENT);
            mProgressBar = view.findViewById(R.id.view_loading_progress);
            mLoadMoreText = view.findViewById(R.id.view_loading_load_more_text);
            view.setTag(R.id.loading_adapter_view_tag_key, this);
        }

        private View mProgressBar;

        private TextView mLoadMoreText;
    }

    /**
     * @param message
     */
    public void setLoadMoreText(final String message) {
        mCustomLoadMoreTextMessage = message;
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * Set should start auto loading.
     *
     * @param autoLoading should auto loading
     */
    public void setAutoLoading(boolean autoLoading) {
        if (mAutoTriggerLoading != autoLoading) {
            mAutoTriggerLoading = autoLoading;
            notifyItemChanged(getItemCount() - 1);
        }
    }

    /**
     * @param customMessage
     */
    public void setEndOfPage(final String customMessage) {
        mAutoTriggerLoading = false;
        mCustomLoadMoreTextMessage = customMessage;
        notifyItemChanged(getItemCount() - 1);
    }

    /**
     * @param onLoadingListener
     */
    public void setOnLoadingListener(OnLoadingListener onLoadingListener) {
        mOnLoadingListener = onLoadingListener;
    }

    /**
     * Return on loading listener.
     *
     * @return on loading listener
     */
    public OnLoadingListener getOnLoadingListener() {
        return mOnLoadingListener;
    }

    /**
     * @param position
     */
    private void notifyLoadingStarted(int position) {
        final int positionToNotify = position;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                if (mOnLoadingListener != null) {
                    mOnLoadingListener.onLoadNextPage(positionToNotify);
                }
            }
        };
        mMainThreadHandler.post(r);
    }

    @Override
    public void onClick(View v) {
        if (v.getVisibility() != View.VISIBLE) {
            return;
        }
        if (!mAutoTriggerLoading) {
            notifyLoadingStarted(getItemCount() - 1);
            mAutoTriggerLoading = true;
            /*
             * Start loading state.
             */
            final View progressBar = v.findViewById(R.id.view_loading_progress);
            final TextView loadMoreText = (TextView) v
                    .findViewById(R.id.view_loading_load_more_text);
            progressBar.setVisibility(View.VISIBLE);
            loadMoreText.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnLoadingListener {
        void onLoadNextPage(int position);
    }
}
