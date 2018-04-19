package com.onehook.view;

/**
 * Created by EagleDiao on 2016-09-06.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.onehookinc.androidlib.R;

/**
 * Implementation of an easy vertical SeekBar, based on the normal SeekBar.
 */
public class VerticalSeekBar extends AppCompatSeekBar {
    private boolean fromUser = false;
    private boolean onlySlideOnThumb = false;
    private Drawable customThumb = null;
    protected Drawable initialCustomThumb = null;

    private boolean wasDownOnThumb = false;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray customAttr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.VerticalSeekBar,
                0, 0);

        boolean onlyThumb = customAttr.getBoolean(R.styleable.VerticalSeekBar_onlyThumb, false);
        initialCustomThumb = customAttr.getDrawable(R.styleable.VerticalSeekBar_customThumb);

        setOnlyThumb(onlyThumb);
        setCustomThumb(initialCustomThumb);
        setThumb(getResources().getDrawable(android.R.color.transparent));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
        drawThumb(c);
    }

    private void drawThumb(Canvas canvas) {
        Drawable customThumb = getCustomThumb();

        if (customThumb != null) {

            int available = getHeight() - getPaddingTop() - getPaddingBottom();
            final int thumbHeight = customThumb.getIntrinsicHeight();
            available -= thumbHeight;
            // The extra space for the thumb to move on the track
            available += getThumbOffset() * 2;

            int left = (int) (getScale() * available + 0.5f);

            final int top, bottom;
            top = 0;
            bottom = customThumb.getIntrinsicWidth();

            final int right = left + customThumb.getIntrinsicHeight();

            customThumb.setBounds(left, top, right, bottom);

            canvas.save();
            canvas.translate(getPaddingBottom() - getThumbOffset(), getPaddingTop());
            customThumb.draw(canvas);
            canvas.restore();

        }
    }

    private float getScale() {
        final int max = getMax();
        return max > 0 ? getProgress() / (float) max : 0;
    }

    /**
     * Sets the progress for the SeekBar programmatically.
     *
     * @param progress
     */
    @Override
    public void setProgress(int progress) {
        setProgressFromUser(progress, false);
    }

    /**
     * Sets whether the progress was initiated from the user or programmatically.
     *
     * @param progress
     * @param fromUser
     */
    public void setProgressFromUser(int progress, boolean fromUser) {
        this.fromUser = fromUser;
        super.setProgress(progress);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onlySlideOnThumb) {
                    if (isOnThumb(event)) {
                        updateProgress(event.getY());
                        wasDownOnThumb = true;
                    } else {
                        return false;
                    }
                } else {
                    wasDownOnThumb = true;
                    updateProgress(event.getY());
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (wasDownOnThumb) {
                    updateProgress(event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                wasDownOnThumb = false;
                break;
        }
        return true;
    }

    private void updateProgress(float y) {
        int i = getMax() - (int) (getMax() * y / getHeight());
        setProgressFromUser(i, true);
    }

    private boolean isOnThumb(MotionEvent event) {
        Drawable thumb = getCustomThumb();
        Rect thumbBounds = thumb.getBounds();

        if (inBetween(event.getX(), thumbBounds.top, thumbBounds.bottom)
                && inBetween(event.getY(), getHeight() - thumbBounds.left, getHeight() - thumbBounds.right)) {
            return true;
        }

        return false;
    }

    private static boolean inBetween(float loc, float bound1, float bound2) {
        if (loc >= bound1 && loc <= bound2 || loc <= bound1 && loc >= bound2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Use this instead of the isFromUser returned in the {@link android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(SeekBar, int, boolean)}
     * @return whether the touche was initiated from the user
     */
    public boolean isFromUser() {
        return fromUser;
    }

    public boolean isOnlyThumb() {
        return onlySlideOnThumb;
    }

    /**
     * Whether the slider should only move when the touch was initiated on the thumb.
     *
     * @param onlySlideOnThumb
     */
    public void setOnlyThumb(boolean onlySlideOnThumb) {
        this.onlySlideOnThumb = onlySlideOnThumb;
        invalidate();
    }

    public Drawable getCustomThumb() {
        return customThumb;
    }

    /**
     * Custom thumb to ensure the thumb is drawn on the vertical seek bar. Compatible with Android M.
     * see http://stackoverflow.com/questions/33112277/android-6-0-marshmallow-stops-showing-vertical-seekbar-thumb/36094973
     *
     * @param customThumb
     */
    public void setCustomThumb(Drawable customThumb) {
        this.customThumb = customThumb;
        invalidate();
    }
}