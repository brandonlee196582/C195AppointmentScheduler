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
 * Country Class is developed to manage country data imported from the country table in the client_schedule database. Setters
 * are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
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
     * Sets reference variables used to refer to Country class constructor.
     *
     * @param countryId The country ID imported from the countries table in the client_schedule database
     * @param countryName The country name imported from the countries table in the client_schedule database
     * @param creationDate The country creation date and time imported from the countries table in the client_schedule database
     * @param createdBy The country created by username imported from the countries table in the client_schedule database
     * @param lastUpdatedDate The country last updated date and time imported from the countries table in the client_schedule database
     * @param lastUpdatedBy The country last update by username string imported from the countries table in the client_schedule database
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
     * Retries all countries from the allCountries ObservableList.
     *
     * @return Returns an observableList containing all countries.
     */
    public static ObservableList<Country> getAllCountries() {return allCountries;}

    /**
     * Adds a new country to the allCountries ObservableList.
     *
     * @param newCountry Country object to add to the allCountries ObservableList.
     */
    public static void addCountry(Country newCountry) {allCountries.add(newCountry);}

    /**
     * getter for the country id
     *
     * @return Returns the country id as an integer
     */
    public int getCountryId() {return countryId;}

    /**
     * getter for the country name
     *
     * @return Returns the country name as a string
     */
    public String getCountryName() {return countryName;}

    /**
     * getter for the country creation datetime
     *
     * @return Returns the country creation date and time as a timestamp
     */
    public Date getCreationDate() {return creationDate;}

    /**
     * getter for the country created by username
     *
     * @return Returns the country created by username as a string
     */
    public String getCreatedBy() {return createdBy;}

    /**
     * getter for the country last updated date and time
     * @return Returns the country last updated date and time as a timestamp
     */
    public  Date getLastUpdatedDate() {return lastUpdatedDate;}

    /**
     * getter for the country last updated by username
     *
     * @return Returns the country last update by username as a string
     */
    public String getLastUpdatedBy() {return  lastUpdatedBy;}

    /**
     * Gets an ObservableList containing all country names.
     *
     * @return Returns an ObservableList containing all country name strings.
     */
    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> countryNameList = FXCollections.observableArrayList();
        allCountries.forEach(object -> countryNameList.add(object.getCountryName()));
        return countryNameList;
    }

    /**
     * Retries the country id based on a passed in country name.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchName Country name to search for
     * @return Returns a country id as an integer based on a passed in country name
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
     * Retries the country name based on a passed in country id.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchId Country id to search for
     * @return Returns a country name string based on the passed in country id
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
     * Pulls a query of all country items in the countries table from the client_schedule database then creates country
     * objects for those items. Produces an error prompt if unable to retrieve data from the database.
     */
    public static void getDatabaseCountries() {
        String sql;

        try {
            sql = "SELECT * FROM client_schedule.countries";
            PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Country.addCountry(new Country(rs.getInt("Country_ID"),rs.getString("Country"),rs.getDate("Create_Date"), rs.getString("Created_By"), rs.getDate("Last_Update"), rs.getString("Last_Updated_By")));
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve countries from the database. See message for additional details: " + e.getMessage());
        }
    }
}
