package com.C195.control;

import com.C195.Model.Appointment;
import com.C195.Model.Customer;
import com.C195.helper.JDBC;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import java.util.*;

public class MainWindowController implements Initializable {



    //Table view window controls and labels
    @FXML AnchorPane tableViews;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn, addressColumn, PostalCodeColumn, PhoneColumn, divisionColumn, countryColumn;
    @FXML private TableView<Appointment> aptTableView;
    @FXML private TableColumn<Appointment, Integer> aptIDColumn, aptCustomerIdColumn, aptUserIdColumn;
    @FXML private TableColumn<Appointment, String> aptTitleColumn, aptLocationColumn, aptTypeColumn, aptDescriptionColumn, aptContactColumn;
    @FXML private TableColumn<Appointment, Timer> aptStartColumn;
    @FXML private TableColumn<Appointment, Date> aptEndColumn;
    @FXML Button logOutButton, aptAddButton, aptUpdateButton, aptRemoveButton, customerAddButton, customerUpdateButton, customerRemoveButton;
    @FXML Tab aptTabLabel, customerTabLabel;
    //---------

    //Add update appointment window controls and labels
    @FXML AnchorPane addUpdateAptBox;
    @FXML Button aptCancelButton, aptSubmitButton;
    @FXML ComboBox aptContactBox, aptCustomerBox;
    @FXML DatePicker aptStartDate, aptEndDate;
    @FXML Label aptIdLabel, aptUserIdLabel, addAptLabel, aptTitleLabel, aptContactLabel, aptContactIdLabel, aptLocationLabel, aptCustomerNameLabel, aptCustomerIdLabel, aptTypeLabel, aptStartLabel, aptStartHoursLabel, aptStartMinutesLabel, aptEndLabel1, aptEndHoursLabel, aptEndMinutesLabel, aptDescriptionLabel;
    @FXML Spinner aptStartHrs, aptStartMin, aptEndHrs1, aptEndMin1;
    @FXML TextArea aptDescBox;
    @FXML TextField aptIdBox, aptUserIdBox, aptTitleBox, aptLocationBox, aptTypeBox;
    //---------

    //Add update customer window controls and labels
    @FXML AnchorPane addUpdateCustomer;
    @FXML Button customerCancelButton, customerSubmitButton;
    @FXML ComboBox stateProvinceBox, countryBox;
    @FXML Label addUpdateCustomerIdLabel, addUpdateCustomerAddressLabel, addUpdateProvinceLabel, addUpdateCountryLabel, addUpdatePostalCodeLabel, addUpdatePhoneLabel, customerAddUpdateLabel;
    @FXML TextField customerIdBox, customerAddressBox, customerPostalCodeBox, customerPhoneNumberBox;
    //---------

    //Login window controls and labels
    @FXML AnchorPane loginBox;
    @FXML Button loginButton;
    @FXML Label loginLabel, locationLabel;
    @FXML TextField userName, passString;
    //---------
    private Stage stage;
    ZoneId systemZoneId;
    Locale sysLocale, frLocale, usLocale;
    ResourceBundle rb1;


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

    public void addAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateAptBox.setVisible(true);
        addAptLabel.setText("Add Appointment");

        aptCustomerBox.setItems(FXCollections.observableList(Customer.getAllCustomerNames()));
    }

    public void updateAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateAptBox.setVisible(true);
        addAptLabel.setText("Update Appointment");

        aptCustomerBox.setItems(FXCollections.observableList(Customer.getAllCustomerNames()));



        aptContactBox.setItems(FXCollections.observableList(Customer.getAllCustomerNames()));
    }

    public void customerGetIdByName(ActionEvent actionEvent) throws SQLException {
        int aptAddUpdateCustomerId = 0;
        Object customerName = (String) aptCustomerBox.getValue();
        aptAddUpdateCustomerId = Customer.getCustomerIdByName((String) customerName);
        aptCustomerIdLabel.setText(String.valueOf(aptAddUpdateCustomerId));
    }

    public void removeAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println("removed appointment");
    }

    public void addCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Add Customer");
    }

    public void updateCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Update Customer");
    }

    public void removeCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println("removed customer");
    }

    public void cancelCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateCustomer.setVisible(false);
    }

    public void submitCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println("added customer");
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateCustomer.setVisible(false);
    }

    public void submitAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println("added appointment");
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);

        aptIdBox.setText("");
        aptUserIdBox.setText("");
        aptTitleBox.setText("");
        aptLocationBox.setText("");
        aptTypeBox.setText("");
        aptContactBox.setValue(0);
        //aptCustomerBox.se
    }

    public void cancelAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);
    }

    public void logInButtonClicked(ActionEvent actionEvent) throws SQLException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        String userNameText = userName.getText();
        String passwordText = passString.getText();
        String loginMessage = JDBC.openConnection(userNameText, passwordText);

        if (loginMessage.contains("Access denied")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (sysLocale.getLanguage() == frLocale.getLanguage()) {
                alert.setContentText("Informations de connexion incorrectes!");
            } else {
                alert.setContentText("Incorrect login information!");
            }
            alert.showAndWait();
        } else {
            loginBox.setVisible(false);
            tableViews.setDisable(false);
            logOutButton.setDisable(false);

            Customer.getDatabaseCustomers();
            Appointment.getDatabaseApts();

            customerTableView.setItems(Customer.getAllCustomers());
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
            PostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            aptTableView.setItems(Appointment.getAllapts());
            aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            aptTitleColumn.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
            aptLocationColumn.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
            aptContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactString"));
            aptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("aptType"));
            aptStartColumn.setCellValueFactory(new PropertyValueFactory<>("aptStartDateTime"));
            aptEndColumn.setCellValueFactory(new PropertyValueFactory<>("aptEndDateTime"));
            aptCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            aptUserIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            aptContactColumn.setCellValueFactory(new PropertyValueFactory<>("contactString"));

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (sysLocale.getLanguage() == frLocale.getLanguage()) {
                    alert.setContentText("Vous devez vous déconnecter avant de quitter.");
                } else {
                    alert.setContentText("You must log out before you exit.");
                }
                alert.showAndWait();
                event.consume();
            });
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        systemZoneId = ZoneId.systemDefault();
        locationLabel.setText("Current Location: " + systemZoneId.toString());
        frLocale = new Locale("fr", "FR");
        usLocale = new Locale("en", "US");
        sysLocale = Locale.getDefault();
        userName.setText("sqlUser");
        passString.setText("Passw0rd!");

        if (sysLocale.getLanguage() == frLocale.getLanguage()) {
            rb1 = ResourceBundle.getBundle("com.C195.resources.Lang", frLocale);
            //Login window language change
            loginLabel.setText(rb1.getString("login"));
            loginButton.setText(rb1.getString("login"));
            userName.setPromptText(rb1.getString("username"));
            passString.setPromptText(rb1.getString("password"));
            locationLabel.setText(rb1.getString("currentLocation") + ": " + systemZoneId.toString());

            //appointment window language change
            aptIDColumn.setText(rb1.getString("appointment") + " ID");
            aptTitleColumn.setText(rb1.getString("title"));
            aptLocationColumn.setText(rb1.getString("location"));
            aptStartColumn.setText(rb1.getString("start"));
            aptEndColumn.setText(rb1.getString("end"));
            aptTabLabel.setText(rb1.getString("appointment"));
            aptAddButton.setText(rb1.getString("add"));
            aptUpdateButton.setText(rb1.getString("update"));
            aptUpdateButton.setLayoutX(65);
            aptRemoveButton.setText(rb1.getString("remove"));
            aptRemoveButton.setLayoutX(145);

            //customer window language change
            customerIDColumn.setText(rb1.getString("customer") + " ID");
            customerNameColumn.setText(rb1.getString("customerName"));
            addressColumn.setText(rb1.getString("address"));
            PostalCodeColumn.setText(rb1.getString("postalCode"));
            PhoneColumn.setText(rb1.getString("phoneNumber"));
            customerTabLabel.setText(rb1.getString("customer"));
            customerAddButton.setText(rb1.getString("add"));
            customerUpdateButton.setText(rb1.getString("update"));
            customerUpdateButton.setLayoutX(65);
            customerRemoveButton.setText(rb1.getString("remove"));
            customerRemoveButton.setLayoutX(145);

            //Global window language change
            logOutButton.setText(rb1.getString("logout"));
            logOutButton.setLayoutX(542);
        }
    }
}