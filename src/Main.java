import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This appointment scheduler application is developed to manage customers and their appointments. The Main class sets up
 * the components required to start the main window.
 *
 * @author brandonLackey
 */
public class Main extends Application{

    /**
     * Starts the main window which displays the login window on load.
     *
     * @param primaryStage The primary stage used to render the main window GUI.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/mainWindow.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Appointment Management System");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Launches the appointment scheduler application.
     *
     * @param args Passed CLI arguments (not used in this application)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
