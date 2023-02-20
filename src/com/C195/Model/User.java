package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static com.C195.helper.JDBC.connection;

public class User {
    int userId;
    String username;
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    private User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }
    public static ObservableList<User> getAllUsers() {return allUsers;}
    public static void addUser(User newUser) {allUsers.add(newUser);}
    public int getUserId() {return userId;}
    public String getUsername() {return username;}

    public static ObservableList<String> getAllUserNames() {
        ObservableList<String> userNameList = FXCollections.observableArrayList();
        allUsers.forEach(object -> {
            userNameList.add(object.getUsername());
        });
        return userNameList;
    }

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
