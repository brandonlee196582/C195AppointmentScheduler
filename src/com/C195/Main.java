package com.C195;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * @author brandonLackey
 */
public class Main extends Application{

    /**
     *
     * @param primaryStage
     * @throws Exception
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
     *
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        launch(args);
    }
}
