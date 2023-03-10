package helper;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Class containing user error, information, and confirm prompt methods.
 *
 * @author brandonLackey
 */
public class promptHelper {
    /**
     *  Triggers an error prompt containing the passed title, ok button, and message.
     *
     * @param title The title for the error prompt.
     * @param content The prompt content.
     */
    public static void errorDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Triggers an information prompt containing the passed title, header, ok button, and message.
     *
     * @param title The title for the information prompt.
     * @param header The header for the information prompt.
     * @param content The content for the information prompt.
     */
    public static void infoDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Triggers a confirmation prompt containing the passed title and message, and yes and no buttons.
     *
     * @param title The title for the confirmation prompt.
     * @param content The content for the confirmation prompt.
     * @return Returns the result of the prompt.
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
