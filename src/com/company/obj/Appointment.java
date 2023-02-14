package com.company.obj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static dbConnection.JDBC.connection;

public class Appointment {
    private int aptId, customerId, userId, contactId;
    private String aptTitle, aptDescription, aptLocation, aptType, aptCreatedBy, aptLastUpdatedBy;
    private Date aptStartDateTime,aptEndDateTime, aptCreationDate, aptLastUpdatedDate;
    private static ObservableList<Appointment> allApts = FXCollections.observableArrayList();

    public Appointment(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType,Date aptStartDateTime, Date aptEndDateTime, Date aptCreationDate, String aptCreatedBy, Date aptLastUpdatedDate, String lastUpdatedBy, Integer customerId, Integer userId, Integer contactId) {
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
    }
    public static ObservableList<Appointment> getAllapts() {return allApts;}
    public static void addApt(Appointment newApt) {allApts.add(newApt);}
    public int getAptId() {return aptId;}
    public void setAptId(int aptId) {this.aptId = aptId;}
    public String getAptTitle() {return aptTitle;}
    public void setAptTitle(String aptTitle) {this.aptTitle = aptTitle;}
    public String getAptDescription() {return aptDescription;}
    public void setAptDescription(String aptDescription) {this.aptDescription = aptDescription;}
    public String getAptLocation() {return aptLocation;}
    public void setAptLocation(String aptLocation) {this.aptLocation = aptLocation;}
    public String getAptType() {return aptType;}
    public void setAptType() {this.aptType = aptType;}
    public Date getAptStartDateTime() {return aptStartDateTime;}
    public void setAptStartDateTime(Date aptStartDateTime) {this.aptStartDateTime = aptStartDateTime;}
    public Date getAptEndDateTime() {return aptEndDateTime;}
    public void setAptEndDateTime(Date aptEndDateTime) {this.aptEndDateTime = aptEndDateTime;}
    public Date getAptCreationDate() {return aptCreationDate;}
    public void setAptCreationDate(Date aptCreationDate) {this.aptCreationDate = aptCreationDate;}
    public String getAptCreatedBy() {return aptCreatedBy;}
    public void setAptCreatedBy(String aptCreatedBy) {this.aptCreatedBy = aptCreatedBy;}
    public Date getAptLastUpdatedDate() {return aptLastUpdatedDate;}
    public void setAptLastUpdatedDate(Date aptLastUpdatedDate) {this.aptLastUpdatedDate = aptLastUpdatedDate;}
    public String getAptLastUpdatedBy() {return aptLastUpdatedBy;}
    public void setAptLastUpdatedBy(String aptLastUpdatedBy) {this.aptLastUpdatedBy = aptLastUpdatedBy;}
    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public int getContactId() {return contactId;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    public static void getDatabaseApts(String[] outColumns) throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.appointments";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Appointment.addApt(new Appointment(rs.getInt("Appointment_ID"),rs.getString("Title"),rs.getString("Description"),rs.getString("Location"),rs.getString("Type"),rs.getDate("Start"),rs.getDate("End"),rs.getDate("Create_Date"),rs.getString("Created_By"),rs.getDate("Last_Update"),rs.getString("Last_Updated_By"),rs.getInt("Customer_ID"),rs.getInt("User_ID"),rs.getInt("Contact_ID")));
        }
    }

    public static int insertApt(int aptId, String aptTitle, String aptDescription, String aptLocation, String aptType, Date aptStartDateTime, Date aptEndDateTime, Date aptCreationDate, String aptCreatedBy, Date aptLastUpdatedDate, String aptlastUpdatedBy, Integer customerId, Integer userId, Integer contactId) throws SQLException {
        String sql = "INSERT INTO customers (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, aptTitle);
        ps.setString(2, aptDescription);
        ps.setString(3, aptLocation);
        ps.setString(4, aptType);
        ps.setDate(5, (java.sql.Date) aptStartDateTime);
        ps.setDate(6, (java.sql.Date) aptEndDateTime);
        ps.setDate(7, (java.sql.Date) aptCreationDate);
        ps.setString(8, aptCreatedBy);
        ps.setDate(9, (java.sql.Date) aptLastUpdatedDate);
        ps.setString(10, aptlastUpdatedBy);
        ps.setInt(11, customerId);
        ps.setInt(12, userId);
        ps.setInt(13, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
