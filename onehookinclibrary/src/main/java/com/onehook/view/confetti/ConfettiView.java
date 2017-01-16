package com.onehook.view.confetti;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.onehook.util.animator.AnimationEndListener;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by EagleDiao on 2016-04-04.
 */
public class ConfettiView extends ViewGroup {

    private static final long MINIMUM_DURATION = 2000;

    private static final long MAXIMUM_DURATION = 3000;

    private static final int DEFAULT_CONFETTI_COUNT = 30;

    private static final int DEFAULT_CONFETTI_DELAY_RATIO = 8;

    private static final int DEFAULT_CONFETTI_SIZE = 300;

    public interface IConfettiViewCustomizationListener {

        void customizeConfettiCell(final ConfettiViewCell cell, final int index);
    }

    public ConfettiView(Context context) {
        super(context);
        commonInit();
    }

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit();
    }

    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit();
    }

    @TargetApi(21)
    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit();
    }

    private ArrayList<ConfettiViewCell> mConfettiViewCells;

    private int mConfettiCellCount = DEFAULT_CONFETTI_COUNT;

    private int mConfettiSize = DEFAULT_CONFETTI_SIZE;

    private int mConfettiDelayRatio = DEFAULT_CONFETTI_DELAY_RATIO;

    private void commonInit() {
        mConfettiViewCells = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void start() {
        start(null);
    }

    public void start(final IConfettiViewCustomizationListener customizationListener) {
        if (mConfettiViewCells != null && mConfettiViewCells.size() > 0) {
            return;
        }

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        mConfettiSize = Math.min(width, height) / 10;

        for (int i = 0; i < mConfettiCellCount; i++) {
            final ConfettiViewCell cell = new ConfettiViewCell(getContext());
            cell.measure(MeasureSpec.makeMeasureSpec(mConfettiSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mConfettiSize, MeasureSpec.EXACTLY));
            cell.layout(width / 2 - mConfettiSize / 2, -mConfettiSize, width / 2 + mConfettiSize / 2, 0);
            addView(cell);
            mConfettiViewCells.add(cell);
        }

        final Collection<Animator> animators = new ArrayList<>();

        for (int i = 0; i < mConfettiViewCells.size(); i++) {
            final ConfettiViewCell cell = mConfettiViewCells.get(i);
            if (customizationListener != null) {
                customizationListener.customizeConfettiCell(cell, i);
            }
            final long duration = generateRandomDuration();
            final long delay = generateRandomDelay(i);
            final int endingX = generateEndingX();
            final int controlX = (endingX > width / 2) ? width : 0;
            final int endingY = height + mConfettiSize;
            final int controlY = (int) (Math.random() * endingY);

            final Path path = new Path();
            path.moveTo(width / 2, -mConfettiSize);
            path.quadTo(controlX, controlY, endingX, endingY);

            final ValueAnimator pathAnimator = ValueAnimator.ofFloat(1.0f, 1.0f);
            pathAnimator.setDuration(duration);
            pathAnimator.setInterpolator(new AccelerateInterpolator());
            final PathMeasure pathMeasure = new PathMeasure(path, false);
            pathAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                float[] point = new float[2];

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = animation.getAnimatedFraction();
                    pathMeasure.getPosTan(pathMeasure.getLength() * val, point, null);
                    cell.setX(point[0]);
                    cell.setY(point[1]);
                }
            });

            final ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(cell, "rotation", 0f, 360f);
            rotationAnimator.setDuration(duration);

            final AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(rotationAnimator, pathAnimator);
            animatorSet.setStartDelay(delay);
            animators.add(animatorSet);
        }

        final AnimatorSet allSets = new AnimatorSet();
        allSets.playTogether(animators);
        allSets.start();
        allSets.addListener(new AnimationEndListener() {
            @Override
            public void onAnimationEndOrCanceled(Animator animation) {
                for (View view : mConfettiViewCells) {
                    removeView(view);
                }
                mConfettiViewCells.clear();
            }
        });

    }

    private long generateRandomDuration() {
        return (long) (Math.random() * (MAXIMUM_DURATION - MINIMUM_DURATION) + MINIMUM_DURATION);
    }

    private long generateRandomDelay(final int index) {
        return (long) ((index / mConfettiDelayRatio) * 500);
    }

    private int generateEndingX() {
        return (int) (getMeasuredWidth() * Math.random());
    }
}
