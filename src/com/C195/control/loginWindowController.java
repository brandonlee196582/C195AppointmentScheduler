package com.C195.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class loginWindowController {

    private Parent scene;
    public void logInButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("control_view/fxml/MainWindow2.fxml")));
        stage.setTitle("Schedule Application");
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
