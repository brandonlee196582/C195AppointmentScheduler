TITLE:
    WGU C195 - Appointment Scheduler Project

PURPOSE:
    This project implements system requirements detailed by solution statements form a system analyst. The solution statements
    led to the development of a graphical user interface application that communicates with a provided MySQL database. This
    system tracks appointments and manages data stored in the database for users, customers, and contacts.

APPLICATION INFORMATION:
    Author: Brandon Lackey
    Contact Email: brandonlee196582@gmail.com
    Application Version: 1.0.0
    Date: 2023-02-26

IDE INFORMATION:
    IDE: IntelliJ IDEA 2022.1.1 (Community Edition)

JDK INFORMATION:
    JDK: Java Oracle OpenJDK version 17.0.1

JAVAFX INFORMATION:
    javaFX: javaFX-sdk-17.06

MYSQL INFORMATION:
    MySQL: mysql-connector-java-8.0.25

APPLICATION RUN DIRECTIONS:
    1. Open IntelliJ CE
    2. Click "File" "Open" and navigate to where you stored the app and select "C195AppointmentScheduler" folder.
    3. After the project folder is open, click "File" "Project Structure"
    4. Set the "Project SDK" to "17 Java version 17.0.1" on the "Project" menu, click "Apply".
    5. Still on the "Project Structure" window in the "Libraries" tab click "+" to add a new project library and select "Java".
    6. Navigate to the "required files" folder provided with the project, expand "mysql-connector-java-8.0.25", then click
       "mysql-connector-java-8.0.25.jar", and click "Ok".
    7. Click "Ok" on the "Choose Modules" window.
    8. Click "+" to add another new project library and select "Java".
    9. Navigate to the "required files" folder provided with the project, expand "javafx-sdk-17.0.6", then expand "lib".
    10. Select all the .jar files in "lib" by clicking on the first file hold shift and click on the last file.
    11. Once items are selected click "Ok".
    12. Click "Ok" on the "Choose Modules" window.
    13. Click "Apply" then "Ok" on the "Project Structure" window.
    14. Click "Add Configuration" then "Add new" on the "Run/Debug Configurations" window and click "Application".
    15. Name you configuration to your desired name.
    16. Then click on the "Browse" button in the "Main class" box.
    17. Click the "Project" tab, expand "C195AppointmentScheduler", expand "src", click "Main", and click "Ok".
    18. Still in the "Run/Debug Configurations" click the folder icon in the "Working directory" box.
    19. In the "Select Working Directory" window, ensure "C195AppointmentScheduler" is selected, and click "Ok"
    20. Click "Apply" on the "Run/Debug Configurations" window.
    21. Still in the "Run/Debug Configurations" window, "Modify options", then "Add VM options"
    22. Expand the VM Options box and enter the following information in the box, the module path should start with the
        location of your "C195AppointmentScheduler" folder (Example - "C:\Users\{YOUR USER FOLDER}\Desktop\C195AppointmentScheduler")
            --module-path "{path to C195AppointmentScheduler}\javafx-sdk-17.0.6\lib" --add-modules=javafx.controls,javafx.fxml
    23. Click "Apply" then "Ok" on the "Run/Debug Configurations" window.
    24. Click "Build" then "Build Project". Wait for this to complete.
    25. Click the play button.

ADDITIONAL REPORT DESCRIPTION:
    My additional report is a custom report builder. To run a custom report, navigate to the "Custom Report Builder tab".
    By default, the report displays all appointments after the date selected in the "Filter Start Date" box. You can change
    this to display returns for all reports by checking the "All Time Results" check box. The user can further filter the
    results by setting the "Layer One Selector" to "Contact" then set the "Layer One Value" to "Li Lee". The table view will
    now display all appointments for Li Lee after the filter start date or all time if the "All Time Results" checkbox is
    checked. The user will also receive a count of the returned results in the "Report Return" label. This can provide the
    user the same information as report two with the added ability to see a schedule for sometime in the future if you change
    the "Filter Start Date". The user can now further filter the results but selecting Customer on the "Layer Two Selector"
    and setting the "Layer Two Value" to "Daddy Warbucks". The user will now get a result for all the appointments Li Lee
    has scheduled for Daddy Warbucks. These reports can be built somewhat dynamically in that the selectors are set to 4
    options, Customer, contact, Type, or Location but the value selections are set by what values are available in the data
    (these values are not hard coded). If the user is unsure how to operate the custom report builder, I have provided a
    help menu as a guide.

