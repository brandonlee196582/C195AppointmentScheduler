package com.C195.control;

import com.C195.Model.*;
import com.C195.helper.DateTimeProcessing;
import com.C195.helper.JDBC;
import com.C195.helper.promptHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
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
    @FXML TextArea aptDescBox;
    @FXML TextField aptIdBox, aptUserIdBox, aptTitleBox, aptLocationBox, aptTypeBox, aptStartHrs, aptStartMin, aptEndHrs, aptEndMin;
    //---------

    //Add update customer window controls and labels
    @FXML AnchorPane addUpdateCustomer;
    @FXML Button customerCancelButton, customerSubmitButton;
    @FXML ComboBox stateProvinceBox, countryBox;
    @FXML Label addUpdateCustomerIdLabel, addUpdateCustomerNameLabel, addUpdateCustomerAddressLabel, addUpdateProvinceLabel, addUpdateProvinceIdLabel, addUpdateCountryLabel, addUpdateCountryIdLabel, addUpdatePostalCodeLabel, addUpdatePhoneLabel, customerAddUpdateLabel;
    @FXML TextField customerIdBox, customerNameBox, customerAddressBox, customerPostalCodeBox, customerPhoneNumberBox;
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


    //Login Window Functions
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

            Contact.getDatabaseContacts();
            Division.getDatabaseDivisions();
            Country.getDatabaseCountries();
            Appointment.getDatabaseApts();
            Customer.getDatabaseCustomers();

            customerTableView.setItems(Customer.getAllCustomers());
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
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
    //----------------------

    //Main Window Functions
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
        aptContactBox.setItems(FXCollections.observableList(Contact.getAllContactNames()));
    }

    public void updateAptButtonClicked(ActionEvent actionEvent) throws SQLException {

        Appointment selectedApt = aptTableView.getSelectionModel().getSelectedItem();
        if (selectedApt == null) {
            promptHelper.errorDialog("No appointment selected", "Please select an appointment to update.");
            return;
        }

        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateAptBox.setVisible(true);
        addAptLabel.setText("Update Appointment");

        setAppointment(selectedApt);

        aptCustomerBox.setItems(FXCollections.observableList(Customer.getAllCustomerNames()));
        aptContactBox.setItems(FXCollections.observableList(Contact.getAllContactNames()));
    }

    public void removeAptButtonClicked(ActionEvent actionEvent) throws SQLException, ParseException {




        System.out.println("removed appointment");
    }

    public void addCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Add Customer");

        stateProvinceBox.setItems(FXCollections.observableList(Division.getAllDivisionNames()));
        countryBox.setItems(FXCollections.observableList(Country.getAllCountryNames()));
    }

    public void updateCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            promptHelper.errorDialog("No customer selected", "Please select a customer to update.");
            return;
        }

        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Update Customer");

        setCustomer(selectedCustomer);

        stateProvinceBox.setItems(FXCollections.observableList(Division.getAllDivisionNames()));
        countryBox.setItems(FXCollections.observableList(Country.getAllCountryNames()));
    }

    public void removeCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        String appointmentsScheduled = " | ";

        for (Appointment apt : Appointment.getAllapts()) {
            if (apt.getCustomerId() == selectedCustomer.getCustomerId()) {
                appointmentsScheduled = appointmentsScheduled + apt.getAptId() + " | ";
            }
        }
        if (appointmentsScheduled != " | ") {
            promptHelper.errorDialog("Unable to remove customer","Customers with scheduled appointments can't be remove. Customer is scheduled for the following appointments: " + appointmentsScheduled);
        } else {
            if (promptHelper.confirmPrompt("Remove Customer Confirmation","Are you sure you would like to remove " + selectedCustomer.getCustomerName() + "'s customer record?")) {

                Customer.remeoveCustomer(selectedCustomer.getCustomerId());
                customerTableView.getItems().clear();
                Customer.getDatabaseCustomers();
            }
        }
    }

    //----------MainWindow------------

    //Add-Update Appointment Window Functions

    public void submitAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        System.out.println("added appointment");
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);

        cleanAptForm();
    }

    public void cancelAptButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);

        cleanAptForm();
    }

    public void setAppointment(Appointment selectedApt) {
        aptIdBox.setText(Integer.toString(selectedApt.getAptId()));
        aptUserIdBox.setText(Integer.toString(selectedApt.getUserId()));
        aptTitleBox.setText(selectedApt.getAptTitle());
        aptLocationBox.setText(selectedApt.getAptLocation());
        aptTypeBox.setText(selectedApt.getAptType());
        aptContactBox.setValue(Contact.getContactNameById(selectedApt.getContactId()));
        aptCustomerBox.setValue(Customer.getCustomerNameById(selectedApt.getCustomerId()));
        aptContactIdLabel.setText(Integer.toString(selectedApt.getContactId()));
        aptCustomerIdLabel.setText(Integer.toString(selectedApt.getCustomerId()));
        String[] startDateArray = DateTimeProcessing.splitDateTime(selectedApt.getAptStartDateTime());
        aptStartDate.getEditor().setText(startDateArray[0]);
        aptStartHrs.setText(startDateArray[1]);
        aptStartMin.setText(startDateArray[2]);
        String[] endDateArray = DateTimeProcessing.splitDateTime(selectedApt.getAptEndDateTime());
        aptEndDate.getEditor().setText(endDateArray[0]);
        aptEndHrs.setText(endDateArray[1]);
        aptEndMin.setText(endDateArray[2]);
        aptDescBox.setText(selectedApt.getAptDescription());
    }

    public void contactGetIdByName(ActionEvent actionEvent) throws SQLException {
        int aptAddUpdateContactId = 0;
        Object contactName = (String) aptContactBox.getValue();
        aptAddUpdateContactId = Contact.getContactIdByName((String) contactName);
        aptContactIdLabel.setText(String.valueOf(aptAddUpdateContactId));
    }

    public void customerGetIdByName(ActionEvent actionEvent) throws SQLException {
        int aptAddUpdateCustomerId = 0;
        Object customerName = (String) aptCustomerBox.getValue();
        aptAddUpdateCustomerId = Customer.getCustomerIdByName((String) customerName);
        aptCustomerIdLabel.setText(String.valueOf(aptAddUpdateCustomerId));
    }

    public void cleanAptForm() {
        aptIdBox.setText("");
        aptUserIdBox.setText("");
        aptTitleBox.setText("");
        aptLocationBox.setText("");
        aptTypeBox.setText("");
        aptContactBox.setValue("");
        aptCustomerBox.setValue("");
        aptContactIdLabel.setText("");
        aptCustomerIdLabel.setText("");
        aptStartDate.getEditor().clear();
        aptStartHrs.setText("");
        aptStartMin.setText("");
        aptEndDate.getEditor().clear();
        aptEndHrs.setText("");
        aptEndMin.setText("");
        aptDescBox.setText("");
    }

    //----------AppointmentWindow------------

    //Add-Update Customer Window Functions

    public void cancelCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateCustomer.setVisible(false);

        cleanCustomerForm();
    }

    public void submitCustomerButtonClicked(ActionEvent actionEvent) throws SQLException {
        int id = 0, divisionId = 0, errorCk = 0;
        String name, address, postalCode, phoneNumber, menu;


        if (customerIdBox.getText() != "") {
            id = Integer.parseInt(customerIdBox.getText());
        }
        name = customerNameBox.getText();
        System.out.println(name.split(" ").length);
        if (name.split(" ").length == 1) {
            promptHelper.errorDialog("Input validation error!","You must enter a first and last name.");
            errorCk++;
        }
        address = customerAddressBox.getText();
        if (address.split(",").length < 2) {
            promptHelper.errorDialog("Input validation error!","Please check the address format and try again.");
            errorCk++;
        }
        if (Integer.parseInt(addUpdateProvinceIdLabel.getText()) > 1) {
            divisionId = Integer.parseInt(addUpdateProvinceIdLabel.getText());
        } else {
            promptHelper.errorDialog("Input validation error!","State or Province is required");
            errorCk++;
        }
        postalCode = customerPostalCodeBox.getText();
        if (Objects.equals(postalCode, "")) {
            promptHelper.errorDialog("Input validation error!","The postal code is required");
            errorCk++;
        }
        phoneNumber = customerPhoneNumberBox.getText();
        if (phoneNumber == "") {
            promptHelper.errorDialog("Input validation error!","A phone number is required");
            errorCk++;
        }
        menu = customerAddUpdateLabel.getText();

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (errorCk == 0) {
            tableViews.setDisable(false);
            logOutButton.setDisable(false);
            addUpdateCustomer.setVisible(false);
            if (selectedCustomer == null) {
                Customer.insertCustomer(id,name,address,postalCode,phoneNumber,divisionId,userName.getText(),menu,0);
            } else {
                Customer.insertCustomer(id,name,address,postalCode,phoneNumber,divisionId,userName.getText(),menu,selectedCustomer.getCustomerId());
            }
            customerTableView.getItems().clear();
            Customer.getDatabaseCustomers();

            cleanCustomerForm();
        }
    }

    public void setDivisionCountry(ActionEvent actionEvent) throws SQLException {
        int divisionId, countryId;

        countryBox.setValue("");
        addUpdateCountryIdLabel.setText("");

        divisionId = Division.getDivisionIdByName((String) stateProvinceBox.getValue());
        addUpdateProvinceIdLabel.setText(String.valueOf(divisionId));

        countryId  = Division.getCountryIdByDivisionId(divisionId);
        countryBox.setValue(Country.getCountryNameById(countryId));
        addUpdateCountryIdLabel.setText(Integer.toString(countryId));
    }

    public void setCountry(ActionEvent actionEvent) throws SQLException {
        int countryId;
        countryId = Country.getCountryIdByName((String) countryBox.getValue());
        addUpdateCountryIdLabel.setText(Integer.toString(countryId));

        stateProvinceBox.setItems(FXCollections.observableList(Division.getAllDivisionNamesByCountry(Integer.parseInt(addUpdateCountryIdLabel.getText()))));

    }

    public void setCustomer(Customer selectedCustomer) {
        customerIdBox.setText(Integer.toString(selectedCustomer.getCustomerId()));
        customerNameBox.setText(selectedCustomer.getCustomerName());
        customerAddressBox.setText(selectedCustomer.getAddress());
        addUpdateProvinceIdLabel.setText(Integer.toString(selectedCustomer.getDivisionId()));
        stateProvinceBox.setValue(Division.getDivisionNameById(selectedCustomer.getDivisionId()));
        int countryId = Division.getCountryIdByDivisionId(selectedCustomer.getDivisionId());
        addUpdateCountryIdLabel.setText(Integer.toString(countryId));
        countryBox.setValue(Country.getCountryNameById(countryId));
        customerPostalCodeBox.setText(selectedCustomer.getPostalCode());
        customerPhoneNumberBox.setText(selectedCustomer.getPhone());
    }

    public void cleanCustomerForm() {
        customerIdBox.setText("");
        customerNameBox.setText("");
        customerAddressBox.setText("");
        stateProvinceBox.setValue("");
        countryBox.setValue("");
        customerPostalCodeBox.setText("");
        customerPhoneNumberBox.setText("");
    }

    //----------CustomerWindow------------

    public void initialize(URL location, ResourceBundle resources) {
        systemZoneId = ZoneId.systemDefault();
        locationLabel.setText("Current Location: " + systemZoneId.toString());
        frLocale = new Locale("fr", "FR");
        usLocale = new Locale("en", "US");
        sysLocale = Locale.getDefault();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        userName.setText("sqlUser");
        passString.setText("Passw0rd!");
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

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