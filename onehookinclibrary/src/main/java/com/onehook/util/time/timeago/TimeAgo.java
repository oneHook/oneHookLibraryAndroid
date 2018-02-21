package com.onehook.util.time.timeago;

import android.content.Context;

import com.onehookinc.androidlib.R;

/**
 * Created by EagleDiao on 2016-04-19.
 */
public class TimeAgo {

    private static final long SECOND = 1000;
    private static final long NOW = 30000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = DAY * 7;
    private static final long MONTH = WEEK * 4;
    private static final long YEAR = DAY * 365;

    public TimeAgo() {

    }

    public String relativeTimeShort(final Context context, final long time) {
        return relativeTimeShort(context, System.currentTimeMillis(), time);
    }

    public String relativeTimeShort(final Context context, final long currentTime, final long time) {
        final long secondsDelta = time - currentTime;
        if (secondsDelta < 0) {
            return timeAgoShort(context, -secondsDelta);
        } else {
            return context.getResources().getString(R.string.time_ago_short_now);
        }
    }

    public String relativeTimeMedium(final Context context, final long time) {
        return relativeTimeMedium(context, System.currentTimeMillis(), time);
    }

    public String relativeTimeMedium(final Context context, final long currentTime, final long time) {
        final long secondsDelta = time - currentTime;
        if (secondsDelta < 0) {
            return timeAgoMedium(context, -secondsDelta);
        } else {
            return context.getResources().getString(R.string.time_ago_medium_now);
        }
    }

    private String timeAgoShort(final Context context, final long delta) {
        if (delta < NOW) {
            return context.getResources().getString(R.string.time_ago_short_now);
        } else if (delta < MINUTE) {
            return context.getResources().getString(R.string.time_ago_short_seconds_ago, delta / SECOND);
        } else if (delta < HOUR) {
            return context.getResources().getString(R.string.time_ago_short_minutes_ago, delta / MINUTE);
        } else if (delta < DAY) {
            return context.getResources().getString(R.string.time_ago_short_hours_ago, delta / HOUR);
        } else if (delta < WEEK) {
            return context.getResources().getString(R.string.time_ago_short_days_ago, delta / DAY);
        } else if (delta < MONTH) {
            return context.getResources().getString(R.string.time_ago_short_weeks_ago, delta / WEEK);
        } else if (delta < YEAR) {
            return context.getResources().getString(R.string.time_ago_short_months_ago, delta / MONTH);
        } else {
            return context.getResources().getString(R.string.time_ago_short_years_ago, delta / YEAR);
        }
    }

    private String timeAgoMedium(final Context context, final long delta) {
        if (delta < NOW) {
            return context.getResources().getString(R.string.time_ago_medium_now);
        } else if (delta < MINUTE) {
            return context.getResources().getString(R.string.time_ago_medium_seconds_ago, delta / SECOND);
        } else if (delta < HOUR) {
            return context.getResources().getString(R.string.time_ago_medium_minutes_ago, delta / MINUTE);
        } else if (delta < DAY) {
            return context.getResources().getString(R.string.time_ago_medium_hours_ago, delta / HOUR);
        } else if (delta < WEEK) {
            return context.getResources().getString(R.string.time_ago_medium_days_ago, delta / DAY);
        } else if (delta < MONTH) {
            return context.getResources().getString(R.string.time_ago_medium_weeks_ago, delta / WEEK);
        } else if (delta < YEAR) {
            return context.getResources().getString(R.string.time_ago_medium_months_ago, delta / MONTH);
        } else {
            return context.getResources().getString(R.string.time_ago_medium_years_ago, delta / YEAR);
        }
    }

}
