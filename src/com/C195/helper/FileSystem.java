package com.C195.helper;

import java.io.*;
import java.time.ZonedDateTime;

/**
 * @author brandonLackey
 */
public class FileSystem {
    /**
     *
     * @param userName
     * @param date
     * @param loginResponse
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
