
package com.onehook.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * @author EagleDiao
 */
public abstract class BaseWebViewFragment extends Fragment {

    /**
     * Web view.
     */
    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWebView = new WebView(getActivity());
        mWebView.loadUrl(getUrl());
        setupWebView(mWebView);
        return mWebView;
    }

    /**
     * Called by the fragment's holder activity to handle a back navigation
     * within the webview back stack, if possible.
     *
     * @return Whether the webview was able to handle the back navigation, i.e.
     * true if navigating back within the back stack, false if there is
     * nothing in the back stack (root)
     */
    public boolean handleBack() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    /**
     * Produce the URL need to be loaded by webview.
     *
     * @return URL to be loaded
     */
    public abstract String getUrl();

    /**
     * Additional setup on web view
     *
     * @param webView additional setup on webview
     */
    public abstract void setupWebView(WebView webView);

}
