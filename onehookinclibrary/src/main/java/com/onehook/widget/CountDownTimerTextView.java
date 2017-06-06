package com.onehook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.onehookinc.androidlib.R;

import java.util.HashMap;

/**
 * Created by EagleDiaoOptimity on 2017-05-31.
 */

public class CountDownTimerTextView extends AppCompatTextView {

    private static final String DAY_PLACEHOLDER = "DD";
    private static final String HOUR_PLACEHOLDER = "HH";
    private static final String MINUTE_PLACEHOLDER = "MM";
    private static final String SECOND_PLACEHOLDER = "SS";

    /**
     * Default interval.
     */
    private static final int DEFAULT_INTERVAL = 1000;

    public CountDownTimerTextView(Context context) {
        super(context);
        commonInit(context, null);
    }

    public CountDownTimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public CountDownTimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    /**
     * Timer.
     */
    private CountDownTimer mCountDownTimer;

    /**
     * Time format.
     */
    private String mTimeFormat;

    /**
     * Decoded time format.
     */
    private HashMap<String, Integer> mTimeFormatPosition;

    /**
     * Interval between each update.
     */
    private int mInterval;

    /**
     * Current remaining time.
     */
    private long mCurrentTime;

    /**
     * Common Init.
     *
     * @param context context
     * @param attrs   attributes
     */
    private void commonInit(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        mInterval = DEFAULT_INTERVAL;
        /*
         * Load from style if any.
         */
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CountDownTimerTextView);
            mTimeFormat = a.getString(R.styleable.CountDownTimerTextView_oh_count_down_timer_format);
            mInterval = a.getInteger(R.styleable.CountDownTimerTextView_oh_count_down_timer_interval, mInterval);
            a.recycle();
        }
        if (mTimeFormat == null) {
            /* default format */
            mTimeFormat = "DD:MM:HH:SS";
        }
        setTimeFormat(mTimeFormat);
    }

    /**
     * Start a new count down, if there is any timer current running the running one will be canceled.
     *
     * @param miliseconds mili seconds to go
     */
    public void startCountDown(final long miliseconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCurrentTime = miliseconds;
        mCountDownTimer = new CountDownTimer(miliseconds, mInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = millisUntilFinished;
                renderTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                renderTime(0);
                mCountDownTimer = null;
            }
        };
        mCountDownTimer.start();
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    /**
     * Get current remaining time.
     *
     * @return current remaining time
     */
    public long getCurrentTimeRemaining() {
        return mCurrentTime;
    }

    public void setTimeFormat(final String timeFormat) {
        final StringBuilder sb = new StringBuilder();
        boolean shouldIgnore = false;
        for (int i = 0; i < timeFormat.length(); ) {
            if (shouldIgnore) {
                sb.append(timeFormat.charAt(i));
                i++;
            }
            if (timeFormat.charAt(i) == '\'') {
                shouldIgnore = !shouldIgnore;
                i++;
            } else if (timeFormat.startsWith(DAY_PLACEHOLDER, i)) {
                sb.append("%1$d");
                i += 2;
            } else if (timeFormat.startsWith(HOUR_PLACEHOLDER, i)) {
                sb.append("%2$02d");
                i += 2;
            } else if (timeFormat.startsWith(MINUTE_PLACEHOLDER, i)) {
                sb.append("%3$02d");
                i += 2;
            } else if (timeFormat.startsWith(SECOND_PLACEHOLDER, i)) {
                sb.append("%4$02d");
                i += 2;
            } else {
                sb.append(timeFormat.charAt(i));
                i++;
            }
        }
        mTimeFormat = sb.toString();
    }


    /**
     * Render the text.
     *
     * @param milliseconds
     */
    private void renderTime(final long milliseconds) {
        final long seconds = (milliseconds / 1000) % 60;
        final long minute = (milliseconds / 60000) % 60;
        final long hour = (milliseconds / 3600000) % 24;
        final long day = milliseconds / 86400000;
        final String text = String.format(mTimeFormat, day, hour, minute, seconds);
        setText(text);
    }

}
