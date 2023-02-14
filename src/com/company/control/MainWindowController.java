package com.company.control;

import com.company.Main;
import com.company.obj.Customer;
import dbConnection.JDBC;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> PostalCodeColumn;
    @FXML private TableColumn<Customer, String> PhoneColumn;
    @FXML private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws SQLException {
        String[] outColumns = {};
        customerTableView.getItems().clear();
        JDBC.openConnection();
        Customer.insertCustomer("Brandon Lackey", "3952 Heatherglenn Ln", "80104", "417-718-4964", 5);
        //welcomeText.setText("Welcome to JavaFX Application!");

        outColumns = new String[]{"Customer_ID", "Customer_Name", "Address", "Postal_Code", "Phone"};
        Customer.getDatabaseCustomers("customers", outColumns);

        JDBC.closeConnection();

        customerTableView.setItems(Customer.getAllCustomers());
        customerTableView.refresh();
    }

    @Override
    /**
     * Called to initialize a controller after its root element has been completely processed. -
     */
    public void initialize(URL location, ResourceBundle resources) {
        customerTableView.setItems(Customer.getAllCustomers());
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));



    }
}