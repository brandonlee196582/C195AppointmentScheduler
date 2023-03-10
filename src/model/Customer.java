package model;

import helper.DateTimeProcessing;
import helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import static helper.JDBC.connection;

/**
 * Customer Class is developed to manage customer data imported from the customers table in the client_schedule database
 * Setters are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
 * @author brandonLackey
 */
public class Customer {
    private final int customerId;
    private final int divisionId;
    private final String customerName, address, postalCode, phone, divisionName, countryName;
    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Sets reference variables used to refer to Customer class constructor.
     *
     * @param customerId The customer ID imported from the customers table in the client_schedule database
     * @param customerName The customer name imported from the customers table in the client_schedule database
     * @param address The customer address imported from the customers table in the client_schedule database
     * @param postalCode The customer postal code imported from the customers table in the client_schedule database
     * @param phone The customer phone number imported from the customers table in the client_schedule database
     * @param createDateTime The customer creation date and time imported from the customers table in the client_schedule database
     * @param createdBy The customer created by username string imported from the customers table in the client_schedule database
     * @param lastUpdateDateTime The customer last updated date and time imported from the customers table in the client_schedule database
     * @param lastUpdateBy The customer last update by username string imported from the customers table in the client_schedule database
     * @param divisionId The customer division ID imported from the customers table in the client_schedule database
     * @param divisionName The customer division name imported from the customers table in the client_schedule database
     * @param countryName The customer country name
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
     * Retries all customers from the allCustomers ObservableList.
     *
     * @return Returns an observableList containing all customers.
     */
    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}

    /**
     * Adds a new customer to the allCustomers ObservableList.
     *
     * @param newCustomer Customer object to add to allCustomers.
     */
    public static void addCustomer(Customer newCustomer) {allCustomers.add(newCustomer);}

    /**
     * Getter for the customer id.
     *
     * @return Returns a customer id integer
     */
    public int getCustomerId() {return customerId;}

    /**
     * Getter for the customer name
     *
     * @return Returns a customer name as a string
     */
    public String getCustomerName() {return customerName;}

    /**
     * Getter for the customer address
     *
     * @return Returns a  address as a string
     */
    public String getAddress() {return address;}

    /**
     * Getter for the customer postal code
     *
     * @return Returns a customer postal code as a string
     */
    public String getPostalCode() {return postalCode;}

    /**
     * Getter for the customer phone number
     *
     * @return Returns a customer phone number as a string
     */
    public String getPhone() {return phone;}

    /**
     * Getter for the customer division id
     *
     * @return Returns a customer division id as an integer
     */
    public int getDivisionId() {return divisionId;}

    /**
     * Getter for the customer division name
     *
     * @return Returns a customer division name as a string
     */
    public String getDivisionName() {return  divisionName;}

    /**
     * Getter for the customer country name
     *
     * @return Returns a customer country name as a string
     */
    public String getCountryName() {return countryName;}

    /**
     * Gets an ObservableList containing all customer names
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @return Returns an ObservableList containing all customer names as strings
     */
    public static ObservableList<String> getAllCustomerNames() {
        ObservableList<String> customerNameList = FXCollections.observableArrayList();
        allCustomers.forEach(object -> customerNameList.add(object.getCustomerName()));
        return customerNameList;
    }

    /**
     * Retries the customer id based on a passed in customer name.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     *function which will make my code easier to maintain.
     *
     * @param searchName Customer name to search for
     * @return Returns a customer id as an integer based on the passed in customer name
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
     * Retries the customer name based on a passed in customer id.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param customerId Customer id to search for
     * @return Returns a customer id as an integer based on the passed in customer name
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
     * Pulls a query of all customer items in the customers table from the client_schedule database then creates customer
     * objects for those items. Produces an error prompt if unable to retrieve data from the database.
     */
    public static void getDatabaseCustomers() {
        String sql;

        try {
            sql = "SELECT * FROM client_schedule.customers";
            PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String divisionString = Division.getDivisionNameById(rs.getInt("Division_ID"));
                int countryId = Division.getCountryIdByDivisionId(rs.getInt("Division_ID"));
                String countryString = Country.getCountryNameById(countryId);
                Customer.addCustomer(new Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),rs.getString("Address"),rs.getString("Postal_Code"),rs.getString("Phone"),rs.getDate("Create_Date"),rs.getString("Created_By"),rs.getTimestamp("Last_Update"),rs.getString("Last_Updated_By"),rs.getInt("Division_ID"),divisionString,countryString));
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve customers from the database. See message for additional details: " + e.getMessage());
        }
    }

    /**
     *
     * Commits changes or creates new customers in the customers table of the client_schedule database. Produces error prompt
     * if unable to retrieve data from the database.
     *
     * @param id Customer id integer
     * @param name Customer name string
     * @param address Customer address string
     * @param postalCode Customer postal code string
     * @param phone Customer phone number string
     * @param divisionId Customer division id integer
     * @param user Customer username string
     * @param menu Customer menu string
     * @param selectedCustomerId Selected customer id integer
     */
    public static void insertCustomer(int id, String name, String address, String postalCode, String phone, int divisionId, String user, String menu, int selectedCustomerId) {

        Timestamp timestampLocal = Timestamp.valueOf(LocalDateTime.now());
        Timestamp timestampUtc = DateTimeProcessing.exportTimeToUtc(timestampLocal);

        try {
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
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to commit customer to the database. See message for additional details: " + e.getMessage());
        }
    }

    /**
     * Removes a customer from the customer table in the client_schedule database. Produces an error prompt if unable to
     * remove the appointment from the database.
     *
     * @param id Customer ID for customer to be removed.
     */
    public static void remeoveCustomer(int id) {

        try {
            String sql = "DELETE FROM client_schedule.customers WHERE Customer_ID=" + id;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to remove customer from the database. See message for additional details: " + e.getMessage());
        }
    }
}
