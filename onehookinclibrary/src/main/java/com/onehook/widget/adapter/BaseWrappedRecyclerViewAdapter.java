
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

    final public void onStart() {
        if (mWrappedAdapter != null && mObserver != null) {
            mWrappedAdapter.registerAdapterDataObserver(mObserver);
        }

        if (mWrappedAdapter != null && mWrappedAdapter instanceof BaseWrappedRecyclerViewAdapter) {
            ((BaseWrappedRecyclerViewAdapter) mWrappedAdapter).onStart();
        }
    }

    final public void onStop() {
        if (mWrappedAdapter != null && mObserver != null) {
            mWrappedAdapter.unregisterAdapterDataObserver(mObserver);
        }

        if (mWrappedAdapter != null && mWrappedAdapter instanceof BaseWrappedRecyclerViewAdapter) {
            ((BaseWrappedRecyclerViewAdapter) mWrappedAdapter).onStop();
        }
    }

    public void setDataObserver(RecyclerView.AdapterDataObserver observer) {
        mObserver = observer;
    }

    final public T getWrappedAdapter() {
        return mWrappedAdapter;
    }
}
