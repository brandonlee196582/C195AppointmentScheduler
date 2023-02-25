package com.C195.Model;

import com.C195.helper.DateTimeProcessing;
import com.C195.helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import static com.C195.helper.JDBC.connection;

/**
 * Appointment Class is developed to track appointment data imported from the appointment table in the client_schedule database
 *
 * @author brandonLackey
 */
public class Appointment {
    private final int aptId, customerId, userId, contactId;
    private final String aptTitle, aptDescription, aptLocation, aptType, aptCreatedBy, aptLastUpdatedBy, contactString;
    private final Timestamp aptStartDateTime, aptEndDateTime, aptCreationDate, aptLastUpdatedDate;
    private static final ObservableList<Appointment> allApts = FXCollections.observableArrayList();

    /**
     *
     * @param aptId
     * @param aptTitle
     * @param aptDescription
     * @param aptLocation
     * @param aptType
     * @param aptStartDateTime
     * @param aptEndDateTime
     * @param aptCreationDate
     * @param aptCreatedBy
     * @param aptLastUpdatedDate
     * @param lastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     * @param contactString
     */
    public Appointment(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType, Timestamp aptStartDateTime, Timestamp aptEndDateTime, Timestamp aptCreationDate, String aptCreatedBy, Timestamp aptLastUpdatedDate, String lastUpdatedBy, Integer customerId, Integer userId, Integer contactId, String contactString) {
        this.aptId = aptId;
        this.aptTitle = aptTitle;
        this.aptDescription = aptDescription;
        this.aptLocation = aptLocation;
        this.aptType = aptType;
        this.aptStartDateTime = aptStartDateTime;
        this.aptEndDateTime = aptEndDateTime;
        this.aptCreationDate = aptCreationDate;
        this.aptCreatedBy = aptCreatedBy;
        this.aptLastUpdatedDate = aptLastUpdatedDate;
        this.aptLastUpdatedBy = lastUpdatedBy;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactString = contactString;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Appointment> getAllapts() {return allApts;}

    /**
     *
     * @param newApt
     */
    public static void addApt(Appointment newApt) {allApts.add(newApt);}

    /**
     *
     * @return
     */
    public int getAptId() {return aptId;}

    /**
     *
     * @return
     */
    public String getContactString() {return contactString;}

    /**
     *
     * @return
     */
    public String getAptTitle() {return aptTitle;}

    /**
     *
     * @return
     */
    public String getAptDescription() {return aptDescription;}

    /**
     *
     * @return
     */
    public String getAptLocation() {return aptLocation;}

    /**
     *
     * @return
     */
    public String getAptType() {return aptType;}

    /**
     *
     * @return
     */
    public Timestamp getAptStartDateTime() {return aptStartDateTime;}

    /**
     *
     * @return
     */
    public Timestamp getAptEndDateTime() {return aptEndDateTime;}

    /**
     *
     * @return
     */
    public Timestamp getAptCreationDate() {return aptCreationDate;}

    /**
     *
     * @return
     */
    public String getAptCreatedBy() {return aptCreatedBy;}

    /**
     *
     * @return
     */
    public Timestamp getAptLastUpdatedDate() {return aptLastUpdatedDate;}

    /**
     *
     * @return
     */
    public String getAptLastUpdatedBy() {return aptLastUpdatedBy;}

    /**
     *
     * @return
     */
    public int getCustomerId() {return customerId;}

    /**
     *
     * @return
     */
    public int getUserId() {return userId;}

    /**
     *
     * @return
     */
    public int getContactId() {return contactId;}

    /**
     *
     * @param date
     * @return
     */
    public static ObservableList<Appointment> getAptsByDay(LocalDate date) {

        Timestamp selectionTimestamp = DateTimeProcessing.stringToDateTime(date + " 00:00:00");
        Instant selectionInstant = selectionTimestamp.toInstant();
        long selectionEpoch = selectionInstant.getEpochSecond();
        long futureDay = selectionEpoch + 86400;

        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            Instant aptInstant = apt.getAptStartDateTime().toInstant();
            long aptEpoch = aptInstant.getEpochSecond();

            if (aptEpoch >= selectionEpoch && aptEpoch <= futureDay) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    /**
     *
     */
    public static void checkForAptByTime() {
        String[] curentDateTimeSplit = DateTimeProcessing.splitDateTime(Timestamp.from(Instant.now()));
        String[] currentDateSplit = curentDateTimeSplit[0].split("-");
        int currentYear = Integer.parseInt(currentDateSplit[0]);
        int currentMonth = Integer.parseInt(currentDateSplit[1]);
        int currentDay = Integer.parseInt(currentDateSplit[2]);
        int[] daysPerMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean isLeapYear = (currentYear % 4 == 0);
        isLeapYear = isLeapYear && (currentYear % 100 != 0 || currentYear % 400 == 0);
        if (isLeapYear) {
            daysPerMonth[1]++;
        }
        int currentHr = Integer.parseInt(curentDateTimeSplit[1]);
        int currentMin = Integer.parseInt(curentDateTimeSplit[2]);
        int futureHr = currentHr;
        int futureMin = currentMin;
        int futureDay = currentDay;
        int futureMonth = currentMonth;
        int futureYear = currentYear;
        if (futureMin >= 45) {
            int hrUp = futureHr + 1;
            int minDown = 60 - futureMin;
            if (hrUp >= 24) {
                hrUp = 0;
            }
            futureHr = hrUp;
            futureMin = minDown;
            if (futureHr == 0 && currentHr == 23) {
                futureDay = futureDay + 1;
                for (int i = 0; i < daysPerMonth.length; i++) {
                    if (futureMonth == i + 1) {
                        if (futureDay > daysPerMonth[i]) {
                            futureMonth = futureMonth + 1;
                            futureDay = 1;
                            if (futureMonth > 12) {
                                futureYear = futureYear + 1;
                                futureMonth = 1;
                            }
                        }
                    }
                }
            }
        } else {
            futureMin = futureMin + 15;
        }
        //"yyyy-MM-dd HH:mm:ss"
        String futureMonthString, futureDayString, futureHrString, futureMinString;
        if (futureMonth < 10) {
            futureMonthString = "0" + futureMonth;
        } else {
            futureMonthString = Integer.toString(futureMonth);
        }
        if (futureDay < 10) {
            futureDayString = "0" + futureDay;
        } else {
            futureDayString = Integer.toString(futureDay);
        }
        if (futureHr < 10) {
            futureHrString = "0" + futureHr;
        } else {
            futureHrString = Integer.toString(futureHr);
        }
        if (futureMin < 10) {
            futureMinString = "0" + futureMin;
        } else {
            futureMinString = Integer.toString(futureMin);
        }
        Timestamp futureDateTimestamp = DateTimeProcessing.stringToDateTime(futureYear + "-" + futureMonthString + "-" + futureDayString + " " + futureHrString + ":" + futureMinString + ":00");
        Instant futureInstant = futureDateTimestamp.toInstant();
        int upcomingAptCk = 0;
        for (Appointment apt : Appointment.getAllapts()) {
            String[] aptDateTimeSplit = DateTimeProcessing.splitDateTime(apt.getAptStartDateTime());
            String[] aptDateSplit = aptDateTimeSplit[0].split("-");
            int aptYear = Integer.parseInt(aptDateSplit[0]);
            int aptMonth = Integer.parseInt(aptDateSplit[1]);
            int aptDay = Integer.parseInt(aptDateSplit[2]);
            int aptHr = Integer.parseInt(aptDateTimeSplit[1]);
            int aptMin = Integer.parseInt(aptDateTimeSplit[2]);

            String aptMonthString, aptDayString, aptHrString, aptMinString;
            if (aptMonth < 10) {
                aptMonthString = "0" + aptMonth;
            } else {
                aptMonthString = Integer.toString(aptMonth);
            }
            if (aptDay < 10) {
                aptDayString = "0" + aptDay;
            } else {
                aptDayString = Integer.toString(aptDay);
            }
            if (aptHr < 10) {
                aptHrString = "0" + aptHr;
            } else {
                aptHrString = Integer.toString(aptHr);
            }
            if (aptMin < 10) {
                aptMinString = "0" + aptMin;
            } else {
                aptMinString = Integer.toString(aptMin);
            }
            Timestamp aptDateTimestamp = DateTimeProcessing.stringToDateTime(aptYear + "-" + aptMonthString + "-" + aptDayString + " " + aptHrString + ":" + aptMinString + ":00");
            Instant aptInstant = aptDateTimestamp.toInstant();

            if (aptInstant.getEpochSecond() >= Instant.now().getEpochSecond() && aptInstant.getEpochSecond() <= futureInstant.getEpochSecond()) {
                promptHelper.infoDialog("Notice","There is an appointment within the next 15 minutes.","Appointment Id " + apt.getAptId() + " is starting on " + aptDateTimeSplit[0] + " at " + aptHrString + ":" + aptMinString + ".");
                upcomingAptCk++;
            }
        }
        if (upcomingAptCk == 0) {
            promptHelper.infoDialog("Notice", "Appointment Check","No upcoming appointments.");
        }
    }

    /**
     *
     * @param date
     * @return
     */
    public static ObservableList<Appointment> getAptsByWeek(LocalDate date) {
        Timestamp selectionTimestamp = DateTimeProcessing.stringToDateTime(date + " 00:00:00");
        Instant selectionInstant = selectionTimestamp.toInstant();
        long selectionEpoch = selectionInstant.getEpochSecond();
        long futureWeek = selectionEpoch + 604800;

        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            Instant aptInstant = apt.getAptStartDateTime().toInstant();
            long aptEpoch = aptInstant.getEpochSecond();

            if (aptEpoch >= selectionEpoch && aptEpoch <= futureWeek) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    /**
     *
     * @param date
     * @return
     */
    public static ObservableList<Appointment> getAptsByMonth(LocalDate date) {
        Timestamp selectionTimestamp = DateTimeProcessing.stringToDateTime(date + " 00:00:00");
        Instant selectionInstant = selectionTimestamp.toInstant();
        long selectionEpoch = selectionInstant.getEpochSecond();
        long futureMonth = selectionEpoch + 2592000;

        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            Instant aptInstant = apt.getAptStartDateTime().toInstant();
            long aptEpoch = aptInstant.getEpochSecond();

            if (aptEpoch >= selectionEpoch && aptEpoch <= futureMonth) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    /**
     *
     * @throws SQLException
     */
    public static void getDatabaseApts() throws SQLException {
        int contactId;
        String aptSql, contactSql, contactName = "";

        aptSql = "SELECT * FROM client_schedule.appointments";
        PreparedStatement ps = connection.prepareStatement(aptSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            contactId = rs.getInt("Contact_ID");
            contactSql = "SELECT * FROM client_schedule.contacts where Contact_ID=" + contactId;
            PreparedStatement contactPs = connection.prepareStatement(contactSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet contactRs = contactPs.executeQuery();
            while(contactRs.next()){
                contactName = contactRs.getString("Contact_Name");
            }
            Timestamp start = DateTimeProcessing.importTimeToLocal(rs.getTimestamp("Start"));
            Timestamp end = DateTimeProcessing.importTimeToLocal(rs.getTimestamp("End"));
            Appointment.addApt(new Appointment(rs.getInt("Appointment_ID"),rs.getString("Title"),rs.getString("Description"),rs.getString("Location"),rs.getString("Type"),start,end,rs.getTimestamp("Create_Date"),rs.getString("Created_By"),rs.getTimestamp("Last_Update"),rs.getString("Last_Updated_By"),rs.getInt("Customer_ID"),rs.getInt("User_ID"),contactId,contactName));
        }
    }

    /**
     *
     * @param id
     * @throws SQLException
     */
    public static void remeoveApt(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID=" + id;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     *
     * @param aptId
     * @param aptTitle
     * @param aptDescription
     * @param aptLocation
     * @param aptType
     * @param aptStartDateTime
     * @param aptEndDateTime
     * @param aptCreatedBy
     * @param aptlastUpdatedBy
     * @param customerId
     * @param userId
     * @param contactId
     * @throws SQLException
     */
    public static void insertApt(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType, Timestamp aptStartDateTime, Timestamp aptEndDateTime, String aptCreatedBy, String aptlastUpdatedBy, Integer customerId, Integer userId, Integer contactId) throws SQLException {
        Timestamp aptCreationDate, aptLastUpdatedDate;

        aptCreationDate = Timestamp.valueOf(LocalDateTime.now());
        aptLastUpdatedDate = Timestamp.valueOf(LocalDateTime.now());
        Timestamp start = DateTimeProcessing.exportTimeToUtc(aptStartDateTime);
        Timestamp end = DateTimeProcessing.exportTimeToUtc(aptEndDateTime);

        if (aptId == 0) {
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, aptTitle);
            ps.setString(2, aptDescription);
            ps.setString(3, aptLocation);
            ps.setString(4, aptType);
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setTimestamp(7, aptCreationDate);
            ps.setString(8, aptCreatedBy);
            ps.setTimestamp(9, aptLastUpdatedDate);
            ps.setString(10, aptlastUpdatedBy);
            ps.setInt(11, customerId);
            ps.setInt(12, userId);
            ps.setInt(13, contactId);
            ps.executeUpdate();
        } else {
            String sql = "UPDATE client_schedule.appointments SET Title='" + aptTitle + "', Description='" + aptDescription + "', Location='" + aptLocation + "', Type='" + aptType + "', Start='" + start + "', End='" + end + "', Last_Update='" + aptLastUpdatedDate + "', Last_Updated_By='" + aptlastUpdatedBy + "', Customer_ID=" + customerId + ", User_ID=" + userId + ", Contact_ID=" + contactId + " WHERE Appointment_ID=" + aptId;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }
    }
}
