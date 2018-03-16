package com.onehook.util.time.timeago;

import android.content.Context;
import android.text.format.DateUtils;

import com.onehookinc.androidlib.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
        final int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        final String meridiumString;
        if (hourOfDay < 5) {
            meridiumString = context.getString(R.string.meridiem_field_before_5);
        } else if (hourOfDay < 12) {
            meridiumString = context.getString(R.string.meridiem_field_before_12);
        } else if (hourOfDay < 14) {
            meridiumString = context.getString(R.string.meridiem_field_before_14);
        } else if (hourOfDay < 18) {
            meridiumString = context.getString(R.string.meridiem_field_before_18);
        } else {
            meridiumString = context.getString(R.string.meridiem_field_after_18);
        }

        final SimpleDateFormat formatter = getFormatter();
        if (min == 0) {
            formatter.applyPattern(context.getString(R.string.time_format_no_min, "\'" + meridiumString + "\'"));
        } else {
            formatter.applyPattern(context.getString(R.string.time_format_with_min, "\'" + meridiumString + "\'"));
        }
        return formatter.format(new Date(time));
    }

    public static String getTimeIntervalString(final Context context, final long startTime, final long endTime) {
        final String start = getTimeString(context, startTime);
        final String end = getTimeString(context, endTime);
        return context.getString(R.string.time_interval_string, start, end);
    }

    public static int getTodayWeekdayNormalized() {
        final Calendar calendar = Calendar.getInstance();
        final int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        switch (weekday) {
            case Calendar.SUNDAY:
                return 0;
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            default:
                return 0;
        }
    }

    private static String[] sWeekdayStrings = null;

    public static final String getWeekdayString(final int normalizedWeekday, final Context context) {
        if (sWeekdayStrings == null) {
            sWeekdayStrings = context.getResources().getStringArray(R.array.date_weekdays);
        }
        return sWeekdayStrings[normalizedWeekday];
    }

    /**
     * Create a date object with given year, month and day and 00:00:00 in UTC time.
     *
     * @param year  year
     * @param month month of the year (1-12)
     * @param day   day of the month
     * @return date of the given day in UTC
     */
    public static Date getDate(final int year, final int month, final int day) {
        return getDate(year, month, day, 0, 0);
    }

    /**
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minutes
     * @return
     */
    public static Date getDate(final int year, final int month, final int day, int hour, int minutes) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }
}
