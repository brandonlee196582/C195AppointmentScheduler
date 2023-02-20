package com.C195.helper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeProcessing {

    public static String[] splitDateTime(Timestamp timestamp) {
        String[] dateSplit = timestamp.toString().split(" ");
        String[] timeSplit = dateSplit[1].split(":");
        String[] dateArray = new String[] {dateSplit[0], timeSplit[0], timeSplit[1], timeSplit[2]};

        return dateArray;
    }

    public static String[] splitYearMonthDay(Timestamp timestamp) {

        String[] splitDate = splitDateTime(timestamp);
        String[] dateOut = splitDate[0].split("-");

        return dateOut;
    }

    public static Timestamp importTimeToLocal(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime dateTimeLocal = zoneFromLocal.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime timeStampLocal = dateTimeLocal.toLocalDateTime();

        return Timestamp.valueOf(timeStampLocal);
    }

    public static Timestamp exportTimeToUtc(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime timeStampUtc = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampUtc);
    }

    public static Timestamp dateTimeToEST(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("US/Eastern"));
        LocalDateTime timeStampUtc = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampUtc);
    }
}
