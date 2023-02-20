package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.C195.helper.JDBC.connection;

public class Division {
    int divisionId;
    String divisionName;
    Date creationDate;
    String createdBy;
    Date lastUpdatedDate;
    String lastUpdatedBy;
    int countryId;
    private static ObservableList<Division> allDivisions = FXCollections.observableArrayList();

    private Division(int divisionId, String divisionName, Date creationDate, String createdBy, Date lastUpdatedDate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }
    public static ObservableList<Division> getAllDivisions() {return allDivisions;}
    public static void addDivision(Division newDivision) {allDivisions.add(newDivision);}
    public int getDivisionId() {return divisionId;}
    public String getDivisionName() {return divisionName;}
    public Date getCreationDate() {return creationDate;}
    public String getCreatedBy() {return createdBy;}
    public  Date getLastUpdatedDate() {return lastUpdatedDate;}
    public String getLastUpdatedBy() {return  lastUpdatedBy;}
    public int getCountryId() {return  countryId;}

    public static ObservableList<String> getAllDivisionNames() {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        allDivisions.forEach(object -> {
            nameList.add(object.getDivisionName());
        });
        return nameList;
    }
    public static int getDivisionIdByName(String searchName) {
        List id = new ArrayList();
        allDivisions.forEach(object -> {
            if (object.getDivisionName() == searchName) {
                id.add(object.getDivisionId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return (int) id.get(0);
    }
    public static String  getDivisionNameById(int searchId) {
        List name = new ArrayList();
        allDivisions.forEach(object -> {
            if (object.getDivisionId() == searchId) {
                name.add(object.getDivisionName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0).toString();
    }

    public static ObservableList<String> getAllDivisionNamesByCountry(int country) {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        allDivisions.forEach(object -> {
            if (object.getCountryId() == country) {
                nameList.add(object.getDivisionName());
            }
        });
        return nameList;
    }

    public static int  getCountryIdByDivisionId(int searchId) {
        List id = new ArrayList();
        allDivisions.forEach(object -> {
            if (object.getDivisionId() == searchId) {
                id.add(object.getCountryId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return (int) id.get(0);
    }

    public static void getDatabaseDivisions() throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.first_level_divisions";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Division.addDivision(new Division(rs.getInt("Division_ID"),rs.getString("Division"),rs.getDate("Create_Date"), rs.getString("Created_By"), rs.getDate("Last_Update"), rs.getString("Last_Updated_By"), rs.getInt("Country_ID")));
        }
    }
}