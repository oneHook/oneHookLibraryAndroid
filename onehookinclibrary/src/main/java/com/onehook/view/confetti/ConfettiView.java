package com.onehook.view.confetti;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.onehook.util.animator.AnimationEndListener;
import com.onehookinc.androidlib.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Eagle Diao on 2016-04-04.
 */
public class ConfettiView extends ViewGroup {

    private static final String TAG = "OHConfettiView";
    private static final boolean DEBUG = true;

    public interface IConfettiViewCustomizationListener {
        View createConfettiCell(final ConfettiCellBuilder builder, final int index);
    }

    public ConfettiView(Context context) {
        super(context);
        commonInit(context, null);
    }

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    @TargetApi(21)
    public ConfettiView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        commonInit(context, attrs);
    }

    private int mMinDuration = 1500;
    private int mMaxDuration = 3000;
    private int mConfettiCellCount = 50;
    private int mConfettiSize = 64;
    private int mConfettiDelayRatio = 8;

    private ArrayList<View> mConfettiViewCells;

    /**
     * Common Init.
     *
     * @param context context
     * @param attrs   attributes
     */
    private void commonInit(@NonNull final Context context,
                            @Nullable final AttributeSet attrs) {
        mConfettiViewCells = new ArrayList<>();
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ConfettiView);
            mMinDuration = a.getInteger(R.styleable.ConfettiView_oh_confetti_view_min_duration,
                    mMinDuration);
            mMaxDuration = a.getInteger(R.styleable.ConfettiView_oh_confetti_view_max_duration,
                    mMaxDuration);
            mConfettiCellCount = a.getInteger(R.styleable.ConfettiView_oh_confetti_view_count,
                    mConfettiCellCount);
            mConfettiSize = a.getDimensionPixelSize(R.styleable.ConfettiView_oh_confetti_view_size,
                    mConfettiSize);
            mConfettiDelayRatio = a.getInteger(R.styleable.ConfettiView_oh_confetti_view_delay,
                    mConfettiDelayRatio);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        /* do nothing */
    }

    public void start(@NonNull final IConfettiViewCustomizationListener customizationListener) {
        if (mConfettiViewCells != null && mConfettiViewCells.size() > 0) {
            return;
        }

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        mConfettiSize = Math.min(width, height) / 10;

        final ConfettiCellBuilder builder = new ConfettiCellBuilder(getContext());
        for (int i = 0; i < mConfettiCellCount; i++) {
            final View cell = customizationListener.createConfettiCell(builder, i);

            if (cell instanceof TextView) {
                ((TextView) cell).setTextSize(mConfettiSize / 4);
            }

            cell.measure(MeasureSpec.makeMeasureSpec(mConfettiSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mConfettiSize, MeasureSpec.EXACTLY));
            cell.layout(generateRandomX(), -mConfettiSize, width / 2 + mConfettiSize / 2, 0);
            addView(cell);
            mConfettiViewCells.add(cell);
        }

        final Collection<Animator> animators = new ArrayList<>();

        for (int i = 0; i < mConfettiViewCells.size(); i++) {
            final View cell = mConfettiViewCells.get(i);
            final long duration = generateRandomDuration();
            final long delay = generateRandomDelay(i);
            final int endingX = generateRandomX();
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

            final ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(cell,
                    "rotation",
                    0f,
                    360f);
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

                if (DEBUG) {
                    Log.d(TAG, "confetti finished");
                }
            }
        });

    }

    /**
     * Helper function to generate a random drop down time.
     *
     * @return drop time
     */
    private long generateRandomDuration() {
        return (long) (Math.random() * (mMaxDuration - mMinDuration) + mMinDuration);
    }

    /**
     * Helper function to generate a random delay time for given confetti.
     *
     * @param index index of confetti
     * @return random delay time
     */
    private long generateRandomDelay(final int index) {
        return (long) ((Math.random() * mConfettiDelayRatio) * 500);
    }

    /**
     * Generate a start x position.
     *
     * @return random x position
     */
    private int generateRandomX() {
        return (int) (getMeasuredWidth() * Math.random());
    }

    /* Public accessors */

    public boolean isAnimating() {
        return mConfettiViewCells != null && mConfettiViewCells.size() > 0;
    }
}
