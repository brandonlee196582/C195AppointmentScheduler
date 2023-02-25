package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.C195.helper.JDBC.connection;

/**
 * @author brandonLackey
 */
public class User {
    int userId;
    String username;
    private static final ObservableList<User> allUsers = FXCollections.observableArrayList();

    /**
     *
     * @param userId
     * @param username
     */
    private User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    /**
     *
     * @return
     */
    public static ObservableList<User> getAllUsers() {return allUsers;}

    /**
     *
     * @param newUser
     */
    public static void addUser(User newUser) {allUsers.add(newUser);}

    /**
     *
     * @return
     */
    public int getUserId() {return userId;}

    /**
     *
     * @return
     */
    public String getUsername() {return username;}

    /**
     *
     * @param searchName
     * @return
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
     *
     * @param id
     * @return
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
     *
     * @return
     */
    public static ObservableList<String> getAllUserNames() {
        ObservableList<String> userNameList = FXCollections.observableArrayList();
        allUsers.forEach(object -> userNameList.add(object.getUsername()));
        return userNameList;
    }

    /**
     *
     * @throws SQLException
     */
    public static void getDatabaseUsers() throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.users";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            User.addUser(new User(rs.getInt("User_ID"),rs.getString("User_Name")));
        }
    }
}
