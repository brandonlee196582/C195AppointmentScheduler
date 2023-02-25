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

/**
 * @author brandonLackey
 */
public class Country {
    int countryId;
    String countryName;
    Date creationDate;
    String createdBy;
    Date lastUpdatedDate;
    String lastUpdatedBy;
    private static final ObservableList<Country> allCountries = FXCollections.observableArrayList();

    /**
     *
     * @param countryId
     * @param countryName
     * @param creationDate
     * @param createdBy
     * @param lastUpdatedDate
     * @param lastUpdatedBy
     */
    private Country(int countryId, String countryName, Date creationDate, String createdBy, Date lastUpdatedDate, String lastUpdatedBy) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Country> getAllCountries() {return allCountries;}

    /**
     *
     * @param newCountry
     */
    public static void addCountry(Country newCountry) {allCountries.add(newCountry);}

    /**
     *
     * @return
     */
    public int getCountryId() {return countryId;}

    /**
     *
     * @return
     */
    public String getCountryName() {return countryName;}

    /**
     *
     * @return
     */
    public Date getCreationDate() {return creationDate;}

    /**
     *
     * @return
     */
    public String getCreatedBy() {return createdBy;}

    /**
     *
     * @return
     */
    public  Date getLastUpdatedDate() {return lastUpdatedDate;}

    /**
     *
     * @return
     */
    public String getLastUpdatedBy() {return  lastUpdatedBy;}

    /**
     *
     * @return
     */
    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> countryNameList = FXCollections.observableArrayList();
        allCountries.forEach(object -> countryNameList.add(object.getCountryName()));
        return countryNameList;
    }

    /**
     *
     * @param searchName
     * @return
     */
    public static int getCountryIdByName(String searchName) {
        List<Integer> id = new ArrayList<>();
        allCountries.forEach(object -> {
            if (object.getCountryName().equals(searchName)) {
                id.add(object.getCountryId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return id.get(0);
    }

    /**
     *
     * @param searchId
     * @return
     */
    public static String  getCountryNameById(int searchId) {
        List<String> name = new ArrayList<>();
        allCountries.forEach(object -> {
            if (object.getCountryId() == searchId) {
                name.add(object.getCountryName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0);
    }

    /**
     *
     * @throws SQLException
     */
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
