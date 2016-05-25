package com.onehook.view.overlay;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.onehook.util.animator.AnimationEndListener;

/**
 * Created by EagleDiao on 2016-05-17.
 */
public class ActivityOverlayView extends FrameLayout {

    public ActivityOverlayView(Context context) {
        super(context);
        commonInit();
    }

    public ActivityOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public ActivityOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public ActivityOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private View mContentView;

    private void setContentView(final View view) {
        mContentView = view;
        addView(view);
    }

    private void commonInit() {
        setFitsSystemWindows(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mContentView == null) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mContentView == null) {
            return super.onTouchEvent(event);
        }
        if (mContentView.onTouchEvent(event)) {
            return false;
        } else {
            return true;
        }
    }

    private void hideContent() {
        mContentView.setAlpha(0);
    }

    private void showContent() {
        mContentView.animate().alpha(1.0f).setDuration(300).start();
    }

    public void destroy() {
        mContentView.animate().alpha(0).setDuration(150).setListener(new AnimationEndListener() {
            @Override
            public void onAnimationEndOrCanceled(Animator animation) {
                animate().alpha(0)
                        .setDuration(300)
                        .setListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEndOrCanceled(Animator animation) {
                                removeView(mContentView);
                                mContentView = null;
                            }
                        })
                        .start();
            }
        }).start();

    }

    public static class Builder {

        private ActivityOverlayView mOverlayView;

        private Activity mActivity;

        private RevealStyle mRevealStyle;

        private long mRevealDuration;

        private enum RevealStyle {
            FADE_IN
        }

        public Builder(final Activity activity) {
            mOverlayView = new ActivityOverlayView(activity);
            mActivity = activity;
        }

        public Builder backgroundColor(int color) {
            mOverlayView.setBackgroundColor(color);
            return this;
        }

        public Builder backgroundResource(int drawableRes) {
            mOverlayView.setBackgroundResource(drawableRes);
            return this;
        }

        public Builder fadein(final long duration) {
            mRevealStyle = RevealStyle.FADE_IN;
            mRevealDuration = duration;
            return this;
        }

        public Builder contentRes(final int res) {
            final LayoutInflater inflater = LayoutInflater.from(mOverlayView.getContext());
            final View view = inflater.inflate(res, mOverlayView, false);
            mOverlayView.setContentView(view);
            return this;
        }

        public ActivityOverlayView build() {
            final ActivityOverlayView rv = mOverlayView;
            insertOverlayView();
            mOverlayView = null;
            mActivity = null;
            return rv;
        }

        private void insertOverlayView() {
            final ActivityOverlayView overlayView = mOverlayView;
            mOverlayView.hideContent();
            ((ViewGroup) mActivity.getWindow().getDecorView()).addView(overlayView);
            if (mRevealStyle == RevealStyle.FADE_IN) {
                overlayView.setAlpha(0.0f);
                overlayView.animate()
                        .alpha(1.0f)
                        .setListener(new AnimationEndListener() {
                            @Override
                            public void onAnimationEndOrCanceled(Animator animation) {
                                overlayView.showContent();
                            }
                        })
                        .setDuration(mRevealDuration)
                        .start();
            } else {
                overlayView.showContent();
            }
        }
    }
}
