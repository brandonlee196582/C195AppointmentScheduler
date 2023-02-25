package com.C195.helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * @author brandonLackey
 */
public class promptHelper {
    /**
     *
     * @param title
     * @param content
     */
    public static void errorDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param title
     * @param header
     * @param content
     */
    public static void infoDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param title
     * @param content
     * @return
     */
    public static boolean confirmPrompt(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.YES;
    }

}
