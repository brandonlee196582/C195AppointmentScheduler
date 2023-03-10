package model;

import helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static helper.JDBC.connection;

/**
 * Division Class is developed to manage user data imported from the users table in the client_schedule database. Setters
 * are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
 * @author brandonLackey
 */
public class User {
    int userId;
    String username;
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     * Sets reference variables used to refer to User class constructor.
     *
     * @param userId The user ID imported from the users table in the client_schedule database
     * @param username The username imported from the users table in the client_schedule database
     */
    private User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     * Retries all users from the allUsers ObservableList.
     *
     * @return Returns an observableList containing all users.
     */
    public static ObservableList<User> getAllUsers() {return allUsers;}

    /**
     * Adds a new user to the allUsers ObservableList.
     *
     * @param newUser User object to add to the allUsers ObservableList.
     */
    public static void addUser(User newUser) {allUsers.add(newUser);}

    /**
     * getter for the user id
     *
     * @return Returns the user id as an integer
     */
    public int getUserId() {return userId;}

    /**
     * getter for the username
     *
     * @return Returns the username as a string
     */
    public String getUsername() {return username;}

    /**
     * Retries the user id based on a passed in username.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchName Username to search for
     * @return Returns a user id as an integer based on a passed in username
     */
    public static Integer getUserIdFromName(String searchName) {
        List<Integer> id = new ArrayList<>();
        allUsers.forEach(object -> {
            if (object.getUsername().equals(searchName)) {
                id.add(object.getUserId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return id.get(0);
    }

    /**
     * Retries the username based on a passed in user id.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param id User id to search for
     * @return Returns a username as an integer based on a passed in user id
     */
    public static String getUserNameFromId(int id) {
        List<String> userList = new ArrayList<>();
        allUsers.forEach(object -> {
            if (object.getUserId() == id) {
                userList.add(object.getUsername());
            }
        });
        if (userList.isEmpty()) {
            return "";
        }
        return userList.get(0);
    }

    /**
     * Gets an ObservableList of all  strings
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @return Returns an ObservableList of all username strings
     */
    public static ObservableList<String> getAllUserNames() {
        ObservableList<String> userNameList = FXCollections.observableArrayList();
        allUsers.forEach(object -> userNameList.add(object.getUsername()));
        return userNameList;
    }

    /**
     * Pulls a query of all user items in the users table from the client_schedule database then creates user objects for
     * those items. Produces an error prompt if unable to retrieve data from the database.
     */
    public static void getDatabaseUsers() {
        String sql;

        try {
            sql = "SELECT * FROM client_schedule.users";
            PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User.addUser(new User(rs.getInt("User_ID"),rs.getString("User_Name")));
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve users from the database. See message for additional details: " + e.getMessage());
        }
    }
}
