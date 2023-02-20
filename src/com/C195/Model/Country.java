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

public class Country {
    int countryId;
    String countryName;
    Date creationDate;
    String createdBy;
    Date lastUpdatedDate;
    String lastUpdatedBy;
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    private Country(int countryId, String countryName, Date creationDate, String createdBy, Date lastUpdatedDate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
    }
    public static ObservableList<Country> getAllCountries() {return allCountries;}
    public static void addCountry(Country newCountry) {allCountries.add(newCountry);}
    public int getCountryId() {return countryId;}
    public String getCountryName() {return countryName;}
    public Date getCreationDate() {return creationDate;}
    public String getCreatedBy() {return createdBy;}
    public  Date getLastUpdatedDate() {return lastUpdatedDate;}
    public String getLastUpdatedBy() {return  lastUpdatedBy;}
    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> countryNameList = FXCollections.observableArrayList();
        allCountries.forEach(object -> {
            countryNameList.add(object.getCountryName());
        });
        return countryNameList;
    }
    public static int getCountryIdByName(String searchName) {
        List<Integer> id = new ArrayList();
        allCountries.forEach(object -> {
            if (object.getCountryName() == searchName) {
                id.add(object.getCountryId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return id.get(0);
    }
    public static String  getCountryNameById(int searchId) {
        List name = new ArrayList();
        allCountries.forEach(object -> {
            if (object.getCountryId() == searchId) {
                name.add(object.getCountryName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0).toString();
    }

    public static void getDatabaseCountries() throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.countries";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Country.addCountry(new Country(rs.getInt("Country_ID"),rs.getString("Country"),rs.getDate("Create_Date"), rs.getString("Created_By"), rs.getDate("Last_Update"), rs.getString("Last_Updated_By")));
        }
    }
}
