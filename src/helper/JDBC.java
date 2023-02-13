package helper;

import java.sql.*;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     *
     * @param table String value of table to pull data from
     * @param whereRef String value of SQL where condition, will query entire table if left blank
     * @param whereVal String value of the SQL query value
     * @param outColumns Array containing table columns to display
     * @return
     * @throws SQLException
     */
    public static String[][] queryData(String table, String whereRef, String whereVal, String[] outColumns) throws SQLException {
        String sql;
        String[] rowArr = {};
        Integer colCount;
        String[][] outArr = {};
        Integer rowCount;


        if (whereRef == "") {
            System.out.println("all entries in " + table + " table.");
            sql = "SELECT * FROM client_schedule." + table;
        } else {
            System.out.println("specific entry in " + table + " table.");
            sql = "SELECT * FROM client_schedule." + table + " WHERE " + whereRef + " = " + '"' + whereVal + '"';
        }
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        rs.last();

        outArr = new String[rs.getRow()][];
        rowArr = new String[outColumns.length];
        rowCount = 0;
        rs.beforeFirst();
        while(rs.next()){
            colCount = 0;
            for (String element : outColumns) {
                rowArr[colCount] = rs.getString(element);
                colCount++;
            }
            outArr[rowCount] = rowArr;
            rowArr = new String[outColumns.length];
            rowCount++;
        }
        return outArr;
    }

}
