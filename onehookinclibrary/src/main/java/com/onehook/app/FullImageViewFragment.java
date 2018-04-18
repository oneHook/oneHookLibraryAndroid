package com.onehook.app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.onehook.thirdparty.TouchImageView;
import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-06-09.
 */

public abstract class FullImageViewFragment extends Fragment {

    /**
     * URI arg.
     */
    private static final String ARG_URI = "URI";

    /**
     * URL arg.
     */
    private static final String ARG_URL = "URL";

    /**
     * Image view.
     */
    private TouchImageView mImageView;

    /**
     * Loading view switcher.
     */
    private View mLoadingView;

    protected static Bundle buildArguements(final Uri uri) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        return args;
    }

    protected static Bundle buildArguements(final String url) {
        final Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        return args;
    }

    public abstract View createLoadingView(final ViewGroup parent, final LayoutInflater inflater);

    public abstract void onLoadingViewVisibleChanged(final View loadingView, final boolean visible);

    public abstract void loadImage(final ImageView view, final String url);

    public abstract void loadImage(final ImageView view, final Uri uri);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_full_image_view, container, false);
        mImageView = (TouchImageView) view.findViewById(R.id.fragment_full_image_view_image_view);
        mLoadingView = createLoadingView(container, inflater);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        final FrameLayout.LayoutParams loadingViewLP = (FrameLayout.LayoutParams) mLoadingView.getLayoutParams();
        if (loadingViewLP != null) {
            lp.width = loadingViewLP.width;
            lp.height = loadingViewLP.height;
        }
        lp.gravity = Gravity.CENTER;
        mLoadingView.setLayoutParams(lp);
        view.addView(mLoadingView);
        doLoadImage();

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    protected void onImageLoaded() {
        mLoadingView.setVisibility(View.GONE);
        onLoadingViewVisibleChanged(mLoadingView, false);
    }

    private void doLoadImage() {
        mLoadingView.setVisibility(View.VISIBLE);
        onLoadingViewVisibleChanged(mLoadingView, true);

        final Uri uri = getArguments().getParcelable(ARG_URI);
        if (uri != null) {
            loadImage(mImageView, uri);
            return;
        }
        final String url = getArguments().getString(ARG_URL);
        if (url != null) {
            loadImage(mImageView, url);
            return;
        }
    }
}
