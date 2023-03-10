package model;

import helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static helper.JDBC.connection;

/**
 * Division Class is developed to manage division data imported from the division table in the client_schedule database.
 * Setters are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
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
     * Sets reference variables used to refer to Division class constructor.
     *
     * @param divisionId The division ID imported from the divisions table in the client_schedule database
     * @param divisionName The division name imported from the divisions table in the client_schedule database
     * @param creationDate The division creation date and time imported from the divisions table in the client_schedule database
     * @param createdBy The division created by username imported from the divisions table in the client_schedule database
     * @param lastUpdatedDate The division last updated date and time imported from the divisions table in the client_schedule database
     * @param lastUpdatedBy The division last updated by username imported from the divisions table in the client_schedule database
     * @param countryId The division country id imported from the divisions table in the client_schedule database
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
     * Retries all divisions from the allDivisions ObservableList.
     *
     * @return Returns an observableList containing all divisions.
     */
    public static ObservableList<Division> getAllDivisions() {return allDivisions;}

    /**
     * Adds a new division to the allDivisions ObservableList.
     *
     * @param newDivision Division object to add to the allDivisions ObservableList.
     */
    public static void addDivision(Division newDivision) {allDivisions.add(newDivision);}

    /**
     * getter for the division id
     *
     * @return Returns the division id as an integer
     */
    public int getDivisionId() {return divisionId;}

    /**
     * getter for the division name
     *
     * @return Returns the division name string
     */
    public String getDivisionName() {return divisionName;}

    /**
     * getter for the division creation date and time
     *
     * @return Returns the division date and time timestamp
     */
    public Date getCreationDate() {return creationDate;}

    /**
     * getter for the division created by username
     *
     * @return Returns the division created by username as an integer
     */
    public String getCreatedBy() {return createdBy;}

    /**
     * getter for the division last update date and time
     *
     * @return Returns the division last update date and time timestamp
     */
    public  Date getLastUpdatedDate() {return lastUpdatedDate;}

    /**
     * getter for the division last updated by username
     *
     * @return Returns the division last updated by username string
     */
    public String getLastUpdatedBy() {return  lastUpdatedBy;}

    /**
     * getter for the division country id
     *
     * @return Returns the division country id as an integer
     */
    public int getCountryId() {return  countryId;}

    /**
     * Gets an ObservableList containing all division names.
     *
     * @return Returns an ObservableList containing all division name strings.
     */
    public static ObservableList<String> getAllDivisionNames() {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        allDivisions.forEach(object -> nameList.add(object.getDivisionName()));
        return nameList;
    }

    /**
     * Retries the division id based on a passed in division name.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchName Division name to search for
     * @return Returns a division id as an integer based on a passed in division name
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
     * Retries the division name based on a passed in division id.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchId Division id to search for
     * @return Returns a division name as a string based on a passed in division id
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
     * Gets an ObservableList of all division names based on a passed in country id
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param country Country id to search for
     * @return Returns an ObservableList of all division name strings based on a passed in country id
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
     * Retries the country id based on a passed in division id.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchId Division id to search for
     * @return Returns a country id as an integer based on a passed in division id
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
     * Pulls a query of all division items in the divisions table from the client_schedule database then creates division
     * objects for those items. Produces an error prompt if unable to retrieve data from the database.
     */
    public static void getDatabaseDivisions() {
        String sql;

        try {
            sql = "SELECT * FROM client_schedule.first_level_divisions";
            PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Division.addDivision(new Division(rs.getInt("Division_ID"),rs.getString("Division"),rs.getDate("Create_Date"), rs.getString("Created_By"), rs.getDate("Last_Update"), rs.getString("Last_Updated_By"), rs.getInt("Country_ID")));
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve divisions from the database. See message for additional details: " + e.getMessage());
        }
    }
}