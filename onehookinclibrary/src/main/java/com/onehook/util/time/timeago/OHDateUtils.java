package com.onehook.util.time.timeago;

import android.content.Context;
import android.text.format.DateUtils;

import com.onehookinc.androidlib.R;

import java.text.SimpleDateFormat;
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
}
