package com.torch.androidutil.date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {

    public static LocalTime currentTimeMillis() {
        return LocalTime.now();
    }

    public static long milliSecondsOf(LocalDate localDate) {
        return milliSecondsOf(localDate, ZoneId.systemDefault());
    }

    public static long milliSecondsOf(LocalDate localDate, @NonNull ZoneId zoneId) {
        return localDate.atStartOfDay(zoneId)
                .toInstant()
                .toEpochMilli();
    }

    public static LocalDate toLocalDate(long epochMilli) {
        return toLocalDate(epochMilli, null);
    }

    public static LocalDate toLocalDate(long epochMilli, @Nullable ZoneId zoneId) {
        return Instant.ofEpochMilli(epochMilli)
                .atZone(zoneId == null ? ZoneId.systemDefault() : zoneId)
                .toLocalDate();
    }

    public static LocalDate toLocalDate(Date date) {
        return toLocalDate(date.getTime());
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay(), null);
    }

    public static Date toDate(LocalDate localDate, @Nullable ZoneId zoneId) {
        return toDate(localDate.atStartOfDay(), zoneId);
    }

    public static Date toDate(LocalDateTime localDateTime, @Nullable ZoneId zoneId) {
        return Date.from(localDateTime
                .atZone(zoneId == null ? ZoneId.systemDefault() : zoneId)
                .toInstant());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, null);
    }

    public static LocalDateTime toLocalDateTime(Date date, @Nullable ZoneId zoneId) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(zoneId == null ? ZoneId.systemDefault() : zoneId)
                .toLocalDateTime();
    }

    public static void forEachDates(final LocalDate fromDate, final LocalDate toDate,
                                    Consumer<LocalDate> doOperation) {
        LocalDate start = fromDate;
        while (start.isBefore(toDate) ||
                start.isEqual(toDate)) {
            doOperation.accept(start);
            start = start.plusDays(1);
        }
    }
}
