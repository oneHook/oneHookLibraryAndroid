package com.onehook.view.touchevent;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

/**
 * Created by EagleDiao on 2016-03-22.
 */
public abstract class TouchEventGesture implements View.OnTouchListener {

    private float mStartY;

    private float mStartX;

    private float mLastX;

    private float mLastY;

    private float mTotalDisplacement;

    private VelocityTracker mVelocityTracker = null;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final float x = motionEvent.getRawX();
        final float y = motionEvent.getRawY();

        /* hack ! */
        motionEvent.setLocation(x, y);

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
              /* setup velocity tracker */
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
        } else {
            mVelocityTracker.addMovement(motionEvent);
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                mLastX = x;
                mLastY = y;
                mTotalDisplacement = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final float xDelta = x - mLastX;
                final float yDelta = y - mLastY;
                mLastX = x;
                mLastY = y;
                mTotalDisplacement += Math.sqrt(xDelta * xDelta + yDelta * yDelta);
                onDrag(mStartX, mStartY, x, y, xDelta, yDelta);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mTotalDisplacement < 25) {
                    /* treat as a click */
                    onClicked(x, y);
                } else {
                    onDragUp(mStartX, mStartY, x, y, x - mStartX, y - mStartY, mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                }
            default:
                break;

        }
        return true;
    }


    public abstract void onDrag(final float startX, final float startY, final float rawX,
                                final float rawY, final float xDelta, final float yDelta);

    public abstract void onClicked(final float clickX, final float clickY);

    public abstract void onDragUp(final float startX, final float startY, final float rawX, final float rawY,
                                  final float xOffset, final float yOffset, final float velocityX, final float velocityY);
}
