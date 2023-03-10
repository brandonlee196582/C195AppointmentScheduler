package helper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Collection of static helper methods which manipulate dates and times.
 *
 * @author brandonLackey
 */
public class DateTimeProcessing {

    /**
     * Converts a timestamp to an array which contain the date, hour, minute, and second as strings.
     *
     * @param timestamp Passed in timestamp to be processed.
     * @return Returns an array [DATE STRING, HOUR STRING, MINUTE STRING, SECOND STRING]
     */
    public static String[] splitDateTime(Timestamp timestamp) {
        String[] dateSplit = timestamp.toString().split(" ");
        String[] timeSplit = dateSplit[1].split(":");
        String[] dateArray = new String[] {dateSplit[0], timeSplit[0], timeSplit[1], timeSplit[2]};

        return dateArray;
    }

    /**
     * Converts a passed UTC timestamp to the system default time.
     *
     * @param timestamp Timestamp to convert to system time zone
     * @return Returns a timestamp in the system default time.
     */
    public static Timestamp importTimeToLocal(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.of("UTC"));
        ZonedDateTime dateTimeLocal = zoneFromLocal.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime timeStampLocal = dateTimeLocal.toLocalDateTime();

        return Timestamp.valueOf(timeStampLocal);
    }

    /**
     * Converts a passed system timestamp to UTC.
     *
     * @param timestamp Timestamp to convert to UTC time.
     * @return Returns a timestamp in UTC time.
     */
    public static Timestamp exportTimeToUtc(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime timeStampUtc = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampUtc);
    }

    /**
     * Converts a passed system timestamp to EST.
     *
     * @param timestamp Timestamp to convert to EST time.
     * @return Returns a timestamp in UTC time.
     */
    public static Timestamp dateTimeToEST(Timestamp timestamp) {

        ZonedDateTime zoneFromLocal = timestamp.toLocalDateTime().atZone(ZoneId.systemDefault());
        ZonedDateTime dateTimeUtc = zoneFromLocal.withZoneSameInstant(ZoneId.of("US/Eastern"));
        LocalDateTime timeStampEst = dateTimeUtc.toLocalDateTime();

        return Timestamp.valueOf(timeStampEst);
    }

    /**
     * Converts a passed string "yyyy-MM-dd HH:mm:ss" to a timestamp.
     *
     * @param dateTimeString String formatted "yyyy-MM-dd HH:mm:ss" to convert to a timestamp
     * @return Returns a timestamp of the passed string.
     */
    public static Timestamp stringToDateTime(String dateTimeString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        return Timestamp.valueOf(dateTime);
    }

    /**
     * Converts a passed timestamp to java epoch second (Seconds from "1970-01-01T00:00:00Z").
     * @param timestamp Timestamp to convert to epoch.
     * @return Returns the number of seconds from "1970-01-01T00:00:00Z" as long.
     */
    public static long timestampToEpoch(Timestamp timestamp) {

        Instant timestampInstant = timestamp.toInstant();
        long timestampEpoch = timestampInstant.getEpochSecond();

        return timestampEpoch;
    }

    /**
     * Converts a passed array of timestamps to java epoch second (Seconds from "1970-01-01T00:00:00Z").
     * Used to combine a series of dates for comparison.
     *
     * @param timestampArr Array of timestamps to convert to epoch.
     * @return Returns an array of numbers that are the numbers of seconds from "1970-01-01T00:00:00Z" as long.
     */
    public static long[] timestampArrToEpoch(Timestamp[] timestampArr) {
        long[] epochArr = new long[timestampArr.length];

        for (int i = 0; i < timestampArr.length; i++) {
            epochArr[i] = timestampToEpoch(timestampArr[i]);
        }
        return epochArr;
    }

}
