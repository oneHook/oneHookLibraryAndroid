package com.onehook.util;

/**
 * Created by EagleDiao on 2016-03-03.
 */
public class StringUtil {

    public static String getNumberSuperscript(final int num) {
         final int j = num % 10;
        final int k = num % 100;
        if(j == 1 && k != 11) {
            return "st";
        }
        if(j == 2 && k != 12) {
            return "nd";
        }
        if(j == 3 && k != 13) {
            return "rd";
        }
        return "th";
    }
}
