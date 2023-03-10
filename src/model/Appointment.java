package model;

import helper.DateTimeProcessing;
import helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.*;
import static helper.JDBC.connection;

/**
 * Appointment Class is developed to manage appointment data imported from the appointments table in the client_schedule database
 * Setters are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
 * @author brandonLackey
 */
public class Appointment {
    private final int aptId, customerId, userId, contactId;
    private final String aptTitle, aptDescription, aptLocation, aptType, aptCreatedBy, aptLastUpdatedBy, contactString;
    private final Timestamp aptStartDateTime, aptEndDateTime, aptCreationDate, aptLastUpdatedDate;
    private static final ObservableList<Appointment> allApts = FXCollections.observableArrayList();

    /**
     * Sets reference variables used to refer to Appointment class constructor.
     *
     * @param aptId The appointment ID imported from the appointments table in the client_schedule database
     * @param aptTitle The appointment title imported from the appointments table in the client_schedule database
     * @param aptDescription The appointment description imported from the appointments table in the client_schedule database
     * @param aptLocation The appointment location imported from the appointments table in the client_schedule database
     * @param aptType The appointment type imported from the appointments table in the client_schedule database
     * @param aptStartDateTime The appointment start date and time imported from the appointments table in the client_schedule database
     * @param aptEndDateTime The appointment end date and time imported from the appointments table in the client_schedule database
     * @param aptCreationDate The appointment creation date and time imported from the appointments table in the client_schedule database
     * @param aptCreatedBy The appointment created by string imported from the appointments table in the client_schedule database
     * @param aptLastUpdatedDate The appointment last updated date snd time imported from the appointments table in the client_schedule database
     * @param lastUpdatedBy The appointment last update by string imported from the appointments table in the client_schedule database
     * @param customerId The appointment customer id imported from the appointments table in the client_schedule database
     * @param userId The appointment user id imported from the appointments table in the client_schedule database
     * @param contactId The appointment contact id imported from the appointments table in the client_schedule database
     * @param contactString The appointment contact name string
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
     * Retries all appointments from the allApts ObservableList.
     *
     * @return Returns an observableList containing all appointments.
     */
    public static ObservableList<Appointment> getAllapts() {return allApts;}

    /**
     * Adds a new appointment to the allApts ObservableList.
     *
     * @param newApt Appointment object to add to allApts.
     */
    public static void addApt(Appointment newApt) {allApts.add(newApt);}

    /**
     * Getter for the appointment id.
     *
     * @return Returns an appointment id integer
     */
    public int getAptId() {return aptId;}

    /**
     * Getter for the contact string.
     *
     * @return Returns the contact name as a string.
     */
    public String getContactString() {return contactString;}

    /**
     * Getter for the appointment title.
     *
     * @return Returns the appointment title as a string.
     */
    public String getAptTitle() {return aptTitle;}

    /**
     * Getter for the appointment description.
     *
     * @return Returns the appointment description as a string.
     */
    public String getAptDescription() {return aptDescription;}

    /**
     * Getter for the appointment location.
     *
     * @return Returns the appointment description as a string.
     */
    public String getAptLocation() {return aptLocation;}

    /**
     *  Getter for the appointment type.
     *
     * @return Returns the appointment type as a string.
     */
    public String getAptType() {return aptType;}

    /**
     * Getter for the appointment start date and time.
     *
     * @return Returns the appointment start date and time as a timestamp.
     */
    public Timestamp getAptStartDateTime() {return aptStartDateTime;}

    /**
     * Getter for the appointment end date and time.
     *
     * @return Returns the end date and time as a timestamp.
     */
    public Timestamp getAptEndDateTime() {return aptEndDateTime;}

    /**
     * Getter for the appointment creation date and time.
     *
     * @return Returns the appointment creation date and time as a timestamp.
     */
    public Timestamp getAptCreationDate() {return aptCreationDate;}

    /**
     * Getter for the created by string.
     *
     * @return Returns the creators name as a string.
     */
    public String getAptCreatedBy() {return aptCreatedBy;}

    /**
     * Getter for the appointment last updated date and time.
     *
     * @return Returns the last updated date and time as a timestamp.
     */
    public Timestamp getAptLastUpdatedDate() {return aptLastUpdatedDate;}

    /**
     * Getter for the last update by string.
     *
     * @return Returns the name of the user who last updated the appointment as a string.
     */
    public String getAptLastUpdatedBy() {return aptLastUpdatedBy;}

    /**
     * Getter for the customer id.
     *
     * @return Returns the customer id as an integer.
     */
    public int getCustomerId() {return customerId;}

    /**
     * Getter for the user id.
     *
     * @return Returns the user id as an Integer.
     */
    public int getUserId() {return userId;}

    /**
     * Getter for the contact id.
     *
     * @return Returns the contact id as an integer.
     */
    public int getContactId() {return contactId;}

    /**
     * Getter for returning all appointments for a passed in day.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param date Passed in date for the date filter.
     * @return Returns an ObservableList containing all appointments for a passed date.
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
     * Checks for appointments occurring withing 15 minutes of the time executed.
     *
     * Room for improvement: Converting this logic to use epoch second could drastically reduce its complexity.
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
     * Gets an ObservableList of all appointments with start dates forward one week from the date executed.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param date Start date for the one week filter.
     * @return Returns an ObservableList containing all appointments occurring forward one week from the passed in date.
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
     * Gets an ObservableList of all appointments with start dates forward one month from the date executed.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param date Start date for the one month filter.
     * @return Returns an ObservableList containing all appointments occurring forward one month from the passed in date.
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
     * Pulls a query of all appointment items in the appointment table from the client_schedule database then creates appointment
     * objects for those items. Produces an error prompt if unable to retrieve data from the database.
     */
    public static void getDatabaseApts() {
        int contactId;
        String aptSql, contactSql, contactName = "";

        try {
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
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve appointments from the database. See message for additional details: " + e.getMessage());
        }
    }

    /**
     *  Removes an appointment from the appointment table in the client_schedule database. Produces an error prompt if unable
     *  to remove the appointment from the database.
     *
     * @param id Appointment ID for appointment to be removed.
     */
    public static void remeoveApt(int id) {
        try {
            String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID=" + id;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch(Exception e) {
            promptHelper.errorDialog("Database Error", "Unable to remove appointment from database. See message for additional details: " + e.getMessage());
        }
    }

    /**
     * Commits changes or creates new appointments in the appointments table of the client_schedule database. Produces an
     * error prompt if unable to retrieve data from the database.
     *
     * @param aptId Appointment id integer
     * @param aptTitle appointment title string
     * @param aptDescription appointment description string
     * @param aptLocation appointment location string
     * @param aptType appointment type string
     * @param aptStartDateTime appointment start date and time timestamp
     * @param aptEndDateTime appointment end date and time timestamp
     * @param aptCreatedBy appointment created by username string
     * @param aptlastUpdatedBy Date and time appointment was last updated as a timestamp
     * @param customerId appointment customer id as an integer
     * @param userId appointment user id as an integer
     * @param contactId appointment contact id as an integer
     */
    public static void insertApt(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType, Timestamp aptStartDateTime, Timestamp aptEndDateTime, String aptCreatedBy, String aptlastUpdatedBy, Integer customerId, Integer userId, Integer contactId) {
        Timestamp aptCreationDate, aptLastUpdatedDate;

        Timestamp timestampNow = Timestamp.valueOf(LocalDateTime.now());
        Timestamp aptCreationDateUTC = DateTimeProcessing.exportTimeToUtc(timestampNow);
        Timestamp aptLastUpdateDateUTC = DateTimeProcessing.exportTimeToUtc(timestampNow);

        Timestamp start = DateTimeProcessing.exportTimeToUtc(aptStartDateTime);
        Timestamp end = DateTimeProcessing.exportTimeToUtc(aptEndDateTime);

        try {

            if (aptId == 0) {
                String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, aptTitle);
                ps.setString(2, aptDescription);
                ps.setString(3, aptLocation);
                ps.setString(4, aptType);
                ps.setTimestamp(5, start);
                ps.setTimestamp(6, end);
                ps.setTimestamp(7, aptCreationDateUTC);
                ps.setString(8, aptCreatedBy);
                ps.setTimestamp(9, aptLastUpdateDateUTC);
                ps.setString(10, aptlastUpdatedBy);
                ps.setInt(11, customerId);
                ps.setInt(12, userId);
                ps.setInt(13, contactId);
                ps.executeUpdate();
            } else {
                try{
                    String sql = "UPDATE client_schedule.appointments SET Title='" + aptTitle + "', Description='" + aptDescription + "', Location='" + aptLocation + "', Type='" + aptType + "', Start='" + start + "', End='" + end + "', Last_Update='" + aptLastUpdateDateUTC + "', Last_Updated_By='" + aptlastUpdatedBy + "', Customer_ID=" + customerId + ", User_ID=" + userId + ", Contact_ID=" + contactId + " WHERE Appointment_ID=" + aptId;
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.executeUpdate();
                } catch(Exception e) {
                    promptHelper.errorDialog("Database Error", "Unable to update appointment ID " + aptId + " database values. See message for additional details: " + e.getMessage());
                }
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Error", "Unable to add appointment to database. See message for additional details: " + e.getMessage());
        }
    }
}
