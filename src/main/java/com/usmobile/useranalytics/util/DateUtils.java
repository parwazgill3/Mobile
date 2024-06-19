package com.usmobile.useranalytics.util;

import java.time.*;
import java.util.Date;

public class DateUtils {

    public static Date getDateDaysAgoMidnight(int daysAgo) {
        return Date.from(LocalDate.now(ZoneOffset.UTC).minusDays(daysAgo).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static Date getDateDaysAgoEndOfDay(int daysAgo) {
        return Date.from(LocalDate.now(ZoneOffset.UTC).minusDays(daysAgo).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
    }

    public static Date getTodayMidnight() {
        return Date.from(LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static Date getDateDaysFromNowEndOfDay(int daysFromNow) {
        return Date.from(LocalDate.now(ZoneOffset.UTC).plusDays(daysFromNow).atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC));
    }
}
