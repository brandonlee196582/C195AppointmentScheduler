package com.C195.Model;

import com.C195.helper.DateTimeProcessing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;

import static com.C195.helper.JDBC.connection;

public class Appointment {
    private int aptId, customerId, userId, contactId;
    private String aptTitle, aptDescription, aptLocation, aptType, aptCreatedBy, aptLastUpdatedBy, contactString;
    private Timestamp aptStartDateTime,aptEndDateTime, aptCreationDate, aptLastUpdatedDate;
    private static ObservableList<Appointment> allApts = FXCollections.observableArrayList();

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
    public static ObservableList<Appointment> getAllapts() {return allApts;}
    public static void addApt(Appointment newApt) {allApts.add(newApt);}
    public int getAptId() {return aptId;}
    public String getContactString() {return contactString;}
    public void setAptId(int aptId) {this.aptId = aptId;}
    public String getAptTitle() {return aptTitle;}
    public void setAptTitle(String aptTitle) {this.aptTitle = aptTitle;}
    public String getAptDescription() {return aptDescription;}
    public void setAptDescription(String aptDescription) {this.aptDescription = aptDescription;}
    public String getAptLocation() {return aptLocation;}
    public void setAptLocation(String aptLocation) {this.aptLocation = aptLocation;}
    public String getAptType() {return aptType;}
    public void setAptType() {this.aptType = aptType;}
    public Timestamp getAptStartDateTime() {return aptStartDateTime;}
    public void setAptStartDateTime(Timestamp aptStartDateTime) {this.aptStartDateTime = aptStartDateTime;}
    public Timestamp getAptEndDateTime() {return aptEndDateTime;}
    public void setAptEndDateTime(Timestamp aptEndDateTime) {this.aptEndDateTime = aptEndDateTime;}
    public Timestamp getAptCreationDate() {return aptCreationDate;}
    public void setAptCreationDate(Timestamp aptCreationDate) {this.aptCreationDate = aptCreationDate;}
    public String getAptCreatedBy() {return aptCreatedBy;}
    public void setAptCreatedBy(String aptCreatedBy) {this.aptCreatedBy = aptCreatedBy;}
    public Timestamp getAptLastUpdatedDate() {return aptLastUpdatedDate;}
    public void setAptLastUpdatedDate(Timestamp aptLastUpdatedDate) {this.aptLastUpdatedDate = aptLastUpdatedDate;}
    public String getAptLastUpdatedBy() {return aptLastUpdatedBy;}
    public void setAptLastUpdatedBy(String aptLastUpdatedBy) {this.aptLastUpdatedBy = aptLastUpdatedBy;}
    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public int getContactId() {return contactId;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    public static ObservableList<Appointment> getAptsByDay(LocalDate date) {
        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            String dateString = date.toString();
            String [] aptStartTimeArray = DateTimeProcessing.splitDateTime(apt.getAptStartDateTime());
            if (dateString.equals(aptStartTimeArray[0])) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    public static ObservableList<Appointment> getAptsByWeek(LocalDate date) {

        Calendar calendar = Calendar.getInstance();
        String[] passedDate = date.toString().split("-");
        calendar.set(Integer.parseInt(passedDate[0]), Integer.parseInt(passedDate[1]), Integer.parseInt(passedDate[2]));
        String passedDateWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + passedDate[0];
        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            String[] aptDateString = DateTimeProcessing.splitYearMonthDay(apt.getAptStartDateTime());
            calendar.set(Integer.parseInt(aptDateString[0]), Integer.parseInt(aptDateString[1]), Integer.parseInt(aptDateString[2]));
            String aptStartDateWeekOfYear = calendar.get(Calendar.WEEK_OF_YEAR) + aptDateString[0];
            if (aptStartDateWeekOfYear.equals(passedDateWeekOfYear)) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    public static ObservableList<Appointment> getAptsByMonth(LocalDate date) {
        ObservableList<Appointment> foundApts = FXCollections.observableArrayList();
        allApts.forEach(apt -> {
            String[] aptDateString = DateTimeProcessing.splitDateTime(apt.getAptStartDateTime());
            LocalDate aptDate = LocalDate.parse(aptDateString[0]);
            if (date.getMonthValue() == aptDate.getMonthValue()) {
                foundApts.add(apt);
            }
        });
        return foundApts;
    }

    public static void getDatabaseApts() throws SQLException {
        Integer contactId;
        String aptSql, contactSql, contactName = "";

        aptSql = "SELECT * FROM client_schedule.appointments";
        PreparedStatement ps = connection.prepareStatement(aptSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            //SELECT * FROM client_schedule.contacts where Contact_ID=2;
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

    public static void remeoveApt(int id) throws SQLException {
        String sql = "DELETE FROM client_schedule.appointments WHERE Appointment_ID=" + id;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.executeUpdate();
    }

    public static int insertApt(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType, Timestamp aptStartDateTime, Timestamp aptEndDateTime, Timestamp aptCreationDate, String aptCreatedBy, Timestamp aptLastUpdatedDate, String aptlastUpdatedBy, Integer customerId, Integer userId, Integer contactId) throws SQLException {
        String sql = "INSERT INTO customers (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, aptTitle);
        ps.setString(2, aptDescription);
        ps.setString(3, aptLocation);
        ps.setString(4, aptType);
        ps.setTimestamp(5, aptStartDateTime);
        ps.setTimestamp(6, aptEndDateTime);
        ps.setTimestamp(7, aptCreationDate);
        ps.setString(8, aptCreatedBy);
        ps.setTimestamp(9, aptLastUpdatedDate);
        ps.setString(10, aptlastUpdatedBy);
        ps.setInt(11, customerId);
        ps.setInt(12, userId);
        ps.setInt(13, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
