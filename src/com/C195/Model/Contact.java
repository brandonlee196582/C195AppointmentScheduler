package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.C195.helper.JDBC.connection;

/**
 * @author brandonLackey
 */
public class Contact {
    private final int contactId;
    private final String contactName, contactEmail;
    private static final ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     *
     * @param contactId
     * @param contactName
     * @param contactEmail
     */
    private Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * .
     * @return
     */
    public static ObservableList<Contact> getAllContacts() {return allContacts;}

    /**
     *
     * @param newContact
     */
    public static void addContact(Contact newContact) {allContacts.add(newContact);}

    /**
     *
     * @return
     */
    public int getContactId() {return contactId;}

    /**
     *
     * @return
     */
    public String getContactName() {return contactName;}

    /**
     *
     * @return
     */
    public String getContactEmail() {return contactEmail;}

    /**
     *
     * @return
     */
    public static ObservableList<String> getAllContactNames() {
        ObservableList<String> contactNameList = FXCollections.observableArrayList();
        allContacts.forEach(object -> contactNameList.add(object.getContactName()));
        return contactNameList;
    }

    /**
     *
     * @param searchName
     * @return
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
     *
     * @param contactId
     * @return
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
     *
     * @throws SQLException
     */
    public static void getDatabaseContacts() throws SQLException {
        String sql;

        sql = "SELECT * FROM client_schedule.contacts";
        PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Contact.addContact(new Contact(rs.getInt("Contact_ID"),rs.getString("Contact_Name"),rs.getString("Email")));
        }
    }
}
