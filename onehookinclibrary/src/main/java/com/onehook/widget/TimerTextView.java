package com.onehook.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by EagleDiaoOptimity on 2017-05-31.
 */

public class TimerTextView extends AppCompatTextView {

    public enum TimerTextViewStyle {
        HOUR_MINUTE_SECONDS,
        DAY_HOUR_MINUTE_SECONDS
    }

    public TimerTextView(Context context) {
        super(context);
        commonInit(context, null);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        commonInit(context, attrs);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        commonInit(context, attrs);
    }

    private TimerTextViewStyle mTimerStyle;

    private CountDownTimer mCountDownTimer;

    private String mPrefix;

    private long mCurrentTime;

    private void commonInit(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        mTimerStyle = TimerTextViewStyle.DAY_HOUR_MINUTE_SECONDS;
        mPrefix = "";
    }

    public void setStyle(TimerTextViewStyle style) {
        mTimerStyle = style;
    }

    public void setPrefix(final String prefix) {
        mPrefix = prefix;
    }

    public void startCountDown(final long miliseconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(miliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCurrentTime = millisUntilFinished;
                renderTime(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                renderTime(0);
            }
        };
        mCountDownTimer.start();
    }

    public void startCountUp() {

    }

    public void stop() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    private void renderTime(final long milliseconds) {
        final long seconds = (milliseconds / 1000) % 60;
        final long minute = (milliseconds / 60000) % 60;
        final long hour = (milliseconds / 3600000) % 24;
        final long day = milliseconds / 86400000;
        if (mTimerStyle == TimerTextViewStyle.DAY_HOUR_MINUTE_SECONDS) {
            final String text = String.format("%s%02d:%02d:%02d:%02d", mPrefix, day, hour, minute, seconds);
            setText(text);
        } else {
            final String text = String.format("%s%02d:%02d:%02d", mPrefix, hour, minute, seconds);
            setText(text);
        }
    }

}
