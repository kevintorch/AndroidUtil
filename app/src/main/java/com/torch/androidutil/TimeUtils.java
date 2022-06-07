package com.torch.androidutil;

import static java.lang.String.format;

import androidx.annotation.NonNull;

public class TimeUtils {

    public static String formatDuration(long duration) {
        return formatDuration(duration, false);
    }

    /**
     * format the duration to readable string
     *
     * @param duration duration in minutes.
     *                 <p>
     *                 NOTE:- duration should always be in minutes. otherwise unexpected results
     *                 will occur.
     */
    public static String formatDuration(long duration, boolean inShort) {

        long hour = duration / 60;
        long minutes = duration % 60;

        if (hour > 0) {
            if (minutes > 0) {
                return formatHourMinute(hour, minutes, inShort);
            } else {
                return formatHour(hour, inShort);
            }
        }

        return formatMinute(minutes, inShort);
    }

    @NonNull
    private static String formatMinute(long minutes, boolean inShort) {
        String minuteString = minutes == 1 ? "Minute" : "Minutes";
        if (inShort) minuteString = "m";
        String FORMAT = inShort ? "%d%s" : "%d %s";
        return format(FORMAT, minutes, minuteString);
    }

    @NonNull
    private static String formatHour(long hours, boolean inShort) {
        String hourString = hours == 1 ? "Hour" : "Hours";
        if (inShort) hourString = "h";
        String FORMAT = inShort ? "%d%s" : "%d %s";
        return format(FORMAT, hours, hourString);
    }

    @NonNull
    private static String formatHourMinute(long hours, long minutes, boolean inShort) {
        String hourString = formatHour(hours, inShort);
        String minuteString = formatMinute(minutes, inShort);
        String FORMAT = inShort ? "%s %s" : "%s and %s";
        return format(FORMAT, hourString, minuteString);
    }
}
