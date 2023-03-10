package control;

import helper.DateTimeProcessing;
import helper.FileSystem;
import helper.JDBC;
import helper.promptHelper;
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
import model.*;

import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Date;
import static helper.JDBC.connection;
import static java.time.LocalDate.now;

/**
 * This system is built on a single stage which means this controller contains all controls needed for the entire application.
 * The application is split into a login window, a main window with a tab for appointments, customers, standard reports,
 * and a filter building system with a help window, an add update window, and an add update customer window. The controls
 * for the different windows are separated by sections.
 *
 * @author brandonLackey
 */
public class AppWindowController implements Initializable {
    // FXML components for the main window
    @FXML AnchorPane tableViews;
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn, addressColumn, PostalCodeColumn, PhoneColumn, divisionColumn, countryColumn;
    @FXML private TableView<Appointment> aptTableView, aptReportTableView, aptReportContactSchedule;
    @FXML private TableColumn<Appointment, Integer> aptIDColumn, aptReportIDCol, aptCustomerIdColumn, aptReportCustomerIdCol, aptUserIdColumn, aptcontactScheduleIdCol, aptcontactScheduleCustomerIdCol;
    @FXML private TableColumn<Appointment, String> aptTitleColumn, aptReportTitleCol, aptLocationColumn, aptReportLocationCol, aptTypeColumn, aptReportTypeCol, aptDescriptionColumn, aptReportDescriptionCol, aptContactColumn, aptReportContactCol, aptcontactScheduleTitleCol, aptcontactScheduleTypeCol, aptcontactScheduleDescCol;
    @FXML private TableColumn<Appointment, Timer> aptStartColumn, aptReportStartCol, aptcontactScheduleStartCol, aptcontactScheduleEndCol;
    @FXML private TableColumn<Appointment, Date> aptEndColumn, aptReportEndCol;
    @FXML private TableView<ReportByMonthType> aptReportTypeMonth;
    @FXML private TableColumn<ReportByMonthType, String> aptTypeMonthMonthCol, aptTypeMonthTypeCol;
    @FXML private TableColumn<ReportByMonthType, Integer> aptTypeMonthCountCol;
    @FXML Button logOutButton, aptAddButton, aptUpdateButton, aptRemoveButton, customerAddButton, customerUpdateButton, customerRemoveButton, setTodayButton, setTodayReportButton;
    @FXML Tab aptTabLabel, customerTabLabel, aptAllTab, aptDayTab, aptWeekTab, aptMonthTab, reportBuilderTab, standardReportsTab;
    @FXML DatePicker aptStartDateFilter,aptReportStartDateFilter;
    @FXML Label aptStartDateFilterLabel, aptReportTitleLabel, reportWeekMonthLabel, aptReportStartDateFilterLabel, aptReportEntitySelectLabel, aptReportEntityNameLabel, aptReportEntityTypeLabel, aptReportEntityTypeNameLabel, reportReturnLabel, reportReturnNumber, oldStart, oldEnd;
    @FXML TabPane aptFilterTabSelector;
    @FXML ToggleButton reportWeekMonthToggle;
    @FXML ComboBox<String> aptReportEntitySelectBox, aptReportEntityNameBox, aptReportEntityTypeBox, aptReportEntityTypeNameBox, contactScheduleBox;
    @FXML CheckBox aptReportAllTimeCk;
    //---------

    // FXML components for the custom report help window
    @FXML AnchorPane aptReportHelp;
    @FXML Button aptReportHelpCloseButton;
    @FXML Label aptReportHelpLabel;
    @FXML TextArea aptReportHelpText;
    //---------

    // FXML components for the add update appointment window
    @FXML AnchorPane addUpdateAptBox;
    @FXML Button aptCancelButton, aptSubmitButton;
    @FXML ComboBox<String> aptContactBox, aptCustomerBox, aptStartHrs, aptStartMin, aptEndHrs, aptEndMin, aptUserIdBox;
    @FXML DatePicker aptStartDate, aptEndDate;
    @FXML Label aptIdLabel, aptUserIdLabel, addAptLabel, aptTitleLabel, aptContactLabel, aptContactIdLabel, aptLocationLabel, aptCustomerNameLabel, aptCustomerIdLabel, aptTypeLabel, aptStartLabel, aptStartHoursLabel, aptStartMinutesLabel, aptEndLabel, aptDescriptionLabel, aptUserLabel;
    @FXML TextArea aptDescBox;
    @FXML TextField aptIdBox, aptTitleBox, aptLocationBox, aptTypeBox;
    //---------

    // FXML components for the add update customer window
    @FXML AnchorPane addUpdateCustomer;
    @FXML Button customerCancelButton, customerSubmitButton;
    @FXML ComboBox<String> stateProvinceBox;
    @FXML ComboBox<String> countryBox;
    @FXML Label addUpdateCustomerIdLabel, addUpdateCustomerNameLabel, addUpdateCustomerAddressLabel, addUpdateProvinceLabel, addUpdateProvinceIdLabel, addUpdateCountryLabel, addUpdateCountryIdLabel, addUpdatePostalCodeLabel, addUpdatePhoneLabel, customerAddUpdateLabel, menuOpenCk;
    @FXML TextField customerIdBox, customerNameBox, customerAddressBox, customerPostalCodeBox, customerPhoneNumberBox;
    //---------

    // FXML components for the login window
    @FXML AnchorPane loginBox;
    @FXML Button loginButton;
    @FXML Label loginLabel, locationLabel;
    @FXML TextField userName, passString;
    //---------
    // Global class variables
    private Stage stage;
    ZoneId systemZoneId;
    Locale sysLocale, frLocale, usLocale;
    ResourceBundle rb1;

    //---------------------------------------
    //---------------------------------------
    // LOGIN WINDOW FUNCTIONS
    //---------------------------------------
    //---------------------------------------

    /**
     * Sets up the initial functionality of the main window after the user enters correct login information pulled from
     * the users table in the client_schedule database. Since all windows are built around a single stage, this window is
     * toggled between visible and hidden as the user interacts with the interface.
     *
     * Lambda Expression: Using a lambda expression here eliminated the need to create a separate handler function to modify
     * what happens when the application is close. Added closing the database connection when the app is closed.
     *
     * @param actionEvent User Event - Controls setOnCloseRequest preventing the user from closing the app while logged in.
     * @throws SQLException Used to throw a sql exception controlled in the JDBC helper class.
     */
    public void logInButtonClicked(ActionEvent actionEvent) throws SQLException {
        int userId = 0;
        String string = "Passw0rd!", loginResponse, loginMessage, loginSql, dbPass = "", uName  = "sqlUser";
        JDBC.openConnection(uName, string);
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        String userNameText = userName.getText();
        String passwordText = passString.getText();

        //creates user objects from the users class then determines if the username attempting to login matches a name from
        // a list of all users. Future implementation could use contain on a collection rather than iteration.
        User.getDatabaseUsers();
        for (User user : User.getAllUsers()) {
            if (userNameText.equals(user.getUsername())) {
                userId = user.getUserId();
            }
        }

        // If a matching user is found, check the password and login to the database then. If a user is not found or the
        // password is incorrect the user is given an error prompt indicating there was a failed login. The login status,
        // failed or success is then added to a login_activity log.
        if (userId != 0) {
            loginSql = "SELECT * FROM client_schedule.users where User_ID=" + userId;
            PreparedStatement loginPs = connection.prepareStatement(loginSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet loginRs = loginPs.executeQuery();

            loginRs.next();
            if (loginRs.getString("User_Name").equals(userNameText)) {
                dbPass = loginRs.getString("Password");
            }

            if (dbPass.equals(passwordText)) {

                loginMessage = "Success";

                // Setup 15 minute appointment windows
                ObservableList<String> minList = FXCollections.observableArrayList();
                minList.add("00");
                minList.add("15");
                minList.add("30");
                minList.add("45");
                aptStartMin.setItems(minList);

                // Determine local business hours based on eastern time and creates the appointment hours list used for
                // appointment scheduling.
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
            } else {
                // Password incorrect
                loginMessage = "Access denied";
            }
        } else {
            // Username incorrect - A separate message indicating if a username or password is incorrect could be implemented
            // with the logic, but is not advisable since it could make it easier for someone to gain unauthorized access
            // to the system.
            loginMessage = "Access denied";
        }

        // If login was unsuccessful, prompt the user.
        if (loginMessage.contains("Access denied")) {
            // Set up a French prompt if the user's system language is french.
            if (sysLocale.getLanguage().equals(frLocale.getLanguage())) {
                promptHelper.errorDialog("échec de la connexion","Identifiant ou mot de passe incorrect!");
            } else {
                promptHelper.errorDialog("Failed Login","Incorrect username or password.");
            }
            loginResponse = "failed";
        } else {
            // If login was successfull, set up the main window and hide the login window.
            loginResponse = "success";
            loginBox.setVisible(false);
            tableViews.setDisable(false);
            logOutButton.setDisable(false);

            // Getting all the data from the database using class functions from the appropriate class then setup javafx
            // table views.
            Contact.getDatabaseContacts();
            Division.getDatabaseDivisions();
            Country.getDatabaseCountries();
            Appointment.getDatabaseApts();
            Customer.getDatabaseCustomers();
            User.getDatabaseUsers();
            ReportByMonthType.getReportItems();

            Appointment.checkForAptByTime();

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

            aptReportTableView.setItems(Appointment.getAllapts());
            aptReportIDCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            aptReportTitleCol.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptReportDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
            aptReportLocationCol.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
            aptReportContactCol.setCellValueFactory(new PropertyValueFactory<>("contactString"));
            aptReportTypeCol.setCellValueFactory(new PropertyValueFactory<>("aptType"));
            aptReportStartCol.setCellValueFactory(new PropertyValueFactory<>("aptStartDateTime"));
            aptReportEndCol.setCellValueFactory(new PropertyValueFactory<>("aptEndDateTime"));
            aptReportCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            aptReportContactSchedule.setItems(Appointment.getAllapts());
            aptcontactScheduleIdCol.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            aptcontactScheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
            aptcontactScheduleDescCol.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
            aptcontactScheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("aptType"));
            aptcontactScheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("aptStartDateTime"));
            aptcontactScheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("aptEndDateTime"));
            aptcontactScheduleCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

            aptReportTypeMonth.setItems(ReportByMonthType.getAllReportItems());
            aptTypeMonthMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            aptTypeMonthTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            aptTypeMonthCountCol.setCellValueFactory(new PropertyValueFactory<>("count"));

            // Setup initial user list for user dropdown menu.
            ObservableList<String> userList = FXCollections.observableArrayList();
            for (User user : User.getAllUsers()) {
                if (!userList.contains(User.getUserNameFromId(user.getUserId()))) {
                    userList.add(User.getUserNameFromId(user.getUserId()));
                }
            }
            aptUserIdBox.setItems(userList);

            // Setup initial contact list for user dropdown menu.
            ObservableList<String> contactList = FXCollections.observableArrayList();
            for (Appointment apt : Appointment.getAllapts()) {
                if (!contactList.contains(Contact.getContactNameById(apt.getContactId()))) {
                    contactList.add(Contact.getContactNameById(apt.getContactId()));
                }
            }

            stage.setOnCloseRequest(event -> {
                JDBC.closeConnection();
                Platform.exit();
            });
        }
        // Logs login response to file.
        FileSystem.loginLog(userNameText, ZonedDateTime.now(), loginResponse);
    }



    //----------------------

    //---------------------------------------
    //---------------------------------------
    //Main Window Functions
    //---------------------------------------
    //---------------------------------------

    /**
     * Refreshes the appointment table view if the Day, Week, or Month tabs are selected on the appointment tab.
     */
    public void aptStartDateFilter() {
        int selectedTabIndex = aptFilterTabSelector.getSelectionModel().getSelectedIndex();
        if (selectedTabIndex != 0) {
            if (selectedTabIndex < 3) {
                aptFilterTabSelector.getSelectionModel().select(3);
                aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
            } else {
                aptFilterTabSelector.getSelectionModel().select(2);
                aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
            }
        }
    }

    /**
     * Refreshes the standard report by month table view on the standard reports tab.
     */
    public void runStandardReports() {
        aptReportTypeMonth.getItems().clear();
        ReportByMonthType.getReportItems();

        contactScheduleBox.setItems(Contact.getAllContactNames());
    }

    /**
     * Filters the contact schedule standard report table view by selected contact in contact dropdown box on the standard
     * reports tab.
     */
    public void runContactSchedule() {
        String contact = contactScheduleBox.getValue();
        ObservableList<Appointment> contactApts = FXCollections.observableArrayList();
        for (Appointment apt : Appointment.getAllapts()) {
            if (apt.getContactId() == Contact.getContactIdByName(contact)) {

                Timestamp[] currentTimestampArr = new Timestamp[] {Timestamp.from(Instant.now()), apt.getAptEndDateTime()};
                long[] compEpochValues = DateTimeProcessing.timestampArrToEpoch(currentTimestampArr);

                if (compEpochValues[1] >= compEpochValues[0]) {
                    contactApts.add(apt);
                }
            }
        }
        aptReportContactSchedule.setItems(contactApts);
    }

    /**
     * Controls the week/month radio button for the custom report builder.
     */
    public void aptReportWeekMonthToggle() {
        if (reportWeekMonthToggle.getText().equals("Week")) {
            reportWeekMonthToggle.setText("Month");
            aptReportStartDateFilter.requestFocus();
        } else {
            reportWeekMonthToggle.setText("Week");
            aptReportStartDateFilter.requestFocus();
        }
        aptRunReport();
    }

    /**
     * Controls the all-time results check box for the custom report builder.
     */
    public void aptReportAllTimeCk() {
        if (aptReportAllTimeCk.isSelected()) {
            reportWeekMonthToggle.setDisable(true);
            reportWeekMonthLabel.setDisable(true);
            reportWeekMonthToggle.setText("All");
            reportWeekMonthToggle.setSelected(false);
        } else {
            reportWeekMonthToggle.setDisable(false);
            reportWeekMonthLabel.setDisable(false);
            reportWeekMonthToggle.setText("Month");
        }
        aptRunReport();
    }

    /**
     * Controls the set today button on the custom report builder.
     */
    public void aptReportStartDateToday() {
        aptReportStartDateFilter.setValue(now());
        aptRunReport();
    }

    /**
     * Sets the layer one and two selector combo box values and the default date for the custom report builder.
     */
    public void aptReport() {
        if (aptReportStartDateFilter != null) {
            ObservableList<String> reportEntitySelector = FXCollections.observableArrayList();
            reportEntitySelector.add("Customer");
            reportEntitySelector.add("Contact");
            reportEntitySelector.add("Type");
            reportEntitySelector.add("Location");
            aptReportEntitySelectBox.setItems(reportEntitySelector);

            aptReportStartDateToday();

            ObservableList<String> reportEntityTypeSelector = FXCollections.observableArrayList();
            reportEntityTypeSelector.add("Customer");
            reportEntityTypeSelector.add("Contact");
            reportEntityTypeSelector.add("Type");
            reportEntityTypeSelector.add("Location");
            aptReportEntityTypeBox.setItems(reportEntitySelector);
        }
    }

    /**
     * Sets the layer one value dropdown based on the layer one selector selection on the custom report builder.
     */
    public void aptReportSetLayerOneSelector() {
        ObservableList<String> reportLayerOneSelector = FXCollections.observableArrayList();
        if (aptReportEntitySelectBox.getValue() != null) {
            if (aptReportEntitySelectBox.getValue().equals("Customer")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerOneSelector.contains(Customer.getCustomerNameById(apt.getCustomerId()))) {
                        reportLayerOneSelector.add(Customer.getCustomerNameById(apt.getCustomerId()));
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Contact")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerOneSelector.contains(Contact.getContactNameById(apt.getContactId()))) {
                        reportLayerOneSelector.add(Contact.getContactNameById(apt.getContactId()));
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Type")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerOneSelector.contains(apt.getAptType())) {
                        reportLayerOneSelector.add(apt.getAptType());
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Location")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerOneSelector.contains(apt.getAptLocation())) {
                        reportLayerOneSelector.add(apt.getAptLocation());
                    }
                }
            }
            aptReportEntityNameBox.setItems(reportLayerOneSelector);
        }
    }

    /**
     * Sets the layer two value dropdown based on the layer two selector selection on the custom report builder.
     */
    public void aptReportSetLayerTwoSelector() {
        ObservableList<String> reportLayerTwoSelector = FXCollections.observableArrayList();
        if (aptReportEntityTypeBox.getValue() != null) {
            if (aptReportEntityTypeBox.getValue().equals("Customer")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerTwoSelector.contains(Customer.getCustomerNameById(apt.getCustomerId()))) {
                        reportLayerTwoSelector.add(Customer.getCustomerNameById(apt.getCustomerId()));
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Contact")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerTwoSelector.contains(Contact.getContactNameById(apt.getContactId()))) {
                        reportLayerTwoSelector.add(Contact.getContactNameById(apt.getContactId()));
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Type")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerTwoSelector.contains(apt.getAptType())) {
                        reportLayerTwoSelector.add(apt.getAptType());
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Location")) {
                for (Appointment apt : Appointment.getAllapts()) {
                    if (!reportLayerTwoSelector.contains(apt.getAptLocation())) {
                        reportLayerTwoSelector.add(apt.getAptLocation());
                    }
                }
            }
            aptReportEntityTypeNameBox.setItems(reportLayerTwoSelector);
        }
    }

    /**
     * Sets values for the custom report builder table view base on the all-time results' checkbox, the week month toggle,
     * the filter start date picker, the layer one value selected, and the layer two value selected.
     *
     * The layer one and two selections are optional.
     */
    public void aptRunReport() {
        //date filters
        if (aptReportAllTimeCk.isSelected()) {
            aptReportTableView.setItems(Appointment.getAllapts());
        }
        if (reportWeekMonthToggle.getText().equals("Month")) {
            ObservableList<Appointment> aptsByMonth = Appointment.getAptsByMonth(aptReportStartDateFilter.getValue());
            aptReportTableView.setItems(aptsByMonth);
        }
        if (reportWeekMonthToggle.getText().equals("Week")) {
            ObservableList<Appointment> aptsByWeek = Appointment.getAptsByWeek(aptReportStartDateFilter.getValue());
            aptReportTableView.setItems(aptsByWeek);
        }

        //layer one filters
        if (aptReportEntityNameBox.getValue() != null) {
            ObservableList<Appointment> layerOneFilterList = FXCollections.observableArrayList();
            if (aptReportEntitySelectBox.getValue().equals("Customer")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    String customerName = Customer.getCustomerNameById(apt.getCustomerId());
                    if (customerName.equals(aptReportEntityNameBox.getValue())) {
                        layerOneFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Contact")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    String contactName = Contact.getContactNameById(apt.getContactId());
                    if (contactName.equals(aptReportEntityNameBox.getValue())) {
                        layerOneFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Type")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    if (apt.getAptType().equals(aptReportEntityNameBox.getValue())) {
                        layerOneFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntitySelectBox.getValue().equals("Location")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    if (apt.getAptLocation().equals(aptReportEntityNameBox.getValue())) {
                        layerOneFilterList.add(apt);
                    }
                }
            }
            aptReportTableView.setItems(layerOneFilterList);
        }
        //layer two filters
        if (aptReportEntityTypeNameBox.getValue() != null) {
            ObservableList<Appointment> layerTwoFilterList = FXCollections.observableArrayList();
            if (aptReportEntityTypeBox.getValue().equals("Customer")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    String customerName = Customer.getCustomerNameById(apt.getCustomerId());
                    if (customerName.equals(aptReportEntityTypeNameBox.getValue())) {
                        layerTwoFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Contact")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    String contactName = Contact.getContactNameById(apt.getContactId());
                    if (contactName.equals(aptReportEntityTypeNameBox.getValue())) {
                        layerTwoFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Type")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    if (apt.getAptType().equals(aptReportEntityTypeNameBox.getValue())) {
                        layerTwoFilterList.add(apt);
                    }
                }
            }
            if (aptReportEntityTypeBox.getValue().equals("Location")) {
                for (Appointment apt : aptReportTableView.getItems()) {
                    if (apt.getAptLocation().equals(aptReportEntityTypeNameBox.getValue())) {
                        layerTwoFilterList.add(apt);
                    }
                }
            }
            aptReportTableView.setItems(layerTwoFilterList);
        }

        int reportOut = aptReportTableView.getItems().size();
        reportReturnNumber.setText(Integer.toString(reportOut));
    }

    /**
     * Resets the custom report builder to the default state.
     */
    public void aptReportReset() {
        aptReportEntitySelectBox.setValue(null);
        aptReportEntityNameBox.setValue(null);
        aptReportEntityNameBox.setItems(null);
        aptReportEntityTypeBox.setValue(null);
        aptReportEntityTypeNameBox.setItems(null);
        aptReportEntityTypeNameBox.setValue(null);
        aptReportStartDateToday();
    }

    /**
     * Displays the custom report builder help menu.
     */
    public void aptReportHelp() {
        aptReportHelp.setVisible(true);
        tableViews.setDisable(true);
    }

    /**
     * Closes the custom report builder help menu.
     */
    public void aptReportHelpClose() {
        aptReportHelp.setVisible(false);
        tableViews.setDisable(false);
    }

    /**
     * Filters the appointment table view values based on the all, day, week, or month tab selection in the appointments
     * menu.
     */
    public void aptFilterTabSelect() {
        int selectedTabIndex = aptFilterTabSelector.getSelectionModel().getSelectedIndex();

        if (aptStartDateFilter != null) {
            setTodayButton.setDisable(false);
            if (selectedTabIndex == 0) {
                setTodayButton.setDisable(true);
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

    /**
     * Sets the filter start date to the current date if there is no date selected on the appointments tab.
     */
    @FXML
    public void setFilterDateBlock() {
        if (aptStartDateFilter.getValue() == null) {
            aptStartDateFilter.setValue(now());
        }
    }

    /**
     * Sets the filter start date to the current date on the appointments tab.
     */
    @FXML
    public void setFilterToday() {
        aptStartDateFilter.setValue(now());
    }

    /**
     * Closes the database connection and returns the user to the login window.
     *
     * @param actionEvent User Event - Controls setOnCloseRequest allowing the user to exit the application if logged out.
     */
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

        Platform.exit();
    }

    /**
     * Opens the add/update appointment window and sets default values for adding a new appointment. Disables the main window.
     */
    public void addAptButtonClicked() {
        String user = userName.getText();
        int userId = User.getUserIdFromName(user);

        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateAptBox.setVisible(true);
        addAptLabel.setText("Add Appointment");
        aptUserIdBox.setValue(user);
        aptUserIdLabel.setText(Integer.toString(userId));

        aptCustomerBox.setItems(Customer.getAllCustomerNames());
        aptContactBox.setItems(Contact.getAllContactNames());
    }

    /**
     * Opens the add/update appointment window and sets the values to the selected appointment values for updating an appointment.
     * Prompts the user to select an appointment if one isn't selected. Disables the main window.
     */
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

    /**
     * Removes appointment from the appointment table in the client_schedule database. Prompts the user for remove confirmation.
     * Prompts the uesr to select an appointment if one isn't selected.
     *
     */
    public void removeAptButtonClicked() {
        Appointment selectedApt = aptTableView.getSelectionModel().getSelectedItem();
        int selectedTabIndex = aptFilterTabSelector.getSelectionModel().getSelectedIndex();

        if (selectedApt == null) {
            promptHelper.errorDialog("No appointment selected", "Please select an appointment to remove.");
            return;
        }

        if (promptHelper.confirmPrompt("Remove appointment Confirmation","Are you sure you would like to remove the following appointment ID#: " + selectedApt.getAptId() + ", Type: " + selectedApt.getAptType() + "?")) {

            Appointment.remeoveApt(selectedApt.getAptId());
            aptTableView.getItems().clear();
            aptReportTableView.getItems().clear();
            Appointment.getDatabaseApts();

            if (selectedTabIndex != 0) {
                if (selectedTabIndex < 3) {
                    aptFilterTabSelector.getSelectionModel().select(3);
                    aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
                } else {
                    aptFilterTabSelector.getSelectionModel().select(2);
                    aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
                }
            }

        }

    }

    /**
     * Opens the add/update customer and sets the default values. Disables the main window.
     */
    public void addCustomerButtonClicked() {
        tableViews.setDisable(true);
        logOutButton.setDisable(true);
        addUpdateCustomer.setVisible(true);
        customerAddUpdateLabel.setText("Add Customer");

        stateProvinceBox.setItems(Division.getAllDivisionNames());
        countryBox.setItems(Country.getAllCountryNames());
    }

    /**
     * opens the add/update customer and sets the values based on the selected customer. Prompts the user to select a customer
     * if one isn't selected. Disables the main window.
     */
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

    /**
     * Removes customer from the customers table in the client_schedule database. Removes all associated appointments based
     * on user_ID from the appointments table in the client_schedule database. Prompts the user for confirmation to delete
     * the customer and all associated appointments (provides a list of associated appointments).
     *
     */
    public void removeCustomerButtonClicked() {
        StringBuilder appointmentsScheduled;

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        appointmentsScheduled = new StringBuilder(" | ");

        for (Appointment apt : Appointment.getAllapts()) {
            if (apt.getCustomerId() == selectedCustomer.getCustomerId()) {
                appointmentsScheduled.append(apt.getAptId()).append(" | ");
            }
        }
        if (!appointmentsScheduled.toString().equals(" | ")) {
            if (promptHelper.confirmPrompt("Customer appointments found!",selectedCustomer.getCustomerName() + " has at least one existing appointment. Removing " + selectedCustomer.getCustomerName() + "'s customer record will also remove the following appointments " + appointmentsScheduled + ". Are you sure you would like to remove this customer?")) {

                for (Appointment apt : Appointment.getAllapts()) {
                    if (apt.getCustomerId() == selectedCustomer.getCustomerId()) {
                        Appointment.remeoveApt(apt.getAptId());
                    }
                }

                aptTableView.getItems().clear();
                Appointment.getDatabaseApts();

                Customer.remeoveCustomer(selectedCustomer.getCustomerId());
                customerTableView.getItems().clear();
                Customer.getDatabaseCustomers();
            }
        } else {
            if (promptHelper.confirmPrompt("Remove Customer Confirmation","Are you sure you would like to remove " + selectedCustomer.getCustomerName() + "'s customer record?")) {

                Customer.remeoveCustomer(selectedCustomer.getCustomerId());
                customerTableView.getItems().clear();
                Customer.getDatabaseCustomers();
            }
        }
    }

    //----------MainWindowEnd------------

    //---------------------------------------
    //---------------------------------------
    //Add-Update Appointment Window Functions
    //---------------------------------------
    //---------------------------------------

    /**
     * Sets the username and ID to the current logged-in user on the add/update appointment window.
     */
    public void userIdControl() {
        String userId = User.getUserIdFromName(aptUserIdBox.getValue()).toString();
        aptUserIdLabel.setText(userId);
    }

    /**
     * Sets the appointment end date to the same as the appointment start date on the add/update appointment window.
     */
    public void setAptEndDate() {
        aptEndDate.setValue(aptStartDate.getValue());
    }

    /**
     * Sets the end hours dropdown list based on the start hours selection on the add/update appointment window.
     */
    public void endHrsSelection() {
        aptEndHrs.getSelectionModel().clearSelection();
        aptEndMin.getSelectionModel().clearSelection();
        ObservableList<String> endHrsList = FXCollections.observableArrayList();
        int startCk = 0;
        int startCk2 = 0;
        for (String time : aptStartHrs.getItems()) {
            if (aptStartHrs.getValue().equals(time)) {
                startCk = 1;
            }
            if (startCk == 1) {
                if (startCk2 == 1) {
                    endHrsList.add(time);
                }
                if (Objects.nonNull(aptStartMin.getValue())) {
                    if (aptStartMin.getValue().equals("45")) {
                        startCk2 = 1;
                    } else {
                        endHrsList.add(time);
                    }
                } else {
                    endHrsList.add(time);
                }
            }
        }
        aptEndHrs.setItems(endHrsList);
    }

    /**
     * Sets the end minutes dropdown list based on the start minute selection on the add/update appointment window if start
     * minutes is set.
     */
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
        if (aptStartMin.getValue().equals("45")) {
            endHrsSelection();
        }
    }

    /**
     * Sets the end minutes dropdown list based on the start minute selection on the add/update appointment window if start
     * minutes is NOT set.
     */
    public void endMinSelectionAfterEndHr() {
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

    /**
     * Commits appointment add or change to the appointments table in the client schedule database. Prompts the user for
     * validation errors if they exist. Executes sql INSERT INTO if adding an appointment and executes sql UPDATE on selected
     * appointment if updating an appointment.
     *
     */
    public void submitAptButtonClicked() {
        int id = 0, userId, contactId, customerId, errorCk = 0, startHrs = 0, startMin = 0, endHrs = 0 , endMin = 0 ;
        String title, location, type, desc, startDate, endDate;
        Timestamp startDateTime, endDateTime, oldStartDateTime, oldEndDateTime;
        String[] startDateSplit, endDateSplit;
        long aptEpochStart, aptEpochEnd;

        if (!aptIdBox.getText().equals("")) {
            id = Integer.parseInt(aptIdBox.getText());
        }

        userId = Integer.parseInt(aptUserIdLabel.getText());
        if (userId == 0) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","Please select a user to continue");
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

        startDate = aptStartDate.getEditor().getText();
        if (startDate == null) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The start date is required.");
        } else  {
            startDateSplit = startDate.split("-");
            if (startDateSplit.length > 1) {
                startDate = startDateSplit[0] + "-" + startDateSplit[1] + "-" + startDateSplit[2];
            } else {
                startDateSplit = startDate.split("/");
                startDate = startDateSplit[2] + "-" + startDateSplit[0] + "-" + startDateSplit[1];
                if (Integer.parseInt(startDateSplit[0]) < 10) {
                    startDateSplit[0] = "0" + startDateSplit[0];
                }
                if (Integer.parseInt(startDateSplit[1]) < 10) {
                    startDateSplit[1] = "0" + startDateSplit[1];
                }
            }
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

        endDate = aptEndDate.getEditor().getText();
        if (endDate == null) {
            errorCk++;
            promptHelper.errorDialog("Validation Error","The end date is required.");
        } else {
            endDateSplit = endDate.split("-");
            if (endDateSplit.length > 1) {
                endDate = endDateSplit[0] + "-" + endDateSplit[1] + "-" + endDateSplit[2];
            } else {
                endDateSplit = endDate.split("/");
                endDate = endDateSplit[2] + "-" + endDateSplit[0] + "-" + endDateSplit[1];
                if (Integer.parseInt(endDateSplit[0]) < 10) {
                    endDateSplit[0] = "0" + endDateSplit[0];
                }
                if (Integer.parseInt(endDateSplit[1]) < 10) {
                    endDateSplit[1] = "0" + endDateSplit[1];
                }
            }

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
        oldStartDateTime = Timestamp.valueOf(oldStart.getText());
        oldEndDateTime = Timestamp.valueOf(oldEnd.getText());

        //                                                    [0]              [1]
        //                                                start before      end before         [2]          [3]
        //                                                window open      window open,     new start     new end
        Timestamp[] compTimestampArr = new Timestamp[] {oldStartDateTime, oldEndDateTime, startDateTime, endDateTime};

        long[] compEpochArr = DateTimeProcessing.timestampArrToEpoch(compTimestampArr);

        int overlapCk = 0;
        int afterTodayCk = 0;
        int endBeforeStartCk = 0;
        for (Appointment apt : Appointment.getAllapts()) {
            Instant aptStartInstant = apt.getAptStartDateTime().toInstant();
            aptEpochStart = aptStartInstant.getEpochSecond();
            Instant aptEndInstant = apt.getAptEndDateTime().toInstant();
            aptEpochEnd = aptEndInstant.getEpochSecond();
            if (!Integer.toString(apt.getAptId()).equals(aptIdBox.getText())) {
                //if new start is greater or equal to apt start and new start is less than or equal to apt end
                if (compEpochArr[2] >= aptEpochStart && compEpochArr[2] <= aptEpochEnd) {
                    overlapCk++;
                }
                //if new end is greater or equal to apt start and new end is less than or equal to apt end
                if (compEpochArr[3] >= aptEpochStart && compEpochArr[3] <= aptEpochEnd) {
                    overlapCk++;
                }
            }
        }

        Instant currentInstant = Instant.now();
        long currentEpoch = currentInstant.getEpochSecond();

        if (compEpochArr[2] < currentEpoch) {
            afterTodayCk++;
        }

        if (compEpochArr[2] > compEpochArr[3]) {
            endBeforeStartCk++;
        }

        if (compEpochArr[0] == compEpochArr[2]) {
            overlapCk = 0;
            afterTodayCk = 0;
        }

        if (compEpochArr[1] == compEpochArr[3]) {
            overlapCk = 0;
            afterTodayCk = 0;
        }

        if (overlapCk > 0) {
            promptHelper.errorDialog("Validation Error","Existing appointment during your start or end time. Please select a different date and try again");
            errorCk++;
        }

        if (afterTodayCk > 0) {
            promptHelper.errorDialog("Validation Error","Appointment must be scheduled after the current date");
            errorCk++;
        }

        if (endBeforeStartCk > 0) {
            promptHelper.errorDialog("Validation Error","End Date must be after start date.");
            errorCk++;
        }

        if (errorCk == 0) {
            tableViews.setDisable(false);
            logOutButton.setDisable(false);
            addUpdateAptBox.setVisible(false);

            Appointment.insertApt(id,title,desc,location,type,startDateTime,endDateTime,userName.getText(),userName.getText(),customerId,userId,contactId);

            aptTableView.getItems().clear();
            aptReportTableView.getItems().clear();
            Appointment.getDatabaseApts();
            cleanAptForm();

            int selectedTabIndex = aptFilterTabSelector.getSelectionModel().getSelectedIndex();
            if (selectedTabIndex != 0) {
                if (selectedTabIndex < 3) {
                    aptFilterTabSelector.getSelectionModel().select(3);
                    aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
                } else {
                    aptFilterTabSelector.getSelectionModel().select(2);
                    aptFilterTabSelector.getSelectionModel().select(selectedTabIndex);
                }
            }

        }
    }

    /**
     * Closes the add update appointment window without changing or adding an appointment and returns the user to the main
     * window.
     */
    public void cancelAptButtonClicked() {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateAptBox.setVisible(false);

        cleanAptForm();
    }

    /**
     *  Sets the add/update appointment window from values based on the selected appointment in the table.
     *
     * @param selectedApt The selected appointment from the appointment table view.
     */
    public void setAppointment(Appointment selectedApt) {
        oldStart.setText(selectedApt.getAptStartDateTime().toString());
        oldEnd.setText(selectedApt.getAptEndDateTime().toString());

        aptStartDate.getEditor().clear();
        aptEndDate.getEditor().clear();
        aptIdBox.setText(Integer.toString(selectedApt.getAptId()));
        menuOpenCk.setText("0");

        aptUserIdBox.setValue(Integer.toString(selectedApt.getUserId()));
        aptUserIdLabel.setText(Integer.toString(selectedApt.getUserId()));
        aptUserIdBox.setValue(User.getUserNameFromId(selectedApt.getUserId()));
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

    /**
     * Displays the selected contact name's contact ID on the add/update appointment window.
     */
    public void contactGetIdByName() {
        int aptAddUpdateContactId;
        String contactName = aptContactBox.getValue();
        aptAddUpdateContactId = Contact.getContactIdByName(contactName);
        aptContactIdLabel.setText(String.valueOf(aptAddUpdateContactId));
    }

    /**
     * Displays the selected customer name's customer ID on the add/update appointment window.
     */
    public void customerGetIdByName() {
        int aptAddUpdateCustomerId;
        String customerName = aptCustomerBox.getValue();
        aptAddUpdateCustomerId = Customer.getCustomerIdByName(customerName);
        aptCustomerIdLabel.setText(String.valueOf(aptAddUpdateCustomerId));
    }

    /**
     * Clears values or sets values to default in the add/update appointment window.
     */
    public void cleanAptForm() {
        String user = userName.getText();
        String[] nowStringArr = DateTimeProcessing.splitDateTime(Timestamp.from(Instant.now()));
        int userId = User.getUserIdFromName(user);

        menuOpenCk.setText("0");
        aptIdBox.setText("");
        aptUserIdBox.setValue(user);
        aptUserIdLabel.setText(Integer.toString(userId));
        aptTitleBox.setText("");
        aptLocationBox.setText("");
        aptTypeBox.setText("");
        aptContactBox.setValue("");
        aptCustomerBox.setValue("");
        aptContactIdLabel.setText("");
        aptCustomerIdLabel.setText("");
        aptStartDate.setValue(now());
        aptStartDate.getEditor().setText(nowStringArr[0]);
        aptStartHrs.setValue("");
        aptStartMin.setValue("");
        aptEndDate.setValue(now());
        aptEndDate.getEditor().setText(nowStringArr[0]);
        aptEndHrs.setValue("");
        aptEndMin.setValue("");
        aptDescBox.setText("");
    }

    //----------EndAppointmentWindow------------

    //Add-Update Customer Window Functions

    /**
     * Closes the add/update customer window and returns user to the main window.
     */
    public void cancelCustomerButtonClicked() {
        tableViews.setDisable(false);
        logOutButton.setDisable(false);
        addUpdateCustomer.setVisible(false);

        cleanCustomerForm();
    }

    /**
     * Commits customer add or change to the customers table in the client schedule database. Prompts the user for validation
     * errors if they exist. Executes sql INSERT INTO if adding a customer and executes sql UPDATE on selected customer
     * if updating a customer.
     *
     */
    public void submitCustomerButtonClicked() {
        int id = 0, divisionId = 0, errorCk = 0;
        String name, address, postalCode, phoneNumber, menu;


        if (!customerIdBox.getText().equals("")) {
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
        if (postalCode.equals("")) {
            promptHelper.errorDialog("Input validation error!","The postal code is required");
            errorCk++;
        }
        phoneNumber = customerPhoneNumberBox.getText();
        if (phoneNumber.equals("")) {
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

    /**
     * Future development (not functional)
     *
     * Intended Purposes: If a first level division is selected before a country, the country is auto filled based on the
     * first level division selection in the add/update customer window.
     */
    public void setDivisionCountry() {
        int divisionId, countryId;

        divisionId = Division.getDivisionIdByName(stateProvinceBox.getValue());
        addUpdateProvinceIdLabel.setText(String.valueOf(divisionId));

//        if (addUpdateCountryIdLabel.getText().equals("0")) {
//            countryBox.setValue("");
//            addUpdateCountryIdLabel.setText("");
//
//            countryId = Division.getCountryIdByDivisionId(divisionId);
//            countryBox.setValue(Country.getCountryNameById(countryId));
//            addUpdateCountryIdLabel.setText(Integer.toString(countryId));
//        }
    }

    /**
     * Sets the available first level divisions based on the country selection in the add/update customer window.
     */
    public void setCountry() {
        int countryId;
        countryId = Country.getCountryIdByName(countryBox.getValue());
        addUpdateCountryIdLabel.setText(Integer.toString(countryId));
        ObservableList<String> divisions = Division.getAllDivisionNamesByCountry(countryId);

        stateProvinceBox.setItems(divisions);
    }

    /**
     * Sets the add/update customer form values based on the selected customer.
     *
     * @param selectedCustomer Selected customer from the customer table view in the main window.
     */
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

    /**
     * Clears values from the add/update customer window.
     */
    public void cleanCustomerForm() {
        customerIdBox.setText("");
        customerNameBox.setText("");
        customerAddressBox.setText("");
        stateProvinceBox.setValue("");
        countryBox.setValue("");
        customerPostalCodeBox.setText("");
        customerPhoneNumberBox.setText("");
    }

    //----------EndCustomerWindow------------

    /**
     * Initializes the default environment. Displays the login window in english if the system language is english and displays
     * the login form in French if the system language is French.
     *
     * @param location File path to the mainWindow.fxml file.
     * @param resources Passed resources (default null)
     */
    public void initialize(URL location, ResourceBundle resources) {
        systemZoneId = ZoneId.systemDefault();
        locationLabel.setText("Current Location: " + systemZoneId);
        frLocale = new Locale("fr", "FR");
        usLocale = new Locale("en", "US");
        sysLocale = Locale.getDefault();
        //sysLocale = Locale.FRANCE;

        if (sysLocale.getLanguage().equals(frLocale.getLanguage())) {
            rb1 = ResourceBundle.getBundle("resources.Lang", frLocale);
            //Login window language change
            loginLabel.setText(rb1.getString("login"));
            loginButton.setText(rb1.getString("login"));
            userName.setPromptText(rb1.getString("username"));
            passString.setPromptText(rb1.getString("password"));
            locationLabel.setText(rb1.getString("currentLocation") + ": " + systemZoneId.toString());
        }
    }
}