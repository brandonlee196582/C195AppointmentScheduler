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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.LocalDate.now;

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
    @FXML Button logOutButton, aptAddButton, aptUpdateButton, aptRemoveButton, customerAddButton, customerUpdateButton, customerRemoveButton, setTodayButton;
    @FXML Tab aptTabLabel, customerTabLabel, aptAllTab, aptDayTab, aptWeekTab, aptMonthTab;
    @FXML DatePicker aptStartDateFilter;
    @FXML Label aptStartDateFilterLabel;
    @FXML TabPane aptFilterTabSelector;
    //---------

    //Add update appointment window controls and labels
    @FXML AnchorPane addUpdateAptBox;
    @FXML Button aptCancelButton, aptSubmitButton;
    @FXML ComboBox<String> aptContactBox, aptCustomerBox, aptStartHrs, aptStartMin, aptEndHrs, aptEndMin, aptUserIdBox;
    @FXML DatePicker aptStartDate, aptEndDate;
    @FXML Label aptIdLabel, aptUserIdLabel, addAptLabel, aptTitleLabel, aptContactLabel, aptContactIdLabel, aptLocationLabel, aptCustomerNameLabel, aptCustomerIdLabel, aptTypeLabel, aptStartLabel, aptStartHoursLabel, aptStartMinutesLabel, aptEndLabel, aptDescriptionLabel;
    @FXML TextArea aptDescBox;
    @FXML TextField aptIdBox, aptTitleBox, aptLocationBox, aptTypeBox;
    //---------

    //Add update customer window controls and labels
    @FXML AnchorPane addUpdateCustomer;
    @FXML Button customerCancelButton, customerSubmitButton;
    @FXML ComboBox<String> stateProvinceBox;
    @FXML ComboBox<String> countryBox;
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

    //---------------------------------------
    //---------------------------------------
    //Login Window Functions
    //---------------------------------------
    //---------------------------------------

    public void logInButtonClicked(ActionEvent actionEvent) throws SQLException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        String userNameText = userName.getText();
        String passwordText = passString.getText();
        String loginMessage = JDBC.openConnection(userNameText, passwordText);

        ObservableList<String> minList = FXCollections.observableArrayList();
        minList.add("00");
        minList.add("15");
        minList.add("30");
        minList.add("45");

        aptStartMin.setItems(minList);

        String businessStartStringEST = LocalDate.now() + " 08:00:00";
        Timestamp businessStartEtc = DateTimeProcessing.stringToDateTime(businessStartStringEST);
        Timestamp businessStartLocal = DateTimeProcessing.dateTimeToEST(businessStartEtc);

        int startLocal;
        String[] startLocalSplit = businessStartLocal.toString().split(" ");
        String[] startLocalTimeSplit = startLocalSplit[1].split(":");
        if (startLocalTimeSplit[0].startsWith("0")) {
            startLocal = Integer.parseInt(startLocalTimeSplit[0].substring(1));
        } else {
            startLocal = Integer.parseInt(startLocalTimeSplit[0]);
        }

        ObservableList<String> hrsList = FXCollections.observableArrayList();
        int x = 0;
        while (x < 14) {
            if (startLocal < 10) {
                if (startLocal == 0) {
                    hrsList.add("00");
                } else {
                    hrsList.add("0" + startLocal);
                }
            } else {
                hrsList.add(String.valueOf(startLocal));
            }
            startLocal++;
            if (startLocal > 23) {
                startLocal = 0;
            }
            x++;
        }

        aptStartHrs.setItems(hrsList);

        if (loginMessage.contains("Access denied")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (Objects.equals(sysLocale.getLanguage(), frLocale.getLanguage())) {
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
            User.getDatabaseUsers();

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

            aptUserIdBox.setItems(User.getAllUserNames());

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                if (Objects.equals(sysLocale.getLanguage(), frLocale.getLanguage())) {
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

    //---------------------------------------
    //---------------------------------------
    //Main Window Functions
    //---------------------------------------
    //---------------------------------------

    public void aptFilterTabSelect() {
        int selectedTabIndex = aptFilterTabSelector.getSelectionModel().getSelectedIndex();

        if (aptStartDateFilter != null) {
            if (selectedTabIndex == 0) {
                aptStartDateFilter.setDisable(true);
                aptStartDateFilter.setValue(null);
                aptTableView.setItems(Appointment.getAllapts());
            }

            if (selectedTabIndex == 1) {
                aptStartDateFilter.setDisable(false);
                setFilterDateBlock();
                ObservableList<Appointment> aptsByDay = Appointment.getAptsByDay(aptStartDateFilter.getValue());
                aptTableView.setItems(aptsByDay);
            }

            if (selectedTabIndex == 2) {
                aptStartDateFilter.setDisable(false);
                setFilterDateBlock();
                ObservableList<Appointment> aptsByWeek = Appointment.getAptsByWeek(aptStartDateFilter.getValue());
                aptTableView.setItems(aptsByWeek);
            }

            if (selectedTabIndex == 3) {
                aptStartDateFilter.setDisable(false);
                setFilterDateBlock();
                ObservableList<Appointment> aptsByMonth = Appointment.getAptsByMonth(aptStartDateFilter.getValue());
                aptTableView.setItems(aptsByMonth);
            }
        }

    }

    @FXML
    public void setFilterDateBlock() {
        if (aptStartDateFilter.getValue() == null) {
            aptStartDateFilter.setValue(now());
        }
    }

    @FXML
    public void setFilterToday() {
        aptStartDateFilter.setValue(now());
    }

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

        stage.setOnCloseRequest(event -> Platform.exit());
    }

    public void addAptButtonClicked() {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateAptBox.setVisible(true);
        addAptLabel.setText("Add Appointment");

        aptCustomerBox.setItems(Customer.getAllCustomerNames());
        aptContactBox.setItems(Contact.getAllContactNames());
    }

    public void updateAptButtonClicked() {

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

        aptCustomerBox.setItems(Customer.getAllCustomerNames());
        aptContactBox.setItems(Contact.getAllContactNames());
    }

    public void removeAptButtonClicked() throws SQLException {
        Appointment selectedApt = aptTableView.getSelectionModel().getSelectedItem();

        if (promptHelper.confirmPrompt("Remove appointment Confirmation","Are you sure you would like to remove the following appointment ID#:" + selectedApt.getAptId() + ", " + selectedApt.getAptTitle() + "?")) {

            Appointment.remeoveApt(selectedApt.getAptId());
            aptTableView.getItems().clear();
            Appointment.getDatabaseApts();
        }

    }

    public void addCustomerButtonClicked() {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Add Customer");

        stateProvinceBox.setItems(Division.getAllDivisionNames());
        countryBox.setItems(Country.getAllCountryNames());
    }

    public void updateCustomerButtonClicked() {
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

    public void removeCustomerButtonClicked() throws SQLException {
        StringBuilder appointmentsScheduled;

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        appointmentsScheduled = new StringBuilder(" | ");

        for (Appointment apt : Appointment.getAllapts()) {
            if (apt.getCustomerId() == selectedCustomer.getCustomerId()) {
                appointmentsScheduled.append(apt.getAptId()).append(" | ");
            }
        }
        if (!appointmentsScheduled.toString().equals(" | ")) {
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

    //---------------------------------------
    //---------------------------------------
    //Add-Update Appointment Window Functions
    //---------------------------------------
    //---------------------------------------

    public void userIdControl() {
        System.out.println("works");
    }

    public void endHrsSelection() {
        aptEndHrs.getSelectionModel().clearSelection();
        aptEndMin.getSelectionModel().clearSelection();
        ObservableList<String> endHrsList = FXCollections.observableArrayList();
        int startCk = 0;
        for (String time : aptStartHrs.getItems()) {
            if (aptStartHrs.getValue().equals(time)) {
                startCk = 1;
            }
            if (startCk == 1) {
                endHrsList.add(time);
            }
        }
        aptEndHrs.setItems(endHrsList);
    }

    public void endMinSelection() {
        int startCk = 0;
        aptEndMin.getSelectionModel().clearSelection();
        aptEndMin.setValue("");
        ObservableList<String> endMinList = FXCollections.observableArrayList();

        if (aptEndHrs.getSelectionModel().getSelectedIndex() > -1) {
            if (aptStartMin.getSelectionModel().getSelectedIndex() > -1) {
                if (aptStartHrs.getValue().equals(aptEndHrs.getValue())) {
                    for (String min : aptStartMin.getItems()) {
                        if (aptStartMin.getValue().equals(min)) {
                            startCk = 1;
                        }
                        if (startCk == 1) {
                            if (!aptStartMin.getValue().equals(min)) {
                                endMinList.add(min);
                            }
                        }
                    }
                    aptEndMin.setItems(endMinList);
                } else {
                    aptEndMin.setItems(aptStartMin.getItems());
                }
            }
        }
    }

    public void submitAptButtonClicked() throws SQLException {
        int id = 0, userId = 0, contactId, customerId, errorCk = 0, startHrs = 0, startMin = 0, endHrs = 0 , endMin = 0 ;
        String title, location, type, desc;
        LocalDate startDate, endDate;
        Timestamp startDateTime, endDateTime;

        if (!Objects.equals(customerIdBox.getText(), "")) {
            id = Integer.parseInt(aptIdBox.getText());
        }

        try {
            userId = Integer.parseInt(aptUserIdBox.getValue());
        } catch (Exception e) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The user Id is required and must be a number.");
        }

        title = aptTitleBox.getText();
        if (title.equals("")) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The appointment title is required.");
        }

        location  = aptLocationBox.getText();
        if (location.equals("")) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The location is required.");
        }

        type =  aptTypeBox.getText();
        if (type.equals("")) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The type is required.");
        }

        contactId = Integer.parseInt(aptContactIdLabel.getText());
        if (contactId == 0) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","You must select a contact");
        }

        customerId = Integer.parseInt(aptCustomerIdLabel.getText());
        if (customerId == 0) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","You must select a customer.");
        }

        startDate = aptStartDate.getValue();
        if (startDate == null) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The start date is required.");
        }

        try {
            startHrs = Integer.parseInt(aptStartHrs.getValue());
        } catch (Exception e) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The starting hour is required and must be a number.");
        }

        try {
            startMin = Integer.parseInt(aptStartMin.getValue());
        } catch (Exception e) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The starting minute is required and must be a number.");
        }

        endDate = aptEndDate.getValue();
        if (endDate == null) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The end date is required.");
        }

        try {
            endHrs = Integer.parseInt(aptEndHrs.getValue());
        } catch (Exception e) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The end hour is required and must be a number.");
        }

        try {
            endMin = Integer.parseInt(aptEndMin.getValue());
        } catch (Exception e) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The end hour is required and must be a number.");
        }

        desc = aptDescBox.getText();
        if (desc.equals("")) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The description is required.");
        }

        startDateTime = Timestamp.valueOf(startDate + " " + startHrs + ":" + startMin + ":00");
        endDateTime = Timestamp.valueOf(endDate + " " + endHrs + ":" + endMin + ":00");


        if (errorCk == 0) {
            tableViews.setDisable(false);
            logOutButton.setDisable(false);
            addUpdateAptBox.setVisible(false);

            Appointment.insertApt(id,title,desc,location,type,startDateTime,endDateTime,userName.getText(),userName.getText(),customerId,userId,contactId);

            aptTableView.getItems().clear();
            Appointment.getDatabaseApts();

            cleanAptForm();
        }
    }

    public void cancelAptButtonClicked() {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);

        cleanAptForm();
    }

    public void setAppointment(Appointment selectedApt) {
        aptIdBox.setText(Integer.toString(selectedApt.getAptId()));
        aptUserIdBox.setValue(Integer.toString(selectedApt.getUserId()));
        aptTitleBox.setText(selectedApt.getAptTitle());
        aptLocationBox.setText(selectedApt.getAptLocation());
        aptTypeBox.setText(selectedApt.getAptType());
        aptContactBox.setValue(Contact.getContactNameById(selectedApt.getContactId()));
        aptCustomerBox.setValue(Customer.getCustomerNameById(selectedApt.getCustomerId()));
        aptContactIdLabel.setText(Integer.toString(selectedApt.getContactId()));
        aptCustomerIdLabel.setText(Integer.toString(selectedApt.getCustomerId()));
        String[] startDateArray = DateTimeProcessing.splitDateTime(selectedApt.getAptStartDateTime());
        aptStartDate.getEditor().setText(startDateArray[0]);
        aptStartHrs.setValue(startDateArray[1]);
        aptStartMin.setValue(startDateArray[2]);
        String[] endDateArray = DateTimeProcessing.splitDateTime(selectedApt.getAptEndDateTime());
        aptEndDate.getEditor().setText(endDateArray[0]);
        aptEndHrs.setValue(endDateArray[1]);
        aptEndMin.setValue(endDateArray[2]);
        aptDescBox.setText(selectedApt.getAptDescription());
    }

    public void contactGetIdByName() {
        int aptAddUpdateContactId;
        String contactName = aptContactBox.getValue();
        aptAddUpdateContactId = Contact.getContactIdByName(contactName);
        aptContactIdLabel.setText(String.valueOf(aptAddUpdateContactId));
    }

    public void customerGetIdByName() {
        int aptAddUpdateCustomerId;
        String customerName = aptCustomerBox.getValue();
        aptAddUpdateCustomerId = Customer.getCustomerIdByName(customerName);
        aptCustomerIdLabel.setText(String.valueOf(aptAddUpdateCustomerId));
    }

    public void cleanAptForm() {
        aptIdBox.setText("");
        aptUserIdBox.setValue("");
        aptTitleBox.setText("");
        aptLocationBox.setText("");
        aptTypeBox.setText("");
        aptContactBox.setValue("");
        aptCustomerBox.setValue("");
        aptContactIdLabel.setText("");
        aptCustomerIdLabel.setText("");
        aptStartDate.getEditor().clear();
        aptStartHrs.setValue("");
        aptStartMin.setValue("");
        aptEndDate.getEditor().clear();
        aptEndHrs.setValue("");
        aptEndMin.setValue("");
        aptDescBox.setText("");
    }

    //----------AppointmentWindow------------

    //Add-Update Customer Window Functions

    public void endValidationNotice() {
        if (aptStartHrs.getValue() == null || aptStartMin.getValue() == null) {
            promptHelper.errorDialog("Validation Error","You must enter the start time before entering the end time.");
        }
    }

    public void cancelCustomerButtonClicked() {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateCustomer.setVisible(false);

        cleanCustomerForm();
    }

    public void submitCustomerButtonClicked() throws SQLException {
        int id = 0, divisionId = 0, errorCk = 0;
        String name, address, postalCode, phoneNumber, menu;


        if (!Objects.equals(customerIdBox.getText(), "")) {
            id = Integer.parseInt(customerIdBox.getText());
        }
        name = customerNameBox.getText();
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
        if (Objects.equals(phoneNumber, "")) {
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

    public void setDivisionCountry() {
        int divisionId, countryId;

        divisionId = Division.getDivisionIdByName(stateProvinceBox.getValue());
        addUpdateProvinceIdLabel.setText(String.valueOf(divisionId));

        if (addUpdateCountryIdLabel.getText().equals("0")) {
            countryBox.setValue("");
            addUpdateCountryIdLabel.setText("");

            countryId = Division.getCountryIdByDivisionId(divisionId);
            countryBox.setValue(Country.getCountryNameById(countryId));
            addUpdateCountryIdLabel.setText(Integer.toString(countryId));
        }
    }

    public void setCountry() {
        int countryId;
        countryId = Country.getCountryIdByName(countryBox.getValue());
        addUpdateCountryIdLabel.setText(Integer.toString(countryId));
        ObservableList<String> divisions = Division.getAllDivisionNamesByCountry(countryId);

        stateProvinceBox.setItems(divisions);
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
        locationLabel.setText("Current Location: " + systemZoneId);
        frLocale = new Locale("fr", "FR");
        usLocale = new Locale("en", "US");
        sysLocale = Locale.getDefault();

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        userName.setText("sqlUser");
        passString.setText("Passw0rd!");
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        if (Objects.equals(sysLocale.getLanguage(), frLocale.getLanguage())) {
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