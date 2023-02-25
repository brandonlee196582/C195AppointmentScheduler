package com.C195.helper;

import java.sql.*;

/**
 * @author brandonLackey
 */
public abstract class JDBC {

    public static Connection connection;

    /**
     *
     * @param userName
     * @param password
     */
    public static void openConnection(String userName, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule?connectionTimeZone = SERVER", userName, password);
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to open database connection. See message for additional details: " + e.getMessage());
        }
    }

    /**
     *
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to close database connection. See message for additional details: " + e.getMessage());
        }
    }
}
