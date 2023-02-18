package com.C195.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.C195.helper.JDBC.connection;

public class Contact {
    private int contactId;
    private String contactName, contactEmail;
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    private Contact(int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }
    public static ObservableList<Contact> getAllContacts() {return allContacts;}
    public static void addContact(Contact newContact) {allContacts.add(newContact);}
    public int getContactId() {return contactId;}
    public String getContactName() {return contactName;}
    public String getContactEmail() {return contactEmail;}
    public static List getAllContactNames() {
        List contactNameList = new ArrayList();
        allContacts.forEach(object -> {
            contactNameList.add(object.getContactName());
        });
        return contactNameList;
    }
    public static int getContactIdByName(String searchName) {
        List id = new ArrayList();
        allContacts.forEach(object -> {
            if (object.getContactName() == searchName) {
                id.add(object.getContactId());
            }
        });
        if (id.isEmpty()) {
            return 0;
        }
        return (int) id.get(0);
    }
    public static String  getContactNameById(int contactId) {
        List name = new ArrayList();
        allContacts.forEach(object -> {
            if (object.getContactId() == contactId) {
                name.add(object.getContactName());
            }
        });
        if (name.isEmpty()) {
            return "unknown";
        }
        return name.get(0).toString();
    }
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
