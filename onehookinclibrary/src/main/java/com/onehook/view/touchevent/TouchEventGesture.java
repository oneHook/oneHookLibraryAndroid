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
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        System.out.println("oneHook MOTION EVENT:  " + motionEvent.getAction() + " y " + motionEvent.getY());
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                mStartY = y;
                mLastX = x;
                mLastY = y;
                mTotalDisplacement = 0;

                /* setup velocity tracker */
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(motionEvent);

                final float xDelta = x - mLastX;
                final float yDelta = y - mLastY;
                final float xOffset = x - mStartX;
                final float yOffset = y - mStartY;
                mLastX = x;
                mLastY = y;
                mTotalDisplacement += Math.sqrt(xDelta * xDelta + yDelta * yDelta);
                onDrag(mStartX, mStartY, x, y, xOffset, yOffset);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.addMovement(motionEvent);
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mTotalDisplacement < 25) {
                            /* treat as a click */
                    onClicked(motionEvent.getX(), motionEvent.getY());
                } else {
                    onDragUp(mStartX, mStartY, x, y, x - mStartX, y - mStartY, mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                }
            default:
                break;

        }
        return true;
    }


    public abstract void onDrag(final float startX, final float startY, final float currentX,
                                final float currentY, final float xOffset, final float yOffset);

    public abstract void onClicked(final float clickX, final float clickY);

    public abstract void onDragUp(final float startX, final float startY, final float currentX, final float currentY,
                                  final float xOffset, final float yOffset, final float velocityX, final float velocityY);
}
