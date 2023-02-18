package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.C195.helper.JDBC.connection;

public class Customer {
    private int customerId,divisionId;
    private String customerName, address, postalCode, phone, createdBy, lastUpdateBy, division, country;
    private Date createDateTime, lastUpdateDateTime;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, Date createDateTime, String createdBy, Date lastUpdateDateTime, String lastUpdateBy, int divisionId, String division, String country) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDateTime = createDateTime;
        this.createdBy = createdBy;
        this.lastUpdateDateTime = lastUpdateDateTime;
        this.lastUpdateBy = lastUpdateBy;
        this.divisionId = divisionId;
        this.division = division;
        this.country = country;
    }

    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}
    public static List getAllCustomerNames() {
        List customerNameList = new ArrayList();
        allCustomers.forEach(object -> {
            customerNameList.add(object.getCustomerName());
        });
        return customerNameList;
    }
    public static void addCustomer(Customer newCustomer) {allCustomers.add(newCustomer);}
    public int getCustomerId() {return customerId;}
    public static int getCustomerIdByName(String searchName) {
        List id = new ArrayList();
        allCustomers.forEach(object -> {
            if (object.getCustomerName() == searchName) {
                id.add(object.getCustomerId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return (int) id.get(0);
    }
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public String getCustomerName() {return customerName;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public String getAddress() {return address;}
    public String getDivision() {return division;}
    public String getCountry() {return country;}
    public void setAddress(String address) {this.address = address;}
    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}

    public static void getDatabaseCustomers() throws SQLException {
        int divisionId = 0, countryId = 0;
        String sql, divisionSql, countrySql, division = "", country = "";

        sql = "SELECT * FROM client_schedule.customers";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            //SELECT * FROM client_schedule.contacts where Contact_ID=2;
            divisionId = rs.getInt("Division_ID");
            divisionSql = "SELECT * FROM client_schedule.first_level_divisions where Division_ID=" + divisionId;
            PreparedStatement contactPs = connection.prepareStatement(divisionSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet contactRs = contactPs.executeQuery();
            while(contactRs.next()){
                division = contactRs.getString("Division");
                countryId = contactRs.getInt("Country_ID");
            }
            countrySql = "SELECT * FROM client_schedule.countries where Country_ID=" + countryId;
            PreparedStatement countryPs = connection.prepareStatement(countrySql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet countryRs = countryPs.executeQuery();
            while(countryRs.next()){
                country = countryRs.getString("Country");
            }
            Customer.addCustomer(new Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),rs.getString("Address"),rs.getString("Postal_Code"),rs.getString("Phone"),rs.getDate("Create_Date"),rs.getString("Created_By"),rs.getTimestamp("Last_Update"),rs.getString("Last_Updated_By"),divisionId,division,country));
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
