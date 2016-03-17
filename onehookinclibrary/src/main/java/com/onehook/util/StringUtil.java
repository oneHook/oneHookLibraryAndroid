package com.onehook.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by EagleDiao on 2016-03-03.
 */
public class StringUtil {

    /**
     * @param num
     * @return
     */
    public static String getNumberSuperscript(final int num) {
        final int j = num % 10;
        final int k = num % 100;
        if (j == 1 && k != 11) {
            return "st";
        }
        if (j == 2 && k != 12) {
            return "nd";
        }
        if (j == 3 && k != 13) {
            return "rd";
        }
        return "th";
    }

    /**
     * @param text
     * @return
     */
    public static boolean isEmpty(final CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    /**
     *
     */
    private static Pattern sEmailPattern;

    /**
     * @param emailText
     * @return
     */
    public static boolean isValidEmail(final CharSequence emailText) {
        boolean isValid = false;
        if (sEmailPattern == null) {
            final String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            sEmailPattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        }
        final Matcher matcher = sEmailPattern.matcher(emailText);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
