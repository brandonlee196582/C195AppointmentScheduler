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
public class Division {
    int divisionId;
    String divisionName;
    Date creationDate;
    String createdBy;
    Date lastUpdatedDate;
    String lastUpdatedBy;
    int countryId;
    private static final ObservableList<Division> allDivisions = FXCollections.observableArrayList();

    /**
     *
     * @param divisionId
     * @param divisionName
     * @param creationDate
     * @param createdBy
     * @param lastUpdatedDate
     * @param lastUpdatedBy
     * @param countryId
     */
    private Division(int divisionId, String divisionName, Date creationDate, String createdBy, Date lastUpdatedDate, String lastUpdatedBy, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.creationDate = creationDate;
        this.createdBy = createdBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.countryId = countryId;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Division> getAllDivisions() {return allDivisions;}

    /**
     *
     * @param newDivision
     */
    public static void addDivision(Division newDivision) {allDivisions.add(newDivision);}

    /**
     *
     * @return
     */
    public int getDivisionId() {return divisionId;}

    /**
     *
     * @return
     */
    public String getDivisionName() {return divisionName;}

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
    public int getCountryId() {return  countryId;}

    /**
     *
     * @return
     */
    public static ObservableList<String> getAllDivisionNames() {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        allDivisions.forEach(object -> nameList.add(object.getDivisionName()));
        return nameList;
    }

    /**
     *
     * @param searchName
     * @return
     */
    public static int getDivisionIdByName(String searchName) {
        List<Integer> id = new ArrayList<>();
        allDivisions.forEach(object -> {
            if (object.getDivisionName().equals(searchName)) {
                id.add(object.getDivisionId());
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
    public static String  getDivisionNameById(int searchId) {
        List<String> name = new ArrayList<>();
        allDivisions.forEach(object -> {
            if (object.getDivisionId() == searchId) {
                name.add(object.getDivisionName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0);
    }

    /**
     *
     * @param country
     * @return
     */
    public static ObservableList<String> getAllDivisionNamesByCountry(int country) {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        allDivisions.forEach(object -> {
            if (object.getCountryId() == country) {
                nameList.add(object.getDivisionName());
            }
        });
        return nameList;
    }

    /**
     *
     * @param searchId
     * @return
     */
    public static int  getCountryIdByDivisionId(int searchId) {
        List<Integer> id = new ArrayList<>();
        allDivisions.forEach(object -> {
            if (object.getDivisionId() == searchId) {
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
     * @throws SQLException
     */
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