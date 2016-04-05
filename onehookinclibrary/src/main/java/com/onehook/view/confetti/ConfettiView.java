package com.onehook.view.confetti;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-04-04.
 */
public class ConfettiView extends ViewGroup {

    public ConfettiView(Context context) {
        super(context);
    }

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    private static final long MINIMUM_DURATION = 2000;

    private static final long MAXIMUM_DURATION = 3000;

    private long generateRandomDuration() {
        return (long) Math.random() * (MAXIMUM_DURATION - MINIMUM_DURATION) + MINIMUM_DURATION;
    }

    public void start() {
        final float width = getMeasuredWidth();
        final float height = getMeasuredHeight();

        System.out.println("oneHook " + width + " , " + height);


        final ConfettiViewCell cell = new ConfettiViewCell(getContext());
        cell.setImageResource(R.drawable.ic_close_black_48dp);
        cell.layout(100, 100, 164, 164);
        addView(cell);

        cell.animate().translationY(getMeasuredHeight()).setInterpolator(new AccelerateInterpolator()).setDuration(generateRandomDuration());

    }


}
