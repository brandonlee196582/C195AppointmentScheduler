package com.company;

import helper.JDBC;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        String[] outColumns = {};

        JDBC.openConnection();

        outColumns = new String[]{"Customer_ID", "Customer_Name", "Address", "Postal_Code", "Phone"};
        populateTable(JDBC.queryData("customers", "", "", outColumns), "customers");
        outColumns = new String[]{"Appointment_ID", "Title", "Location"};
        populateTable(JDBC.queryData("appointments", "", "", outColumns), "appointments");
        outColumns = new String[]{"Contact_ID", "Contact_Name", "Email"};
        populateTable(JDBC.queryData("contacts", "", "", outColumns), "appointments");

        JDBC.closeConnection();
    }

    /**
     *
     * @param data Array of arrays containing table data
     * @param table String value of the table to populate
     */
    public static void populateTable(String[][] data, String table) {
        for (int rowIndex = 0; rowIndex < data.length; rowIndex++) {
            System.out.print("| ");
            for (int colIndex = 0; colIndex < data[rowIndex].length; colIndex++) {
                System.out.print(data[rowIndex][colIndex] + " | ");
            }
            System.out.print("\n");
        }
    }
}
