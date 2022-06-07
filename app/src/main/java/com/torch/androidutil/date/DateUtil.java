package com.torch.androidutil.date;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT_SAME_YEAR = "d MMM";
    public static final String DATE_FORMAT_OTHER_YEAR = "d MMM, y";

    public static Calendar getClearedUTC() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        return calendar;
    }

    public static long todayInUtcMilliseconds() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        return now.getTimeInMillis();
    }

    public static Pair<String, String> getDateRangeString(long startDate, long endDate, String sameYearFormat, String otherYearFormat) {
        Calendar todayCalendar = DateUtil.getClearedUTC();
        todayCalendar.setTimeInMillis(new Date().getTime());
        Calendar startCalendar = DateUtil.getClearedUTC();
        startCalendar.setTimeInMillis(startDate);
        Calendar endCalendar = DateUtil.getClearedUTC();
        endCalendar.setTimeInMillis(endDate);

        if (startCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            if (startCalendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR)) {
                return Pair.create(formatDate(new Date(startDate), sameYearFormat), formatDate(new Date(endDate), sameYearFormat));
            } else {
                return Pair.create(formatDate(new Date(startDate), sameYearFormat), formatDate(new Date(endDate), otherYearFormat));
            }
        }
        return Pair.create(formatDate(new Date(startDate), otherYearFormat), formatDate(new Date(endDate), otherYearFormat));
    }

    public static Pair<String, String> getDateRangeString(long startDate, long endDate) {
        return getDateRangeString(startDate, endDate, DATE_FORMAT_SAME_YEAR, DATE_FORMAT_OTHER_YEAR);
    }

    public static String formatDate(long timeInMs, String sameYearFormat, String otherYearFormat) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(timeInMs);

        Calendar now = Calendar.getInstance();
        if (instance.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            return formatDate(instance.getTime(), sameYearFormat);
        }
        return formatDate(instance.getTime(), otherYearFormat);
    }

    public static String socialDateFormat(Date date, boolean showToday, boolean alwaysShowTime) {
        Calendar timeToFormat = Calendar.getInstance();
        timeToFormat.setTime(date);
        Calendar now = Calendar.getInstance();
        final String timeFormatString = "hh:mm a";
        final String dateTimeFormatString = alwaysShowTime ? "dd MMM hh:mm a" : "MMM dd";
        final String generalFormat = alwaysShowTime ? "dd/MM/yyyy hh:mm a" : "dd/MM/yyyy";
//        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == timeToFormat.get(Calendar.DATE)
                && now.get(Calendar.MONTH) == timeToFormat.get(Calendar.MONTH)
                && now.get(Calendar.YEAR) == timeToFormat.get(Calendar.YEAR)) {
            String initials = showToday ? "Today " : "";
            return initials + formatDate(timeToFormat.getTime(), timeFormatString) + "";
        } else if (now.get(Calendar.DATE) - timeToFormat.get(Calendar.DATE) == 1
                && now.get(Calendar.MONTH) == timeToFormat.get(Calendar.MONTH)
                && now.get(Calendar.YEAR) == timeToFormat.get(Calendar.YEAR)) {
            return "Yesterday " + formatDate(timeToFormat.getTime(), timeFormatString);
        } else if (timeToFormat.get(Calendar.DATE) - now.get(Calendar.DATE) == 1
                && now.get(Calendar.MONTH) == timeToFormat.get(Calendar.MONTH)
                && now.get(Calendar.YEAR) == timeToFormat.get(Calendar.YEAR)) {
            return "Tomorrow " + formatDate(timeToFormat.getTime(), timeFormatString);
        } else if (now.get(Calendar.YEAR) == timeToFormat.get(Calendar.YEAR)) {
            return formatDate(timeToFormat.getTime(), dateTimeFormatString);
        } else {
            return formatDate(timeToFormat.getTime(), generalFormat);
        }
    }

    public static String formatDate(@NonNull Date date, String format) {
        return formatDate(date, format, TimeZone.getDefault());
    }

    public static String formatDate(@NonNull Date date, String format, TimeZone timeZone) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.trim(), Locale.getDefault());
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }
}
