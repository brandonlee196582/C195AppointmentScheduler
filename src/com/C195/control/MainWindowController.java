package com.C195.control;

import com.C195.Model.Appointment;
import com.C195.Model.Customer;
import com.C195.helper.JDBC;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;

public class MainWindowController implements Initializable {

    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> PostalCodeColumn;
    @FXML private TableColumn<Customer, String> PhoneColumn;
    @FXML private TableView<Appointment> aptTableView;
    @FXML private TableColumn<Customer, Integer> aptIDColumn;
    @FXML private TableColumn<Customer, String> aptTitleColumn;
    @FXML private TableColumn<Customer, String> aptLocationColumn;
    @FXML private TableColumn<Customer, Timer> aptStartColumn;
    @FXML private TableColumn<Customer, Date> aptEndColumn;
    @FXML AnchorPane loginBox;
    @FXML AnchorPane tableViews;
    @FXML TextField userName;
    @FXML Button logOutButton;
    @FXML PasswordField passString;
    @FXML Label systemZoneIdLabel;
    private Stage stage;
    ZoneId systemZoneId;


    @FXML
    protected void logOutButtonClick(ActionEvent actionEvent) {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        userName.setText("");
        passString.setText("");
        tableViews.setDisable(true);
        loginBox.setVisible(true);
        logOutButton.setDisable(true);
        aptTableView.getItems().clear();
        customerTableView.getItems().clear();
        JDBC.closeConnection();

        stage.setOnCloseRequest(event -> {
            Platform.exit();
        });
    }

    public void logInButtonClicked(ActionEvent actionEvent) throws SQLException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        String userNameText = userName.getText();
        String passwordText = passString.getText();
        String loginMessage = JDBC.openConnection(userNameText, passwordText);

        if (loginMessage.contains("Access denied")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Incorrect login information!");
            alert.showAndWait();
        } else {
            loginBox.setVisible(false);
            tableViews.setDisable(false);
            logOutButton.setDisable(false);

            String[] outColumns = {};

            outColumns = new String[]{"Customer_ID", "Customer_Name", "Address", "Postal_Code", "Phone"};
            Customer.getDatabaseCustomers("customers", outColumns);
            Appointment.getDatabaseApts();

            customerTableView.setItems(Customer.getAllCustomers());
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            aptTableView.setItems(Appointment.getAllapts());
            aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            aptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptLocationColumn.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
            aptStartColumn.setCellValueFactory(new PropertyValueFactory<>("aptStartDateTime"));
            aptEndColumn.setCellValueFactory(new PropertyValueFactory<>("aptEndDateTime"));

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("You must log out before you exit.");
                alert.showAndWait();
                event.consume();
            });
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        systemZoneId = ZoneId.systemDefault();
        systemZoneIdLabel.setText(systemZoneId.toString());
    }
}