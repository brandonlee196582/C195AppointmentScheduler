package com.company;

import com.company.obj.*;
import dbConnection.JDBC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;


public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("control_view/fxml/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {

        String[] outColumns = {};

        JDBC.openConnection();

        outColumns = new String[]{"Customer_ID", "Customer_Name", "Address", "Postal_Code", "Phone"};
        Customer.getDatabaseCustomers("customers", outColumns);

        JDBC.closeConnection();

        launch(args);

    }
}
