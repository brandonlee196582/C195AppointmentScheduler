package helper;

import java.sql.*;

/**
 * Class containing open and close database methods.
 *
 * @author brandonLackey
 */
public abstract class JDBC {
    /**
     * JDBC Connection Class object
     */
    public static Connection connection;

    /**
     *  Opens a mysql connection to the client_schedule database.
     *
     * @param userName The database login username.
     * @param password The database login password.
     */
    public static void openConnection(String userName, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/client_schedule", userName, password);
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to open database connection. See message for additional details: " + e.getMessage());
        }
    }

    /**
     * Closses the mysql connection to the client_schedule database.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to close database connection. See message for additional details: " + e.getMessage());
        }
    }
}
