package com.C195.helper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author brandonLackey
 */
public class DateTimeProcessing {

    /**
     *
     * @param timestamp
     * @return
     */
    public static String[] splitDateTime(Timestamp timestamp) {
        String[] dateSplit = timestamp.toString().split(" ");
        String[] timeSplit = dateSplit[1].split(":");
        String[] dateArray = new String[] {dateSplit[0], timeSplit[0], timeSplit[1], timeSplit[2]};

        return dateArray;
    }

    /**
     *
     * @param timestamp
     * @return
     */
    public static String[] splitYearMonthDay(Timestamp timestamp) {

        String[] splitDate = splitDateTime(timestamp);
        String[] dateOut = splitDate[0].split("-");

        return dateOut;
    }

    /**
     *
     * @param timestamp
     * @return
     */
    public static Timestamp importTimeToLocal(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime dateTimeLocal = zoneFromLocal.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime timeStampLocal = dateTimeLocal.toLocalDateTime();

        return Timestamp.valueOf(timeStampLocal);
    }

    /**
     *
     * @param timestamp
     * @return
     */
    public static Timestamp exportTimeToUtc(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime timeStampUtc = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampUtc);
    }

    /**
     *
     * @param timestamp
     * @return
     */
    public static Timestamp dateTimeToEST(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("US/Eastern"));
        LocalDateTime timeStampEst = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampEst);
    }

    /**
     *
     * @param dateTimeString
     * @return
     */
    public static Timestamp stringToDateTime(String dateTimeString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        return Timestamp.valueOf(dateTime);
    }

    /**
     *
     * @param timestamp
     * @return
     */
    public static long timestampToEpoch(Timestamp timestamp) {

        Instant timestampInstant = timestamp.toInstant();
        long timestampEpoch = timestampInstant.getEpochSecond();

        return timestampEpoch;
    }

    /**
     *
     * @param timestampArr
     * @return
     */
    public static long[] timestampArrToEpoch(Timestamp[] timestampArr) {
        long[] epochArr = new long[timestampArr.length];

        for (int i = 0; i < timestampArr.length; i++) {
            epochArr[i] = timestampToEpoch(timestampArr[i]);
        }

        return epochArr;
    }

}
