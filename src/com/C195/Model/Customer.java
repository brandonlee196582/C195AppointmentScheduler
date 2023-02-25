package com.C195.Model;

import com.C195.helper.DateTimeProcessing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import static com.C195.helper.JDBC.connection;

/**
 * @author brandonLackey
 */
public class Customer {
    private final int customerId;
    private final int divisionId;
    private final String customerName, address, postalCode, phone, divisionName, countryName;
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     *
     * @param customerId
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param createDateTime
     * @param createdBy
     * @param lastUpdateDateTime
     * @param lastUpdateBy
     * @param divisionId
     * @param divisionName
     * @param countryName
     */
    public Customer(int customerId, String customerName, String address, String postalCode, String phone, Date createDateTime, String createdBy, Date lastUpdateDateTime, String lastUpdateBy, int divisionId, String divisionName, String countryName) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryName = countryName;
    }

    /**
     *
     * @return
     */
    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}

    /**
     *
     * @return
     */
    public int getCustomerId() {return customerId;}

    /**
     *
     * @return
     */
    public String getCustomerName() {return customerName;}

    /**
     *
     * @param newCustomer
     */
    public static void addCustomer(Customer newCustomer) {allCustomers.add(newCustomer);}

    /**
     *
     * @return
     */
    public String getAddress() {return address;}

    /**
     *
     * @return
     */
    public String getPostalCode() {return postalCode;}

    /**
     *
     * @return
     */
    public String getPhone() {return phone;}

    /**
     *
     * @return
     */
    public int getDivisionId() {return divisionId;}

    /**
     *
     * @return
     */
    public String getDivisionName() {return  divisionName;}

    /**
     *
     * @return
     */
    public String getCountryName() {return countryName;}

    /**
     *
     * @return
     */
    public static ObservableList<String> getAllCustomerNames() {
        ObservableList<String> customerNameList = FXCollections.observableArrayList();
        allCustomers.forEach(object -> customerNameList.add(object.getCustomerName()));
        return customerNameList;
    }

    /**
     *
     * @param searchName
     * @return
     */
    public static int getCustomerIdByName(String searchName) {
        List<Integer> id = new ArrayList<>();
        allCustomers.forEach(object -> {
            if (object.getCustomerName().equals(searchName)) {
                id.add(object.getCustomerId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return id.get(0);
    }

    /**
     *
     * @param customerId
     * @return
     */
    public static String  getCustomerNameById(int customerId) {
        List<String> name = new ArrayList<>();
        allCustomers.forEach(object -> {
            if (object.getCustomerId() == customerId) {
                name.add(object.getCustomerName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0);
    }

    /**
     *
     * @throws SQLException
     */
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

    /**
     *
     * @param id
     * @param name
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionId
     * @param user
     * @param menu
     * @param selectedCustomerId
     * @throws SQLException
     */
    public static void insertCustomer(int id, String name, String address, String postalCode, String phone, int divisionId, String user, String menu, int selectedCustomerId) throws SQLException {

        Timestamp timestampLocal = Timestamp.valueOf(LocalDateTime.now());
        Timestamp timestampUtc = DateTimeProcessing.exportTimeToUtc(timestampLocal);

        if (Objects.equals(menu, "Add Customer")) {
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
            ps.executeUpdate();
        } else {
            String sql = "UPDATE client_schedule.customers SET Customer_Name='" + name + "', Address='" + address + "', Postal_Code='" + postalCode + "', Phone='" + phone + "', Last_Update='" + timestampUtc + "', Last_Updated_By='" + user + "', Division_ID=" + divisionId + " WHERE Customer_ID=" + selectedCustomerId;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        }
    }

    /**
     *
     * @param id
     * @throws SQLException
     */
    public static void remeoveCustomer(int id) throws SQLException {
            String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID=" + id;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
    }
}
