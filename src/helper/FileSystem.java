package helper;

import java.io.*;
import java.time.ZonedDateTime;

/**
 * File system helper class that handles file system operations.
 *
 * Other file methods expected in future releases.
 *
 * @author brandonLackey
 */
public class FileSystem {
    /**
     * Stores login activity to a log "./login_activity.txt".
     *
     * @param userName The user attempting to login.
     * @param date Timestamp of when the login event happened.
     * @param loginResponse The login response (Expected, success or fail).
     */
    public static void loginLog(String userName, ZonedDateTime date, String loginResponse) {
        try(FileWriter fw = new FileWriter("login_activity.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println("USER: " + userName + " | DATE AND TIME: " + date + " | LOGIN RESPONSE: " + loginResponse);
        } catch (IOException e) {
            promptHelper.errorDialog("log write error", "Unable to write to login log file.");
        }
    }
}
