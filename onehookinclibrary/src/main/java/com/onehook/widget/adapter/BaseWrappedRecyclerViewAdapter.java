
package com.onehook.widget.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * @author EagleDiao
 */
public abstract class BaseWrappedRecyclerViewAdapter<T extends BaseRecyclerViewAdapter> extends BaseRecyclerViewAdapter {

    final T mWrappedAdapter;
    private RecyclerView.AdapterDataObserver mObserver;

    public BaseWrappedRecyclerViewAdapter(T adapter) {
        mWrappedAdapter = adapter;
    }

    final public void onResume() {
        if (mWrappedAdapter != null && mObserver != null) {
            mWrappedAdapter.registerAdapterDataObserver(mObserver);
        }

        if (mWrappedAdapter != null && mWrappedAdapter instanceof BaseWrappedRecyclerViewAdapter) {
            ((BaseWrappedRecyclerViewAdapter) mWrappedAdapter).onResume();
        }
    }

    final public void onPause() {
        if (mWrappedAdapter != null && mObserver != null) {
            mWrappedAdapter.unregisterAdapterDataObserver(mObserver);
        }

        if (mWrappedAdapter != null && mWrappedAdapter instanceof BaseWrappedRecyclerViewAdapter) {
            ((BaseWrappedRecyclerViewAdapter) mWrappedAdapter).onPause();
        }
    }

    public void setDataObserver(RecyclerView.AdapterDataObserver observer) {
        mObserver = observer;
    }

    final public T getWrappedAdapter() {
        return mWrappedAdapter;
    }
}
