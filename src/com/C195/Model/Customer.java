package com.C195.Model;

import com.C195.helper.DateTimeProcessing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.C195.helper.JDBC.connection;

public class Customer {
    private int customerId, divisionId;
    private String customerName, address, postalCode, phone, createdBy, lastUpdateBy, divisionName, countryName;
    private Date createDateTime, lastUpdateDateTime;
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, Date createDateTime, String createdBy, Date lastUpdateDateTime, String lastUpdateBy, int divisionId, String divisionName, String countryName) {
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
        this.divisionName = divisionName;
        this.countryName = countryName;
    }
    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}
    public int getCustomerId() {return customerId;}
    public String getCustomerName() {return customerName;}
    public static void addCustomer(Customer newCustomer) {allCustomers.add(newCustomer);}
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public void setCustomerName(String customerName) {this.customerName = customerName;}
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}
    public String getPostalCode() {return postalCode;}
    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}
    public String getPhone() {return phone;}
    public void setPhone(String phone) {this.phone = phone;}
    public int getDivisionId() {return divisionId;}
    public String getDivisionName() {return  divisionName;}
    public String getCountryName() {return countryName;}

    public static ObservableList<String> getAllCustomerNames() {
        ObservableList<String> customerNameList = FXCollections.observableArrayList();
        allCustomers.forEach(object -> {
            customerNameList.add(object.getCustomerName());
        });
        return customerNameList;
    }
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
    public static String  getCustomerNameById(int customerId) {
        List name = new ArrayList();
        allCustomers.forEach(object -> {
            if (object.getCustomerId() == customerId) {
                name.add(object.getCustomerName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0).toString();
    }

    public static void getDatabaseCustomers() throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.customers";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String divisionString = Division.getDivisionNameById(rs.getInt("Division_ID"));
            int countryId = Division.getCountryIdByDivisionId(rs.getInt("Division_ID"));
            String countryString = Country.getCountryNameById(countryId);
            Customer.addCustomer(new Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),rs.getString("Address"),rs.getString("Postal_Code"),rs.getString("Phone"),rs.getDate("Create_Date"),rs.getString("Created_By"),rs.getTimestamp("Last_Update"),rs.getString("Last_Updated_By"),rs.getInt("Division_ID"),divisionString,countryString));
        }
    }

    public static int insertCustomer(int id, String name, String address, String postalCode, String phone, int divisionId, String user, String menu, int selectedCustomerId) throws SQLException {

        Timestamp timestampLocal = Timestamp.valueOf(LocalDateTime.now());
        Timestamp timestampUtc = DateTimeProcessing.exportTimeToUtc(timestampLocal);

        if (menu == "Add Customer") {

            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, timestampUtc);
            ps.setString(6,user);
            ps.setTimestamp(7, timestampUtc);
            ps.setString(8,user);
            ps.setInt(9, divisionId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        } else {
            //UPDATE client_schedule.customers SET Customer_Name='brandon' WHERE Customer_ID=36;
            String sql = "UPDATE client_schedule.customers SET Customer_Name='" + name + "', Address='" + address + "', Postal_Code='" + postalCode + "', Phone='" + phone + "', Last_Update='" + timestampUtc + "', Last_Updated_By='" + user + "', Division_ID=" + divisionId + " WHERE Customer_ID=" + selectedCustomerId;
            PreparedStatement ps = connection.prepareStatement(sql);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected;
        }
    }



    public static void remeoveCustomer(int id) throws SQLException {
            String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID=" + id;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
    }
}
