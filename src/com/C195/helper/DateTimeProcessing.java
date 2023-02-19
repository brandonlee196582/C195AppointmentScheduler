package com.C195.helper;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeProcessing {
    ZonedDateTime dateTime;

    public static Timestamp timestampProcessor(ZonedDateTime date) {
        String[] dateStringSplit = date.toString().split("T");
        String[] timeStringSplit = dateStringSplit[1].split("-");
        if (timeStringSplit.length < 2) {
            timeStringSplit = dateStringSplit[1].split("Z");
        }
        if (timeStringSplit.length < 2) {
            timeStringSplit = dateStringSplit[1].split("\\+");
        }
        String timestampString = dateStringSplit[0] + " " + timeStringSplit[0];
        Timestamp timestamp = Timestamp.valueOf(timestampString);
        return timestamp;
    }
    public static Timestamp[] zoneDateTimeToTimestamp() {

        ZonedDateTime instant = ZonedDateTime.now();
        ZonedDateTime instantInUTC = instant.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime instantInEST = instant.withZoneSameInstant(ZoneId.of("America/New_York"));

        Timestamp[] timestampArray = {timestampProcessor(instant),timestampProcessor(instantInUTC),timestampProcessor(instantInEST)};

        return timestampArray;
    }

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
}
