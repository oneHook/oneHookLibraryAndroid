package com.onehook.util.time.timeago;

import android.content.Context;
import android.text.format.DateUtils;

import com.onehookinc.androidlib.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by eaglediaotomore on 2016-07-10.
 */

public class OHDateUtils extends DateUtils {

    private static SimpleDateFormat sFormat;

    private static SimpleDateFormat getFormatter() {
        if (sFormat == null) {
            sFormat = new SimpleDateFormat("EEEE");
        }
        return sFormat;
    }

    public static String getDateString(final Context context, final long time, final boolean hasYear, final boolean hasWeek) {
        final Date date = new Date(time);
        final SimpleDateFormat formatter = getFormatter();
        if (hasYear && hasWeek) {
            formatter.applyPattern(context.getString(R.string.date_format_week_year));
        } else if (hasYear) {
            formatter.applyPattern(context.getString(R.string.date_format_year));
        } else if (hasWeek) {
            formatter.applyPattern(context.getString(R.string.date_format_week));
        } else {
            formatter.applyPattern(context.getString(R.string.date_format_default));
        }
        return formatter.format(date);
    }

    public static String getTimeString(final Context context, final long time) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        final int min = calendar.get(Calendar.MINUTE);
        final SimpleDateFormat formatter = getFormatter();
        if (min == 0) {
            formatter.applyPattern("h a");
        } else {
            formatter.applyPattern("h:m a");
        }
        return formatter.format(new Date(time));
    }

    public static String getTimeIntervalString(final Context context, final long startTime, final long endTime) {
        final String start = getTimeString(context, startTime);
        final String end = getTimeString(context, endTime);
        return context.getString(R.string.time_interval_string, start, end);
    }
}
