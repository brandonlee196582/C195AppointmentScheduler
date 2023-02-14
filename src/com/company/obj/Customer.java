package com.company.obj;

import dbConnection.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static dbConnection.JDBC.connection;

public class Customer {
    private int customerId;
    private String customerName, address, postalCode, phone;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer(int customerId, String customerName, String address, String postalCode, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
    }

    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}
    public static void addCustomer(Customer newCustomer) {allCustomers.add(newCustomer);}
    public int getCustomerId() {return customerId;}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public static void getDatabaseCustomers(String table, String[] outColumns) throws SQLException {
        String sql;
        
        sql = "SELECT * FROM client_schedule." + table;
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Customer.addCustomer(new com.company.obj.Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),rs.getString("Address"),rs.getString("Postal_Code"),rs.getString("Phone")));
        }
    }

    public static int insertCustomer(String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
