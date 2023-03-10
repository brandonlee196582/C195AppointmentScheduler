package model;

import helper.promptHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static helper.JDBC.connection;

/**
 * Contact Class is developed to manage contacts data imported from the contacts table in the client_schedule database Setters
 * are not defined because all item changes and additions are intended to be managed in the database not the objects.
 *
 * @author brandonLackey
 */
public class Contact {
    private final int contactId;
    private final String contactName, contactEmail;
    private static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * Sets reference variables used to refer to Contact class constructor.
     *
     * @param contactId The contact ID imported from the contacts table in the client_schedule database
     * @param contactName The contact name imported from the contacts table in the client_schedule database
     * @param contactEmail The contact email imported from the contacts table in the client_schedule database
     */
    private Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * Retries all contacts from the allContacts ObservableList.
     *
     * @return Returns an observableList containing all contacts.
     */
    public static ObservableList<Contact> getAllContacts() {return allContacts;}

    /**
     * Adds a new contact to the allContacts ObservableList.
     *
     * @param newContact Contact object to add to the allContacts ObservableList.
     */
    public static void addContact(Contact newContact) {allContacts.add(newContact);}

    /**
     * Getter to retrieve the contact id.
     *
     * @return Returns the contact as an integer.
     */
    public int getContactId() {return contactId;}

    /**
     * Getter to retrieve the contact name.
     *
     * @return Returns the contact name as a string.
     */
    public String getContactName() {return contactName;}

    /**
     * Getter to retrieve the contact email.
     *
     * @return Returns the contact email as a string.
     */
    public String getContactEmail() {return contactEmail;}

    /**
     * Gets an ObservableList containing all contact names.
     *
     * @return Returns an ObservableList containing all contact name strings.
     */
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNameList = FXCollections.observableArrayList();
        allContacts.forEach(object -> contactNameList.add(object.getContactName()));
        return contactNameList;
    }

    /**
     * Gets the contact id from a passed in contact name.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param searchName Name of the contact to search for.
     * @return Returns an integer of the contact id based on the passed in contact name.
     */
    public static int getContactIdByName(String searchName) {
        List<Integer> id = new ArrayList<>();
        allContacts.forEach(object -> {
            if (object.getContactName().equals(searchName)) {
                id.add(object.getContactId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return id.get(0);
    }

    /**
     * Gets the contact id from a passed in contact name.
     *
     * Lambda Expression: I used a lambda expression here to improve the readability of my for loop reducing potential bugs
     * in the code. This expression is also more concise meaning there is slightly less code required to perform the same
     * function which will make my code easier to maintain.
     *
     * @param contactId Contact id of the contact to search for.
     * @return Returns a string of the contact name based on the passed in contact id.
     */
    public static String getContactNameById(int contactId) {
        List<String> name = new ArrayList<>();
        allContacts.forEach(object -> {
            if (object.getContactId() == contactId) {
                name.add(object.getContactName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0);
    }

    /**
     * Pulls a query of all contact items in the contacts table from the client_schedule database then creates contact objects
     * for those items. Produces an error prompt if unable to retrieve data from the database.
     *
     */
    public static void getDatabaseContacts() {
        String sql;

        try {
            sql = "SELECT * FROM client_schedule.contacts";
            PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Contact.addContact(new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));
            }
        } catch(Exception e) {
            promptHelper.errorDialog("Database Connection Error", "Unable to retrieve contacts from the database. See message for additional details: " + e.getMessage());
        }
    }
}
