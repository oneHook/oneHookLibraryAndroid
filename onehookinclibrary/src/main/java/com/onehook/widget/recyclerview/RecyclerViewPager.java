package com.onehook.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.onehookinc.androidlib.BuildConfig;
import com.onehookinc.androidlib.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerViewPager
 */
public class RecyclerViewPager extends RecyclerView {

    /**
     * RecyclerViewPager on Scroll listener.
     */
    public interface IRecyclerViewPagerOnScrollListener {

        void onRecyclerViewPagerScroll(final RecyclerViewPager pager, final float yOffset, final int currentChildIndex, final float progress);

        void onRecyclerViewPagerScrollEnd(final RecyclerViewPager pager, final float yOffset, final int currentChildIndex, final float progress);
    }

    public static final boolean DEBUG = BuildConfig.DEBUG;

    /**
     * Current scroll content offset y.
     */
    private float mCurrentScrollY;

    /**
     * Adapter wrapper.
     */
    private RecyclerViewPagerAdapter<?> mViewPagerAdapter;

    /**
     * Listener.
     */
    private WeakReference<IRecyclerViewPagerOnScrollListener> mPagerScrollListener;

    private float mTriggerOffset = 0.25f;

    private float mFlingFactor = 0.15f;

    private float mOverlapRatio = 0.10f;

    private float mOverlapAmount = -1;

    private float mTouchSpan;

    private List<OnPageChangedListener> mOnPageChangedListeners;

    private int mSmoothScrollTargetPosition = -1;

    private int mPositionBeforeScroll = -1;

    private boolean mSinglePageFling;

    boolean mNeedAdjust;

    int mFisrtLeftWhenDragging;

    int mFirstTopWhenDragging;

    View mCurView;

    int mMaxLeftWhenDragging = Integer.MIN_VALUE;

    int mMinLeftWhenDragging = Integer.MAX_VALUE;

    int mMaxTopWhenDragging = Integer.MIN_VALUE;

    int mMinTopWhenDragging = Integer.MAX_VALUE;

    private int mPositionOnTouchDown = -1;

    private boolean mHasCalledOnPageChanged = true;

    private boolean reverseLayout = false;

    /**
     * @param context context
     */
    public RecyclerViewPager(Context context) {
        this(context, null);
    }

    /**
     * @param context context
     * @param attrs   attributes
     */
    public RecyclerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context  context
     * @param attrs    attributes
     * @param defStyle style
     */
    public RecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs, defStyle);
        setNestedScrollingEnabled(false);
    }

    /**
     * @param context  context
     * @param attrs    attributes
     * @param defStyle defined style
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewPager, defStyle,
                0);
        mFlingFactor = a.getFloat(R.styleable.RecyclerViewPager_oh_flingFactor, mFlingFactor);
        mTriggerOffset = a.getFloat(R.styleable.RecyclerViewPager_oh_triggerOffset, mTriggerOffset);
        mSinglePageFling = a.getBoolean(R.styleable.RecyclerViewPager_oh_singlePageFling, mSinglePageFling);
        mOverlapRatio = a.getFloat(R.styleable.RecyclerViewPager_oh_overlap_ratio, mOverlapRatio);
        mOverlapAmount = a.getDimension(R.styleable.RecyclerViewPager_oh_overlap_amount, mOverlapAmount);
        a.recycle();
    }

    public void setFlingFactor(float flingFactor) {
        mFlingFactor = flingFactor;
    }

    public float getFlingFactor() {
        return mFlingFactor;
    }

    public void setTriggerOffset(float triggerOffset) {
        mTriggerOffset = triggerOffset;
    }

    public float getTriggerOffset() {
        return mTriggerOffset;
    }

    public void setSinglePageFling(boolean singlePageFling) {
        mSinglePageFling = singlePageFling;
    }

    public boolean isSinglePageFling() {
        return mSinglePageFling;
    }

    public int getOverlapOffset() {
        if (mOverlapAmount > 0) {
            return (int) mOverlapAmount;
        } else {
            return (int) (getMeasuredHeight() * mOverlapRatio);
        }
    }

    public float getOverlapRatio() {
        return mOverlapRatio;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        try {
            Field fLayoutState = state.getClass().getDeclaredField("mLayoutState");
            fLayoutState.setAccessible(true);
            Object layoutState = fLayoutState.get(state);
            Field fAnchorOffset = layoutState.getClass().getDeclaredField("mAnchorOffset");
            Field fAnchorPosition = layoutState.getClass().getDeclaredField("mAnchorPosition");
            fAnchorPosition.setAccessible(true);
            fAnchorOffset.setAccessible(true);
            if (fAnchorOffset.getInt(layoutState) > 0) {
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) - 1);
            } else if (fAnchorOffset.getInt(layoutState) < 0) {
                fAnchorPosition.set(layoutState, fAnchorPosition.getInt(layoutState) + 1);
            }
            fAnchorOffset.setInt(layoutState, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * OnScrollListener for present more detailed scroll info through listener.
     */
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE) {
                final int childHeight = getHeight() - getPaddingTop() - getPaddingBottom() - getOverlapOffset();
                final float currentChildIndexRaw = mCurrentScrollY / childHeight;
                final int currentChildIndex = (int) currentChildIndexRaw;
                final float progress = currentChildIndexRaw - currentChildIndex;
                if (mPagerScrollListener != null && mPagerScrollListener.get() != null) {
                    mPagerScrollListener.get().onRecyclerViewPagerScrollEnd(RecyclerViewPager.this, mCurrentScrollY, currentChildIndex, progress);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            final int childHeight = getHeight() - getPaddingTop() - getPaddingBottom() - getOverlapOffset();
            if (dy == 0) {
                mCurrentScrollY = getCurrentPosition() * childHeight;
            } else {
                mCurrentScrollY += dy;
            }

            final float currentChildIndexRaw = mCurrentScrollY / childHeight;
            final int currentChildIndex = (int) currentChildIndexRaw;
            final float progress = currentChildIndexRaw - currentChildIndex;
            if (mPagerScrollListener != null && mPagerScrollListener.get() != null) {
                mPagerScrollListener.get().onRecyclerViewPagerScroll(RecyclerViewPager.this, mCurrentScrollY, currentChildIndex, progress);
            }
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        mViewPagerAdapter = ensureRecyclerViewPagerAdapter(adapter);
        super.setAdapter(mViewPagerAdapter);
        if (adapter != null) {
            addOnScrollListener(mOnScrollListener);
        } else {
            removeOnScrollListener(mOnScrollListener);
        }
    }

    public void setViewPagerOnScrollListener(final IRecyclerViewPagerOnScrollListener listener) {
        mPagerScrollListener = new WeakReference<>(listener);
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        mViewPagerAdapter = ensureRecyclerViewPagerAdapter(adapter);
        super.swapAdapter(mViewPagerAdapter, removeAndRecycleExistingViews);
    }

    @Override
    public Adapter getAdapter() {
        if (mViewPagerAdapter != null) {
            return mViewPagerAdapter.mAdapter;
        }
        return null;
    }

    /**
     * Get the wrapper adapter.
     *
     * @return wrapper adapter
     */
    public RecyclerViewPagerAdapter getWrapperAdapter() {
        return mViewPagerAdapter;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        if (layout instanceof LinearLayoutManager) {
            reverseLayout = ((LinearLayoutManager) layout).getReverseLayout();
        }
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        boolean flinging = super.fling((int) (velocityX * mFlingFactor), (int) (velocityY * mFlingFactor));
        if (flinging) {
            if (getLayoutManager().canScrollHorizontally()) {
                adjustPositionX(velocityX);
            } else {
                adjustPositionY(velocityY);
            }
        }

        if (DEBUG) {
            Log.d("@", "velocityX:" + velocityX);
            Log.d("@", "velocityY:" + velocityY);
        }
        return flinging;
    }

    @Override
    public void smoothScrollToPosition(int position) {
        if (position >= getAdapter().getItemCount() || position < 0) {
            return;
        }
        mSmoothScrollTargetPosition = position;
        if (getLayoutManager() != null && getLayoutManager() instanceof LinearLayoutManager) {
            /*
             * exclude item decoration
             */
            final LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            if (getLayoutManager() == null) {
                                return null;
                            }
                            return ((LinearLayoutManager) getLayoutManager())
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        @Override
                        protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                            if (getLayoutManager() == null) {
                                return;
                            }
                            final int dx;
                            final int dy;
                            if (getLayoutManager().canScrollHorizontally()) {
                                /* horizontal */
                                dx = 0;
                                dy = 0;
                            } else {
                                dx = 0;
                                int targetTop = getLayoutManager().getDecoratedTop(targetView);
                                final int targetBottom = getLayoutManager().getDecoratedBottom(targetView);
                                final int targetHeight = targetBottom - targetTop;
//                                System.out.println("Target top : " + targetTop + " Target bottom : " + targetBottom + " , height " + targetHeight);
                                if (targetTop < 0 && Math.abs(targetTop) / 2 > targetHeight / 2) {
                                    while (targetTop < 0) {
                                        targetTop += targetHeight;
                                    }
                                }
                                if (targetTop > 0) {
                                    while (targetTop > targetHeight) {
                                        targetTop -= targetHeight;
                                    }
                                }
                                dy = -targetTop;
                            }

                            final int distance = (int) Math.sqrt(dx * dx + dy * dy);
                            final int time = calculateTimeForDeceleration(distance);
                            if (time > 0) {
                                action.update(-dx, -dy, time, mDecelerateInterpolator);
                            }
                        }
                    };
            linearSmoothScroller.setTargetPosition(position);
            getLayoutManager().startSmoothScroll(linearSmoothScroller);
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        mPositionBeforeScroll = getCurrentPosition();
        mSmoothScrollTargetPosition = position;
        super.scrollToPosition(position);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                if (mSmoothScrollTargetPosition >= 0 && mSmoothScrollTargetPosition < mViewPagerAdapter.getItemCount()) {
                    if (mOnPageChangedListeners != null) {
                        for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                            if (onPageChangedListener != null) {
                                onPageChangedListener.OnPageChanged(mPositionBeforeScroll, getCurrentPosition());
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * get item position in center of viewpager
     */
    public int getCurrentPosition() {
        int curPosition = -1;
        if (getLayoutManager().canScrollHorizontally()) {
            curPosition = ViewUtils.getCenterXChildPosition(this);
        } else {
            curPosition = ViewUtils.getCenterYChildPosition(this);
        }
        if (curPosition < 0) {
            curPosition = mSmoothScrollTargetPosition;
        }
        return curPosition;
    }

    /***
     * adjust position before Touch event complete and fling action start.
     */
    protected void adjustPositionX(int velocityX) {
        if (reverseLayout) velocityX *= -1;

        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterXChildPosition(this);
            int childWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            int flingCount = getFlingCount(velocityX, childWidth);
            int targetPosition = curPosition + flingCount;
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;
                if (DEBUG) {
                    Log.d("@", "flingCount:" + flingCount);
                    Log.d("@", "original targetPosition:" + targetPosition);
                }
            }
            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, mViewPagerAdapter.getItemCount() - 1);
            if (targetPosition == curPosition
                    && ((mSinglePageFling
                    && mPositionOnTouchDown == curPosition)
                    || !mSinglePageFling)) {
                View centerXChild = ViewUtils.getCenterXChild(this);
                if (centerXChild != null) {
                    if (mTouchSpan > centerXChild.getWidth() * mTriggerOffset * mTriggerOffset && targetPosition != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < centerXChild.getWidth() * -mTriggerOffset && targetPosition != mViewPagerAdapter.getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }
            if (DEBUG) {
                Log.d("@", "mTouchSpan:" + mTouchSpan);
                Log.d("@", "adjustPositionX:" + targetPosition);
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, mViewPagerAdapter.getItemCount()));
        }
    }

    public void addOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners == null) {
            mOnPageChangedListeners = new ArrayList<>();
        }
        mOnPageChangedListeners.add(listener);
    }

    public void removeOnPageChangedListener(OnPageChangedListener listener) {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.remove(listener);
        }
    }

    public void clearOnPageChangedListeners() {
        if (mOnPageChangedListeners != null) {
            mOnPageChangedListeners.clear();
        }
    }

    /***
     * adjust position before Touch event complete and fling action start.
     */
    protected void adjustPositionY(int velocityY) {
        if (reverseLayout) velocityY *= -1;

        int childCount = getChildCount();
        if (childCount > 0) {
            int curPosition = ViewUtils.getCenterYChildPosition(this);
            int childHeight = getHeight() - getPaddingTop() - getPaddingBottom() - getOverlapOffset();
            int flingCount = getFlingCount(velocityY, childHeight);
            int targetPosition = curPosition + flingCount;
            if (mSinglePageFling) {
                flingCount = Math.max(-1, Math.min(1, flingCount));
                targetPosition = flingCount == 0 ? curPosition : mPositionOnTouchDown + flingCount;
            }

            targetPosition = Math.max(targetPosition, 0);
            targetPosition = Math.min(targetPosition, mViewPagerAdapter.getItemCount() - 1);
            if (targetPosition == curPosition
                    && ((mSinglePageFling
                    && mPositionOnTouchDown == curPosition)
                    || !mSinglePageFling)) {
                View centerYChild = ViewUtils.getCenterYChild(this);
                if (centerYChild != null) {
                    if (mTouchSpan > (centerYChild.getHeight() - getOverlapOffset()) * mTriggerOffset && targetPosition != 0) {
                        if (!reverseLayout) targetPosition--;
                        else targetPosition++;
                    } else if (mTouchSpan < (centerYChild.getHeight() - getOverlapOffset()) *
                            -mTriggerOffset && targetPosition != mViewPagerAdapter.getItemCount() - 1) {
                        if (!reverseLayout) targetPosition++;
                        else targetPosition--;
                    }
                }
            }
            if (DEBUG) {
                Log.d("@", "mTouchSpan:" + mTouchSpan);
                Log.d("@", "adjustPositionY:" + targetPosition);
            }
            smoothScrollToPosition(safeTargetPosition(targetPosition, mViewPagerAdapter.getItemCount()));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mPositionOnTouchDown = getLayoutManager().canScrollHorizontally()
                    ? ViewUtils.getCenterXChildPosition(this)
                    : ViewUtils.getCenterYChildPosition(this);
            if (DEBUG) {
                Log.d("@", "mPositionOnTouchDown:" + mPositionOnTouchDown);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        /*
         * recording the max/min value in touch track
         */
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            if (mCurView != null) {
                mMaxLeftWhenDragging = Math.max(mCurView.getLeft(), mMaxLeftWhenDragging);
                mMaxTopWhenDragging = Math.max(mCurView.getTop(), mMaxTopWhenDragging);
                mMinLeftWhenDragging = Math.min(mCurView.getLeft(), mMinLeftWhenDragging);
                mMinTopWhenDragging = Math.min(mCurView.getTop(), mMinTopWhenDragging);
            }
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_DRAGGING) {
            mNeedAdjust = true;
            mCurView = getLayoutManager().canScrollHorizontally() ? ViewUtils.getCenterXChild(this) :
                    ViewUtils.getCenterYChild(this);
            if (mCurView != null) {
                if (mHasCalledOnPageChanged) {
                    // While rvp is scrolling, mPositionBeforeScroll will be previous value.
                    mPositionBeforeScroll = getChildLayoutPosition(mCurView);
                    mHasCalledOnPageChanged = false;
                }
                mFisrtLeftWhenDragging = mCurView.getLeft();
                mFirstTopWhenDragging = mCurView.getTop();
            } else {
                mPositionBeforeScroll = -1;
            }
            mTouchSpan = 0;
        } else if (state == SCROLL_STATE_SETTLING) {
            mNeedAdjust = false;
            if (mCurView != null) {
                if (getLayoutManager().canScrollHorizontally()) {
                    mTouchSpan = mCurView.getLeft() - mFisrtLeftWhenDragging;
                } else {
                    mTouchSpan = mCurView.getTop() - mFirstTopWhenDragging;
                }
            } else {
                mTouchSpan = 0;
            }
            mCurView = null;
        } else if (state == SCROLL_STATE_IDLE) {
            if (mNeedAdjust) {
                int targetPosition = getLayoutManager().canScrollHorizontally() ? ViewUtils.getCenterXChildPosition(this) :
                        ViewUtils.getCenterYChildPosition(this);
                if (mCurView != null) {
                    targetPosition = getChildAdapterPosition(mCurView);
                    if (getLayoutManager().canScrollHorizontally()) {
                        int spanX = mCurView.getLeft() - mFisrtLeftWhenDragging;
                        // if user is tending to cancel paging action, don't perform position changing
                        if (spanX > mCurView.getWidth() * mTriggerOffset && mCurView.getLeft() >= mMaxLeftWhenDragging) {
                            if (!reverseLayout) targetPosition--;
                            else targetPosition++;
                        } else if (spanX < mCurView.getWidth() * -mTriggerOffset && mCurView.getLeft() <= mMinLeftWhenDragging) {
                            if (!reverseLayout) targetPosition++;
                            else targetPosition--;
                        }
                    } else {
                        int spanY = mCurView.getTop() - mFirstTopWhenDragging;
                        if (spanY > mCurView.getHeight() * mTriggerOffset && mCurView.getTop() >= mMaxTopWhenDragging) {
                            if (!reverseLayout) targetPosition--;
                            else targetPosition++;
                        } else if (spanY < mCurView.getHeight() * -mTriggerOffset && mCurView.getTop() <= mMinTopWhenDragging) {
                            if (!reverseLayout) targetPosition++;
                            else targetPosition--;
                        }
                    }
                }
                int toTargetPosition = safeTargetPosition(targetPosition, mViewPagerAdapter.getItemCount());
                smoothScrollToPosition(toTargetPosition);
                mCurView = null;
            } else if (mSmoothScrollTargetPosition != mPositionBeforeScroll) {
                if (mOnPageChangedListeners != null) {
                    for (OnPageChangedListener onPageChangedListener : mOnPageChangedListeners) {
                        if (onPageChangedListener != null) {
                            onPageChangedListener.OnPageChanged(mPositionBeforeScroll, mSmoothScrollTargetPosition);
                        }
                    }
                }
                mHasCalledOnPageChanged = true;
                mPositionBeforeScroll = mSmoothScrollTargetPosition;
            }
            mMaxLeftWhenDragging = Integer.MIN_VALUE;
            mMinLeftWhenDragging = Integer.MAX_VALUE;
            mMaxTopWhenDragging = Integer.MIN_VALUE;
            mMinTopWhenDragging = Integer.MAX_VALUE;
        }
    }

    @NonNull
    protected RecyclerViewPagerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {
        return (adapter instanceof RecyclerViewPagerAdapter)
                ? (RecyclerViewPagerAdapter) adapter
                : new RecyclerViewPagerAdapter(this, adapter);

    }

    private int getFlingCount(int velocity, int cellSize) {
        if (velocity == 0) {
            return 0;
        }
        int sign = velocity > 0 ? 1 : -1;
        return (int) (sign * Math.ceil((velocity * sign * mFlingFactor / cellSize)
                - mTriggerOffset));
    }

    private int safeTargetPosition(int position, int count) {
        if (position < 0) {
            return 0;
        }
        if (position >= count) {
            return count - 1;
        }
        return position;
    }

    public interface OnPageChangedListener {
        void OnPageChanged(int oldPosition, int newPosition);
    }

}
